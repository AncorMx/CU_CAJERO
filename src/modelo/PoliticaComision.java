package modelo;

/**
 * Interfaz que define el contrato para el cálculo de comisiones en transacciones de retiro.
 * Aplica el patrón Estrategia (Strategy) para permitir diferentes políticas de comisión
 * (porcentaje, tarifa fija, clientes preferenciales, etc.).
 */
public interface PoliticaComision {

    /**
     * Calcula la comisión aplicable sobre un monto de retiro dado.
     *
     * @param montoRetiro Monto en efectivo a retirar.
     * @return Valor monetario de la comisión calculada.
     */
    double calcularComision(double montoRetiro);

    /**
     * Retorna la tasa porcentual en formato decimal (ej: 0.02 para 2%).
     */
    double getTasaPorcentaje();

    /**
     * Retorna el importe mínimo de comisión estipulado.
     */
    double getComisionMinima();

    /**
     * Retorna una descripción textual de la política (ej: "2% (Mínimo $5.00)").
     */
    String getDescripcion();
}
