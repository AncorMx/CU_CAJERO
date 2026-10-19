package modelo;

/**
 * Implementación de PoliticaComision basada en una tasa porcentual
 * con un umbral de cobro mínimo.
 *
 * Configuración requerida por la regla de negocio:
 * - Tasa del 2% (0.02)
 * - Comisión mínima: $5.00
 *
 * Ejemplo:
 * - Para $600.00: 2% = $12.00 (> $5.00) -> Comisión = $12.00
 * - Para $200.00: 2% = $4.00 (< $5.00)  -> Comisión = $5.00
 */
public class PoliticaComisionPorcentaje implements PoliticaComision {
    private final double tasaPorcentaje;
    private final double comisionMinima;

    /**
     * Constructor por defecto con las reglas de negocio especificadas:
     * 2% con mínimo de $5.00.
     */
    public PoliticaComisionPorcentaje() {
        this(0.02, 5.00);
    }

    /**
     * Constructor configurable.
     *
     * @param tasaPorcentaje Tasa decimal (ej: 0.02 para 2%).
     * @param comisionMinima Comisión mínima en moneda local.
     */
    public PoliticaComisionPorcentaje(double tasaPorcentaje, double comisionMinima) {
        if (tasaPorcentaje < 0 || comisionMinima < 0) {
            throw new IllegalArgumentException("La tasa y la comisión mínima deben ser no negativas.");
        }
        this.tasaPorcentaje = tasaPorcentaje;
        this.comisionMinima = comisionMinima;
    }

    @Override
    public double calcularComision(double montoRetiro) {
        if (montoRetiro <= 0) {
            return 0.0;
        }
        double calculada = montoRetiro * this.tasaPorcentaje;
        return Math.max(calculada, this.comisionMinima);
    }

    @Override
    public double getTasaPorcentaje() {
        return tasaPorcentaje;
    }

    @Override
    public double getComisionMinima() {
        return comisionMinima;
    }

    @Override
    public String getDescripcion() {
        return String.format("%.0f%% (Mín. $%.2f)", (tasaPorcentaje * 100), comisionMinima);
    }

    /**
     * Texto para visualización en interfaz (ej: "Comisión (2%)").
     */
    public String getEtiquetaResumen() {
        return String.format("Comisión (%.0f%%):", (tasaPorcentaje * 100));
    }
}
