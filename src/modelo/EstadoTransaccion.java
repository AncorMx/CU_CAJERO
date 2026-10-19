package modelo;

/**
 * Enumeración que representa los posibles estados finales o de control
 * de una transacción de retiro en el cajero automático.
 */
public enum EstadoTransaccion {
    PENDIENTE("Pendiente de confirmación"),
    PROCESANDO("Procesando en el dispensador"),
    EXITOSA("Transacción completada exitosamente"),
    RECHAZADA_SALDO_INSUFICIENTE("Rechazada por saldo insuficiente"),
    FALLIDA_ERROR_SISTEMA("Fallida por error en dispensador o servidor"),
    CANCELADA("Cancelada por el usuario");

    private final String descripcion;

    EstadoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
