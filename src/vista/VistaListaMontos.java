package vista;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import vista.ui.ComponentesUI.BotonModerno;
import vista.ui.ComponentesUI.BotonMonto;
import vista.ui.ComponentesUI.PanelRedondeado;
import vista.ui.PaletaColores;

/**
 * Vista para la selección de monto de retiro.
 */
public class VistaListaMontos extends JPanel {

    public interface ListenerSeleccionMonto {
        void onMontoSeleccionado(double monto);
        void onSolicitarOtroMonto();
        void onCancelar();
    }

    private final List<BotonMonto> botonesMonto = new ArrayList<>();
    private final JButton btnOtroMonto;
    private final BotonModerno btnCancelar;
    private ListenerSeleccionMonto listener;
    private double montoSeleccionadoActual = 600.00; // Por defecto el del ejemplo

    public VistaListaMontos() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));

        PanelRedondeado tarjetaPrincipal = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        tarjetaPrincipal.setLayout(new BorderLayout(0, 20));
        tarjetaPrincipal.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Título
        JLabel lblTitulo = new JLabel("Seleccione un monto");
        lblTitulo.setFont(PaletaColores.FUENTE_TITULO);
        lblTitulo.setForeground(PaletaColores.TEXTO_TITULO);
        tarjetaPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Cuadrícula de opciones (2 columnas, 3 filas)
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        gridPanel.setOpaque(false);

        double[] montos = {200.00, 400.00, 600.00, 800.00, 1000.00};
        for (double m : montos) {
            String etiqueta = String.format("$%.2f", m);
            BotonMonto btn = new BotonMonto(etiqueta, m);
            btn.addActionListener(e -> {
                marcarSeleccionado(btn.getMonto());
                if (listener != null) {
                    listener.onMontoSeleccionado(btn.getMonto());
                }
            });
            botonesMonto.add(btn);
            gridPanel.add(btn);
        }

        // Botón "Otro monto"
        btnOtroMonto = new BotonMonto("Otro monto", -1);
        btnOtroMonto.addActionListener(e -> {
            limpiarSeleccionesPredefinidas();
            if (listener != null) {
                listener.onSolicitarOtroMonto();
            }
        });
        gridPanel.add(btnOtroMonto);

        tarjetaPrincipal.add(gridPanel, BorderLayout.CENTER);

        // Botón inferior Cancelar
        btnCancelar = new BotonModerno(
            "Cancelar",
            PaletaColores.GRIS_BOTON,
            PaletaColores.GRIS_BOTON_HOVER,
            PaletaColores.GRIS_BOTON_TEXTO,
            8
        );
        btnCancelar.setPreferredSize(new Dimension(100, 44));
        btnCancelar.addActionListener(e -> {
            if (listener != null) {
                listener.onCancelar();
            }
        });
        tarjetaPrincipal.add(btnCancelar, BorderLayout.SOUTH);

        add(tarjetaPrincipal, BorderLayout.CENTER);

        // Selección por defecto
        marcarSeleccionado(600.00);
    }

    public void setListener(ListenerSeleccionMonto listener) {
        this.listener = listener;
    }

    public void marcarSeleccionado(double monto) {
        this.montoSeleccionadoActual = monto;
        boolean coincidePredefinido = false;
        for (BotonMonto btn : botonesMonto) {
            boolean coincide = Math.abs(btn.getMonto() - monto) < 0.01;
            btn.setSeleccionado(coincide);
            if (coincide) {
                coincidePredefinido = true;
            }
        }
        if (btnOtroMonto instanceof BotonMonto) {
            ((BotonMonto) btnOtroMonto).setSeleccionado(!coincidePredefinido);
        }
    }

    public void limpiarSeleccionesPredefinidas() {
        for (BotonMonto btn : botonesMonto) {
            btn.setSeleccionado(false);
        }
        if (btnOtroMonto instanceof BotonMonto) {
            ((BotonMonto) btnOtroMonto).setSeleccionado(true);
        }
    }

    public double getMontoSeleccionadoActual() {
        return montoSeleccionadoActual;
    }
}
