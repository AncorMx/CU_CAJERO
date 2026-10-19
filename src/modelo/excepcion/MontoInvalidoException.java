package modelo.excepcion;

/**
 * Excepción lanzada cuando el monto ingresado no cumple con las reglas del cajero
 * (por ejemplo, menor o igual a cero, o no ser múltiplo de $200.00).
 */
public class MontoInvalidoException extends CajeroException {
    private final double montoIngresado;

    public MontoInvalidoException(double montoIngresado, String razon) {
        super("ATM-400", String.format("Monto inválido ($%.2f): %s", montoIngresado, razon));
        this.montoIngresado = montoIngresado;
    }

    public double getMontoIngresado() {
        return montoIngresado;
    }
}
