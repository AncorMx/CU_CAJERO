package controlador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import modelo.Cuenta;
import modelo.PrevisualizacionRetiro;
import modelo.Recibo;
import modelo.ServicioRetiro;
import modelo.excepcion.CajeroException;
import modelo.excepcion.EfectivoCajeroInsuficienteException;
import modelo.excepcion.MontoInvalidoException;
import modelo.excepcion.SaldoInsuficienteException;
import vista.VentanaPrincipalATM;
import vista.VistaCancelada;
import vista.VistaError;
import vista.VistaEstadoCuenta;
import vista.VistaListaMontos;
import vista.VistaRecibo;

/**
 * Controlador principal para el caso de uso "Retirar Efectivo".
 * Gestiona el flujo entre las vistas y la capa de servicios.
 */
public class ControladorRetiro implements
        VistaListaMontos.ListenerSeleccionMonto,
        VistaEstadoCuenta.ListenerConfirmacionRetiro,
        VistaRecibo.ListenerAccionesRecibo,
        VistaError.ListenerAccionesError,
        VistaCancelada.ListenerCancelada {

    private final Cuenta cuenta;
    private final ServicioRetiro servicioRetiro;
    private VentanaPrincipalATM vistaPrincipal;

    private double montoSeleccionado = 600.00;

    public ControladorRetiro(Cuenta cuenta, ServicioRetiro servicioRetiro) {
        this.cuenta = cuenta != null ? cuenta : new Cuenta();
        this.servicioRetiro = servicioRetiro != null ? servicioRetiro : new ServicioRetiro();
    }

    public void registrarVista(VentanaPrincipalATM vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;

        this.vistaPrincipal.getVistaListaMontos().setListener(this);
        this.vistaPrincipal.getVistaEstadoCuenta().setListener(this);
        this.vistaPrincipal.getVistaRecibo().setListener(this);
        this.vistaPrincipal.getVistaError().setListener(this);
        this.vistaPrincipal.getVistaCancelada().setListener(this);

        actualizarPrevisualizacion(this.montoSeleccionado);
    }

    public void actualizarPrevisualizacion(double monto) {
        this.montoSeleccionado = monto;
        PrevisualizacionRetiro prev = servicioRetiro.previsualizarRetiro(cuenta, monto);
        if (vistaPrincipal != null) {
            vistaPrincipal.getVistaEstadoCuenta().actualizarEstado(prev);
            vistaPrincipal.getVistaListaMontos().marcarSeleccionado(monto);
        }
    }

    @Override
    public void onMontoSeleccionado(double monto) {
        actualizarPrevisualizacion(monto);
    }

    @Override
    public void onSolicitarOtroMonto() {
        String input = JOptionPane.showInputDialog(
            vistaPrincipal,
            "Ingrese el monto a retirar en múltiplos de $200.00:\n(Ejemplos: 200, 400, 600, 800, 1000, 1200...)",
            "Otro Monto",
            JOptionPane.PLAIN_MESSAGE
        );

        if (input != null && !input.trim().isEmpty()) {
            try {
                double monto = Double.parseDouble(input.trim().replace("$", "").replace(",", ""));
                servicioRetiro.validarMonto(monto);
                actualizarPrevisualizacion(monto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    vistaPrincipal,
                    "Por favor ingrese una cantidad numérica válida.",
                    "Entrada inválida",
                    JOptionPane.WARNING_MESSAGE
                );
                actualizarPrevisualizacion(montoSeleccionado);
            } catch (MontoInvalidoException ex) {
                JOptionPane.showMessageDialog(
                    vistaPrincipal,
                    ex.getMessage(),
                    "Monto no permitido",
                    JOptionPane.WARNING_MESSAGE
                );
                actualizarPrevisualizacion(montoSeleccionado);
            }
        } else {
            actualizarPrevisualizacion(montoSeleccionado);
        }
    }

    @Override
    public void onConfirmarRetiro() {
        vistaPrincipal.mostrarPantallaProcesando();

        Timer timerProcesamiento = new Timer(1200, e -> {
            ((Timer) e.getSource()).stop();
            ejecutarTransaccionFinal();
        });
        timerProcesamiento.setRepeats(false);
        timerProcesamiento.start();
    }

    private void ejecutarTransaccionFinal() {
        try {
            Recibo recibo = servicioRetiro.ejecutarRetiro(cuenta, montoSeleccionado);
            vistaPrincipal.mostrarPantallaRecibo(recibo);

        } catch (SaldoInsuficienteException ex) {
            double comision = servicioRetiro.getPoliticaComision().calcularComision(montoSeleccionado);
            vistaPrincipal.mostrarPantallaSaldoInsuficiente(
                cuenta.getTitular(),
                ex.getSaldoDisponible(),
                montoSeleccionado,
                comision,
                ex.getMontoFaltante()
            );

        } catch (EfectivoCajeroInsuficienteException ex) {
            vistaPrincipal.mostrarPantallaTransaccionFallida(ex.getCodigoError());

        } catch (CajeroException ex) {
            vistaPrincipal.mostrarPantallaTransaccionFallida(ex.getCodigoError());
        }
    }

    @Override
    public void onCancelar() {
        servicioRetiro.registrarCancelacion(cuenta, montoSeleccionado);
        vistaPrincipal.mostrarPantallaCancelada();
    }

    @Override
    public void onVolverInicio() {
        volverAPantallaPrincipal();
    }

    @Override
    public void onIntentarOtroMonto() {
        volverAPantallaPrincipal();
    }

    @Override
    public void onReintentar() {
        volverAPantallaPrincipal();
    }

    @Override
    public void onCancelarYSalir() {
        servicioRetiro.registrarCancelacion(cuenta, montoSeleccionado);
        vistaPrincipal.mostrarPantallaCancelada();
    }

    @Override
    public void onVolverPantallaPrincipal() {
        volverAPantallaPrincipal();
    }

    @Override
    public void onImprimirOEnviar(Recibo recibo) {
        if (recibo == null) return;

        String nombreArchivo = "Recibo_" + recibo.getFolio() + ".txt";
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write(recibo.generarTextoTicket());
        } catch (IOException ignored) {}

        JOptionPane.showMessageDialog(
            vistaPrincipal,
            "Comprobante emitido correctamente.\n\n" + recibo.generarTextoTicket() +
            "\n(Guardado localmente en: " + new File(nombreArchivo).getName() + ")",
            "Comprobante de Retiro",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void volverAPantallaPrincipal() {
        this.montoSeleccionado = 600.00;
        actualizarPrevisualizacion(montoSeleccionado);
        vistaPrincipal.mostrarPantallaSeleccion();
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public ServicioRetiro getServicioRetiro() {
        return servicioRetiro;
    }

    public double getMontoSeleccionado() {
        return montoSeleccionado;
    }
}
