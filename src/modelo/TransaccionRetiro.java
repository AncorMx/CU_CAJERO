package modelo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Registra los detalles históricos y de auditoría de un intento o ejecución de retiro.
 */
public class TransaccionRetiro {
    private final String idTransaccion;
    private final LocalDateTime fechaHora;
    private final String numeroCuenta;
    private final double montoRetiro;
    private final double comision;
    private final double montoTotal;
    private final double saldoAnterior;
    private double saldoPosterior;
    private EstadoTransaccion estado;
    private String mensajeDetalle;
    private Recibo recibo;

    public TransaccionRetiro(String numeroCuenta, double montoRetiro, double comision, double saldoAnterior) {
        this.idTransaccion = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.fechaHora = LocalDateTime.now();
        this.numeroCuenta = numeroCuenta;
        this.montoRetiro = montoRetiro;
        this.comision = comision;
        this.montoTotal = montoRetiro + comision;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoAnterior;
        this.estado = EstadoTransaccion.PENDIENTE;
        this.mensajeDetalle = "Transacción iniciada";
    }

    public void marcarExitosa(double saldoNuevo, Recibo recibo) {
        this.estado = EstadoTransaccion.EXITOSA;
        this.saldoPosterior = saldoNuevo;
        this.recibo = recibo;
        this.mensajeDetalle = "Retiro completado y efectivo dispensado";
    }

    public void marcarRechazadaSaldo(String motivo) {
        this.estado = EstadoTransaccion.RECHAZADA_SALDO_INSUFICIENTE;
        this.mensajeDetalle = motivo;
    }

    public void marcarFallida(String codigo, String motivo) {
        this.estado = EstadoTransaccion.FALLIDA_ERROR_SISTEMA;
        this.mensajeDetalle = String.format("[%s] %s", codigo, motivo);
    }

    public void marcarCancelada() {
        this.estado = EstadoTransaccion.CANCELADA;
        this.mensajeDetalle = "Operación cancelada por el usuario antes de la confirmación";
    }

    // Getters
    public String getIdTransaccion() {
        return idTransaccion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getMontoRetiro() {
        return montoRetiro;
    }

    public double getComision() {
        return comision;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public double getSaldoPosterior() {
        return saldoPosterior;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public String getMensajeDetalle() {
        return mensajeDetalle;
    }

    public Recibo getRecibo() {
        return recibo;
    }
}
