package modelo.excepcion;

/**
 * Excepción lanzada cuando los fondos de la cuenta (saldo disponible)
 * no cubren el monto solicitado más la comisión calculada.
 */
public class SaldoInsuficienteException extends CajeroException {
    private final double saldoDisponible;
    private final double montoRequerido;
    private final double montoFaltante;

    public SaldoInsuficienteException(double saldoDisponible, double montoRequerido) {
        super("ATM-401", String.format(
            "Saldo insuficiente. Saldo disponible: $%.2f, Monto requerido (con comisión): $%.2f",
            saldoDisponible, montoRequerido
        ));
        this.saldoDisponible = saldoDisponible;
        this.montoRequerido = montoRequerido;
        this.montoFaltante = Math.max(0.0, montoRequerido - saldoDisponible);
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public double getMontoRequerido() {
        return montoRequerido;
    }

    public double getMontoFaltante() {
        return montoFaltante;
    }
}
