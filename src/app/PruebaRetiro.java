package app;

import modelo.Cuenta;
import modelo.InventarioEfectivoCajero;
import modelo.PoliticaComisionPorcentaje;
import modelo.PrevisualizacionRetiro;
import modelo.Recibo;
import modelo.ServicioRetiro;
import modelo.excepcion.CajeroException;
import modelo.excepcion.SaldoInsuficienteException;

/**
 * Pruebas unitarias de las operaciones y reglas de negocio del servicio de retiro.
 */
public class PruebaRetiro {

    public static void main(String[] args) {
        System.out.println("Iniciando pruebas del sistema de retiro...");
        int exitos = 0;
        int fallos = 0;

        // PRUEBA 1: Retiro estándar de $600.00
        try {
            Cuenta cuenta = new Cuenta("**** 4589", "Juan Pérez", 5000.00);
            InventarioEfectivoCajero inventario = new InventarioEfectivoCajero(50000.00);
            PoliticaComisionPorcentaje politica = new PoliticaComisionPorcentaje(0.02, 5.00);
            ServicioRetiro servicio = new ServicioRetiro(inventario, politica);

            // Previsualización
            PrevisualizacionRetiro prev = servicio.previsualizarRetiro(cuenta, 600.00);
            assertIgual(prev.getSaldoActual(), 5000.00, "Saldo inicial de previsualización");
            assertIgual(prev.getMontoRetiro(), 600.00, "Monto solicitado");
            assertIgual(prev.getComision(), 12.00, "Comisión del 2%");
            assertIgual(prev.getMontoTotalDescontar(), 612.00, "Total a debitar");
            assertIgual(prev.getSaldoEstimado(), 4388.00, "Saldo estimado resultante");

            // Ejecución
            Recibo recibo = servicio.ejecutarRetiro(cuenta, 600.00);
            assertIgual(cuenta.getSaldo(), 4388.00, "Saldo posterior en la cuenta");
            assertIgual(recibo.getTotalDebitado(), 612.00, "Total debitado en el recibo");
            assertIgual(recibo.getMontoRetiro(), 600.00, "Monto en el recibo");
            assertIgual(inventario.getEfectivoDisponible(), 49400.00, "Efectivo restante en cajero");

            System.out.println("✔ PRUEBA 1: Retiro exitoso ($600.00) PASÓ.");
            exitos++;
        } catch (Exception e) {
            System.err.println("✘ PRUEBA 1 FALLÓ: " + e.getMessage());
            fallos++;
        }

        // PRUEBA 2: Comisión mínima ($5.00) en retiro de $200.00
        try {
            Cuenta cuenta = new Cuenta("**** 4589", "Juan Pérez", 5000.00);
            PoliticaComisionPorcentaje politica = new PoliticaComisionPorcentaje(0.02, 5.00);
            ServicioRetiro servicio = new ServicioRetiro(new InventarioEfectivoCajero(50000.00), politica);

            PrevisualizacionRetiro prev = servicio.previsualizarRetiro(cuenta, 200.00);
            assertIgual(prev.getComision(), 5.00, "Comisión mínima aplicada");
            assertIgual(prev.getMontoTotalDescontar(), 205.00, "Total a descontar");

            System.out.println("✔ PRUEBA 2: Aplicación de comisión mínima PASÓ.");
            exitos++;
        } catch (Exception e) {
            System.err.println("✘ PRUEBA 2 FALLÓ: " + e.getMessage());
            fallos++;
        }

        // PRUEBA 3: Saldo Insuficiente
        try {
            Cuenta cuenta = new Cuenta("**** 4589", "Juan Pérez", 500.00);
            PoliticaComisionPorcentaje politica = new PoliticaComisionPorcentaje(0.02, 5.00);
            ServicioRetiro servicio = new ServicioRetiro(new InventarioEfectivoCajero(50000.00), politica);

            PrevisualizacionRetiro prev = servicio.previsualizarRetiro(cuenta, 1000.00);
            assertIgual(prev.getComision(), 20.00, "Comisión para $1,000");
            assertIgual(prev.getMontoFaltante(), 520.00, "Faltante calculado");

            boolean lanzoExcepcion = false;
            try {
                servicio.ejecutarRetiro(cuenta, 1000.00);
            } catch (SaldoInsuficienteException ex) {
                lanzoExcepcion = true;
                assertIgual(ex.getMontoFaltante(), 520.00, "Faltante en excepción");
                assertIgual(ex.getSaldoDisponible(), 500.00, "Saldo disponible en excepción");
            }

            if (!lanzoExcepcion) {
                throw new IllegalStateException("Debió lanzar SaldoInsuficienteException.");
            }
            assertIgual(cuenta.getSaldo(), 500.00, "Saldo intacto tras fallo");

            System.out.println("✔ PRUEBA 3: Saldo insuficiente PASÓ.");
            exitos++;
        } catch (Exception e) {
            System.err.println("✘ PRUEBA 3 FALLÓ: " + e.getMessage());
            fallos++;
        }

        // PRUEBA 4: Falla de Servidor ATM-502
        try {
            Cuenta cuenta = new Cuenta("**** 4589", "Juan Pérez", 5000.00);
            ServicioRetiro servicio = new ServicioRetiro();
            servicio.setSimularErrorServidor(true);

            boolean lanzo502 = false;
            try {
                servicio.ejecutarRetiro(cuenta, 600.00);
            } catch (CajeroException ex) {
                if ("ATM-502".equals(ex.getCodigoError())) {
                    lanzo502 = true;
                }
            }

            if (!lanzo502) {
                throw new IllegalStateException("Debió capturarse el código ATM-502.");
            }
            assertIgual(cuenta.getSaldo(), 5000.00, "Saldo intacto tras error 502");

            System.out.println("✔ PRUEBA 4: Error ATM-502 PASÓ.");
            exitos++;
        } catch (Exception e) {
            System.err.println("✘ PRUEBA 4 FALLÓ: " + e.getMessage());
            fallos++;
        }

        // PRUEBA 5: Cancelación de Operación
        try {
            Cuenta cuenta = new Cuenta("**** 4589", "Juan Pérez", 5000.00);
            ServicioRetiro servicio = new ServicioRetiro();
            servicio.registrarCancelacion(cuenta, 600.00);
            assertIgual(cuenta.getSaldo(), 5000.00, "Saldo sin afectación por cancelación");
            assertIgual((double) servicio.getHistoricoTransacciones().size(), 1.0, "Registro en historial");

            System.out.println("✔ PRUEBA 5: Cancelación de operación PASÓ.");
            exitos++;
        } catch (Exception e) {
            System.err.println("✘ PRUEBA 5 FALLÓ: " + e.getMessage());
            fallos++;
        }

        System.out.println("\n-------------------------------------------------");
        System.out.printf("RESULTADO FINAL: %d pasadas, %d fallidas.\n", exitos, fallos);
        System.out.println("-------------------------------------------------");
        if (fallos > 0) {
            System.exit(1);
        }
    }

    private static void assertIgual(double real, double esperado, String concepto) {
        if (Math.abs(real - esperado) > 0.001) {
            throw new AssertionError(String.format("%s: esperado %.2f pero fue %.2f", concepto, esperado, real));
        }
    }
}
