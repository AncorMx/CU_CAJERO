package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa el comprobante o ticket oficial emitido por el cajero
 * tras la finalización exitosa de un retiro.
 */
public class Recibo {
    private final String folio;
    private final LocalDateTime fechaHora;
    private final String numeroCuenta;
    private final String titular;
    private final String concepto;
    private final double montoRetiro;
    private final double comision;
    private final double totalDebitado;
    private final double saldoRestante;

    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    public Recibo(
        String folio,
        LocalDateTime fechaHora,
        String numeroCuenta,
        String titular,
        String concepto,
        double montoRetiro,
        double comision,
        double totalDebitado,
        double saldoRestante
    ) {
        this.folio = folio;
        this.fechaHora = fechaHora != null ? fechaHora : LocalDateTime.now();
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.concepto = concepto != null ? concepto : "Retiro Efectivo";
        this.montoRetiro = montoRetiro;
        this.comision = comision;
        this.totalDebitado = totalDebitado;
        this.saldoRestante = saldoRestante;
    }

    public String getFolio() {
        return folio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getFechaHoraFormateada() {
        return fechaHora.format(FORMATO_FECHA);
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public String getConcepto() {
        return concepto;
    }

    public double getMontoRetiro() {
        return montoRetiro;
    }

    public double getComision() {
        return comision;
    }

    public double getTotalDebitado() {
        return totalDebitado;
    }

    public double getSaldoRestante() {
        return saldoRestante;
    }

    /**
     * Genera la representación en texto del ticket de cajero automático.
     */
    public String generarTextoTicket() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("            CAJERO AUTOMÁTICO           \n");
        sb.append(String.format("         Fecha: %s\n", getFechaHoraFormateada()));
        sb.append(String.format("         Folio: %s\n", folio));
        sb.append("========================================\n");
        sb.append(String.format("Cuenta:      %s\n", numeroCuenta));
        sb.append(String.format("Cliente:     %s\n", titular));
        sb.append(String.format("Concepto:    %s\n", concepto));
        sb.append("----------------------------------------\n");
        sb.append(String.format("Monto:       $%10.2f\n", montoRetiro));
        sb.append(String.format("Comisión:    $%10.2f\n", comision));
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL:       $%10.2f\n", totalDebitado));
        sb.append("----------------------------------------\n");
        sb.append(String.format("Saldo Nuevo: $%10.2f\n", saldoRestante));
        sb.append("========================================\n");
        sb.append("       ¡Gracias por su preferencia!     \n");
        sb.append("========================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return generarTextoTicket();
    }
}
