package modelo;

import modelo.excepcion.EfectivoCajeroInsuficienteException;

/**
 * Controla el efectivo físico disponible en los dispensadores del cajero automático.
 */
public class InventarioEfectivoCajero {
    private double efectivoDisponible;
    private boolean forzarFallaDispensador = false;

    /**
     * Constructor por defecto con inventario inicial suficiente ($50,000.00).
     */
    public InventarioEfectivoCajero() {
        this(50000.00);
    }

    /**
     * Constructor con efectivo inicial especificado.
     *
     * @param efectivoInicial Monto total en billetes cargado en el cajero.
     */
    public InventarioEfectivoCajero(double efectivoInicial) {
        this.efectivoDisponible = efectivoInicial;
    }

    /**
     * Verifica si el cajero dispone de billetes suficientes para entregar el monto solicitado.
     *
     * @param monto Monto solicitado por el cliente.
     * @return true si hay efectivo físico disponible y los dispensadores funcionan correctamente.
     */
    public synchronized boolean tieneEfectivoDisponible(double monto) {
        if (forzarFallaDispensador) {
            return false;
        }
        return this.efectivoDisponible >= monto;
    }

    /**
     * Simula la entrega física de billetes a través del dispensador.
     *
     * @param monto Monto a entregar físicamente.
     * @throws EfectivoCajeroInsuficienteException si no hay billetes suficientes o el dispensador falla.
     */
    public synchronized void dispensarEfectivo(double monto) throws EfectivoCajeroInsuficienteException {
        if (!tieneEfectivoDisponible(monto)) {
            throw new EfectivoCajeroInsuficienteException(this.efectivoDisponible, monto);
        }
        this.efectivoDisponible -= monto;
    }

    /**
     * Permite reabastecer el cajero con nuevo efectivo.
     *
     * @param monto Monto a sumar al cajero.
     */
    public synchronized void reabastecer(double monto) {
        if (monto > 0) {
            this.efectivoDisponible += monto;
        }
    }

    public synchronized double getEfectivoDisponible() {
        return efectivoDisponible;
    }

    public synchronized void setEfectivoDisponible(double monto) {
        this.efectivoDisponible = Math.max(0, monto);
    }

    public synchronized boolean isForzarFallaDispensador() {
        return forzarFallaDispensador;
    }

    public synchronized void setForzarFallaDispensador(boolean forzarFalla) {
        this.forzarFallaDispensador = forzarFalla;
    }

    @Override
    public String toString() {
        return String.format("InventarioEfectivoCajero[Disponible: $%.2f, FallaForzada: %s]",
            efectivoDisponible, forzarFallaDispensador);
    }
}
