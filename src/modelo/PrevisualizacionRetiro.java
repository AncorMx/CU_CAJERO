package modelo;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula el cálculo previo
 * del estado de cuenta antes de confirmar el retiro.
 *
 * Utilizado por la vista VistaEstadoCuenta para mostrar el desglose en tiempo real.
 */
public class PrevisualizacionRetiro {
    private final String titular;
    private final String numeroCuenta;
    private final double saldoActual;
    private final double montoRetiro;
    private final double comision;
    private final double montoTotalDescontar;
    private final double saldoEstimado;
    private final boolean esViable;
    private final double montoFaltante;
    private final String etiquetaComision;

    public PrevisualizacionRetiro(
        String titular,
        String numeroCuenta,
        double saldoActual,
        double montoRetiro,
        double comision,
        String etiquetaComision
    ) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldoActual = saldoActual;
        this.montoRetiro = montoRetiro;
        this.comision = comision;
        this.etiquetaComision = etiquetaComision != null ? etiquetaComision : "Comisión (2%):";
        this.montoTotalDescontar = montoRetiro + comision;
        this.saldoEstimado = saldoActual - this.montoTotalDescontar;
        this.esViable = saldoActual >= this.montoTotalDescontar;
        this.montoFaltante = esViable ? 0.0 : (this.montoTotalDescontar - saldoActual);
    }

    public String getTitular() {
        return titular;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public double getMontoRetiro() {
        return montoRetiro;
    }

    public double getComision() {
        return comision;
    }

    public double getMontoTotalDescontar() {
        return montoTotalDescontar;
    }

    public double getSaldoEstimado() {
        return saldoEstimado;
    }

    public boolean isEsViable() {
        return esViable;
    }

    public double getMontoFaltante() {
        return montoFaltante;
    }

    public String getEtiquetaComision() {
        return etiquetaComision;
    }

    @Override
    public String toString() {
        return String.format(
            "PrevisualizacionRetiro[Cliente=%s, SaldoActual=$%.2f, Monto=$%.2f, Comision=$%.2f, Total=$%.2f, Estimado=$%.2f, Viable=%s]",
            titular, saldoActual, montoRetiro, comision, montoTotalDescontar, saldoEstimado, esViable
        );
    }
}
