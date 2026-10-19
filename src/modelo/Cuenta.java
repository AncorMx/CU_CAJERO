package modelo;

import modelo.excepcion.SaldoInsuficienteException;

/**
 * Entidad que representa la cuenta bancaria del usuario.
 * Cumple estrictamente con el principio de encapsulamiento (atributos privados).
 */
public class Cuenta {
    private final String numeroCuenta;
    private final String titular;
    private double saldo;

    /**
     * Constructor con valores por defecto para pruebas según especificación:
     * Titular: Juan Pérez, Número: **** 4589, Saldo: $5,000.00
     */
    public Cuenta() {
        this("**** 4589", "Juan Pérez", 5000.00);
    }

    /**
     * Constructor parametrizado.
     *
     * @param numeroCuenta Número de cuenta enmascarado.
     * @param titular Nombre completo del titular.
     * @param saldoInicial Saldo monetario disponible en la cuenta.
     */
    public Cuenta(String numeroCuenta, String titular, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.numeroCuenta = numeroCuenta != null ? numeroCuenta : "**** 4589";
        this.titular = titular != null ? titular : "Juan Pérez";
        this.saldo = saldoInicial;
    }

    /**
     * Verifica si la cuenta tiene fondos suficientes para cubrir el monto total
     * (monto de retiro solicitado + comisiones aplicables).
     *
     * @param montoTotal Monto total requerido para la operación.
     * @return true si el saldo actual es mayor o igual al monto total.
     */
    public synchronized boolean tieneFondosSuficientes(double montoTotal) {
        return this.saldo >= montoTotal;
    }

    /**
     * Descuenta el monto total del saldo de la cuenta.
     *
     * @param montoTotal Monto total a descontar (debe incluir comisiones).
     * @throws SaldoInsuficienteException si el saldo no cubre el monto solicitado.
     */
    public synchronized void descontar(double montoTotal) throws SaldoInsuficienteException {
        if (!tieneFondosSuficientes(montoTotal)) {
            throw new SaldoInsuficienteException(this.saldo, montoTotal);
        }
        this.saldo -= montoTotal;
    }

    /**
     * Permite abonar saldo a la cuenta (útil para pruebas o recargas de simulación).
     *
     * @param monto Monto a depositar.
     */
    public synchronized void abonar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
        }
    }

    /**
     * Restablece el saldo de la cuenta al valor inicial especificado en la prueba ($5,000.00).
     */
    public synchronized void restablecerSaldoPrueba() {
        this.saldo = 5000.00;
    }

    // Getters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Formato extendido con cuatro bloques para la vista del recibo (ej: **** **** **** 4589).
     */
    public String getNumeroCuentaFormateado() {
        if (numeroCuenta.startsWith("**** ****")) {
            return numeroCuenta;
        }
        return "**** **** **** " + numeroCuenta.replaceAll("[^0-9]", "");
    }

    public String getTitular() {
        return titular;
    }

    public synchronized double getSaldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return String.format("Cuenta[%s, Titular: %s, Saldo: $%.2f]", numeroCuenta, titular, saldo);
    }
}
