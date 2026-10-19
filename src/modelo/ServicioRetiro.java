package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import modelo.excepcion.CajeroException;
import modelo.excepcion.EfectivoCajeroInsuficienteException;
import modelo.excepcion.MontoInvalidoException;
import modelo.excepcion.SaldoInsuficienteException;

/**
 * Servicio central del dominio bancario / cajero automático.
 * Coordina la previsualización del estado de cuenta y la ejecución atómica
 * de la transacción de retiro de efectivo.
 */
public class ServicioRetiro {
    private final InventarioEfectivoCajero inventarioCajero;
    private final PoliticaComision politicaComision;
    private final List<TransaccionRetiro> historicoTransacciones;
    private boolean simularErrorServidor = false;

    /**
     * Constructor con dependencias inyectadas.
     *
     * @param inventarioCajero Control de efectivo físico del cajero.
     * @param politicaComision Estrategia de cálculo de comisión.
     */
    public ServicioRetiro(InventarioEfectivoCajero inventarioCajero, PoliticaComision politicaComision) {
        if (inventarioCajero == null) {
            throw new IllegalArgumentException("El inventario del cajero no puede ser nulo.");
        }
        if (politicaComision == null) {
            throw new IllegalArgumentException("La política de comisión no puede ser nula.");
        }
        this.inventarioCajero = inventarioCajero;
        this.politicaComision = politicaComision;
        this.historicoTransacciones = new ArrayList<>();
    }

    /**
     * Constructor conveniente con configuración estándar del caso de uso:
     * - Efectivo en cajero: $50,000.00
     * - Comisión: 2% con mínimo de $5.00
     */
    public ServicioRetiro() {
        this(new InventarioEfectivoCajero(50000.00), new PoliticaComisionPorcentaje(0.02, 5.00));
    }

    /**
     * Valida que el monto solicitado cumpla las reglas del cajero:
     * - Mayor a cero.
     * - Múltiplo exacto de $200.00.
     *
     * @param monto Monto a validar.
     * @throws MontoInvalidoException si no cumple con las restricciones.
     */
    public void validarMonto(double monto) throws MontoInvalidoException {
        if (monto <= 0) {
            throw new MontoInvalidoException(monto, "El monto debe ser estrictamente mayor a $0.00.");
        }
        if (monto % 200.0 != 0) {
            throw new MontoInvalidoException(monto, "El monto debe ser múltiplo de $200.00 ($200, $400, $600, $800, etc.).");
        }
    }

    /**
     * Genera la previsualización del estado de cuenta antes de confirmar el retiro.
     *
     * @param cuenta Cuenta del cliente.
     * @param montoRetiro Monto seleccionado para retiro.
     * @return DTO PrevisualizacionRetiro con el desglose exacto (Monto, Comisión, Saldo Restante).
     */
    public PrevisualizacionRetiro previsualizarRetiro(Cuenta cuenta, double montoRetiro) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }
        double comision = politicaComision.calcularComision(montoRetiro);
        String etiqueta = (politicaComision instanceof PoliticaComisionPorcentaje)
            ? ((PoliticaComisionPorcentaje) politicaComision).getEtiquetaResumen()
            : "Comisión:";

        return new PrevisualizacionRetiro(
            cuenta.getTitular(),
            cuenta.getNumeroCuenta(),
            cuenta.getSaldo(),
            montoRetiro,
            comision,
            etiqueta
        );
    }

    /**
     * Ejecuta el retiro de efectivo de manera atómica, aplicando las validaciones de negocio:
     * 1. Valida el monto (múltiplo de 200).
     * 2. Simula verificación de red / servidor (ATM-502 si estuviera forzado).
     * 3. Valida fondos suficientes en la cuenta (monto + comisión).
     * 4. Valida disponibilidad de efectivo en el cajero físico.
     * 5. Descuenta el total de la cuenta.
     * 6. Dispensa el efectivo físico del cajero.
     * 7. Genera y retorna el Recibo oficial.
     *
     * @param cuenta Cuenta origen del débito.
     * @param montoRetiro Monto a retirar.
     * @return Recibo detallado con los datos de la transacción.
     * @throws SaldoInsuficienteException si la cuenta no tiene fondos suficientes.
     * @throws EfectivoCajeroInsuficienteException si el cajero no tiene efectivo disponible.
     * @throws CajeroException si ocurre un error de comunicación o del sistema.
     */
    public synchronized Recibo ejecutarRetiro(Cuenta cuenta, double montoRetiro)
            throws SaldoInsuficienteException, EfectivoCajeroInsuficienteException, CajeroException {

        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }

        // 1. Validación de monto
        validarMonto(montoRetiro);

        double comision = politicaComision.calcularComision(montoRetiro);
        double montoTotal = montoRetiro + comision;
        double saldoAnterior = cuenta.getSaldo();

        TransaccionRetiro transaccion = new TransaccionRetiro(
            cuenta.getNumeroCuenta(),
            montoRetiro,
            comision,
            saldoAnterior
        );

        // 2. Simulación de fallo en servidor/conexión si está activado
        if (simularErrorServidor) {
            transaccion.marcarFallida("ATM-502", "Error de comunicación con el servidor central bancario.");
            historicoTransacciones.add(transaccion);
            throw new CajeroException("ATM-502", "Ha ocurrido un error al procesar su solicitud.");
        }

        // 3. Validación de fondos suficientes en la cuenta
        if (!cuenta.tieneFondosSuficientes(montoTotal)) {
            transaccion.marcarRechazadaSaldo(String.format(
                "Saldo insuficiente. Requerido: $%.2f, Disponible: $%.2f", montoTotal, saldoAnterior
            ));
            historicoTransacciones.add(transaccion);
            throw new SaldoInsuficienteException(saldoAnterior, montoTotal);
        }

        // 4. Validación de efectivo en dispensadores del cajero
        if (!inventarioCajero.tieneEfectivoDisponible(montoRetiro)) {
            transaccion.marcarFallida("ATM-502", "Dispensador sin efectivo suficiente.");
            historicoTransacciones.add(transaccion);
            throw new EfectivoCajeroInsuficienteException(
                inventarioCajero.getEfectivoDisponible(),
                montoRetiro
            );
        }

        // 5. Aplicar descuento a la cuenta bancaria
        cuenta.descontar(montoTotal);

        // 6. Dispensar efectivo físico del cajero
        inventarioCajero.dispensarEfectivo(montoRetiro);

        // 7. Generar comprobante / recibo
        String folio = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Recibo recibo = new Recibo(
            folio,
            LocalDateTime.now(),
            cuenta.getNumeroCuentaFormateado(),
            cuenta.getTitular(),
            "Retiro Efectivo",
            montoRetiro,
            comision,
            montoTotal,
            cuenta.getSaldo()
        );

        transaccion.marcarExitosa(cuenta.getSaldo(), recibo);
        historicoTransacciones.add(transaccion);

        return recibo;
    }

    /**
     * Registra una transacción cancelada por el usuario en el historial.
     */
    public synchronized void registrarCancelacion(Cuenta cuenta, double montoRetiro) {
        if (cuenta != null) {
            double comision = politicaComision.calcularComision(montoRetiro);
            TransaccionRetiro tx = new TransaccionRetiro(cuenta.getNumeroCuenta(), montoRetiro, comision, cuenta.getSaldo());
            tx.marcarCancelada();
            historicoTransacciones.add(tx);
        }
    }

    public InventarioEfectivoCajero getInventarioCajero() {
        return inventarioCajero;
    }

    public PoliticaComision getPoliticaComision() {
        return politicaComision;
    }

    public List<TransaccionRetiro> getHistoricoTransacciones() {
        return Collections.unmodifiableList(historicoTransacciones);
    }

    public boolean isSimularErrorServidor() {
        return simularErrorServidor;
    }

    public void setSimularErrorServidor(boolean simularErrorServidor) {
        this.simularErrorServidor = simularErrorServidor;
    }
}
