package modelo.excepcion;

/**
 * Excepción base para errores ocurridos en el cajero automático.
 */
public class CajeroException extends Exception {
    private final String codigoError;

    public CajeroException(String mensaje) {
        super(mensaje);
        this.codigoError = "ATM-GENERIC";
    }

    public CajeroException(String codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public CajeroException(String codigoError, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
