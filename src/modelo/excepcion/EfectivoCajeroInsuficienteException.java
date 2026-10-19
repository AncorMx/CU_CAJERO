package modelo.excepcion;

/**
 * Excepción lanzada cuando el cajero automático no dispone de suficiente
 * efectivo físico en sus dispensadores para cubrir el retiro.
 */
public class EfectivoCajeroInsuficienteException extends CajeroException {
    private final double efectivoDisponibleCajero;
    private final double montoSolicitado;

    public EfectivoCajeroInsuficienteException(double efectivoDisponibleCajero, double montoSolicitado) {
        super("ATM-502", String.format(
            "El cajero no cuenta con suficiente efectivo disponible ($%.2f) para el monto solicitado ($%.2f).",
            efectivoDisponibleCajero, montoSolicitado
        ));
        this.efectivoDisponibleCajero = efectivoDisponibleCajero;
        this.montoSolicitado = montoSolicitado;
    }

    public double getEfectivoDisponibleCajero() {
        return efectivoDisponibleCajero;
    }

    public double getMontoSolicitado() {
        return montoSolicitado;
    }
}
