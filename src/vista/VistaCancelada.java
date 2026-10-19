package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import vista.ui.ComponentesUI.BotonModerno;
import vista.ui.ComponentesUI.IconoEstadoVisual;
import vista.ui.ComponentesUI.PanelRedondeado;
import vista.ui.PaletaColores;

/**
 * Vista para informar la cancelación de una transacción.
 */
public class VistaCancelada extends JPanel {

    public interface ListenerCancelada {
        void onVolverPantallaPrincipal();
    }

    private ListenerCancelada listener;

    public VistaCancelada() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        PanelRedondeado tarjeta = new PanelRedondeado(20, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(40, 50, 40, 50));
        tarjeta.setPreferredSize(new Dimension(500, 310));

        IconoEstadoVisual iconoCancelado = new IconoEstadoVisual(IconoEstadoVisual.TipoIcono.CANCELADO, 56);
        iconoCancelado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Transacción Cancelada");
        lblTitulo.setFont(PaletaColores.FUENTE_TITULO_GRANDE);
        lblTitulo.setForeground(PaletaColores.TEXTO_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMensaje = new JLabel("Su operación ha sido cancelada exitosamente.");
        lblMensaje.setFont(PaletaColores.FUENTE_SUBTITULO);
        lblMensaje.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAvisoCargo = new JLabel("No se ha realizado ningún cargo a su cuenta.");
        lblAvisoCargo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAvisoCargo.setForeground(PaletaColores.TEXTO_MUTED);
        lblAvisoCargo.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonModerno btnVolver = new BotonModerno(
            "Volver a Pantalla Principal",
            PaletaColores.AZUL_PRIMARIO,
            PaletaColores.AZUL_HOVER,
            Color.WHITE,
            8
        );
        btnVolver.setMaximumSize(new Dimension(230, 44));
        btnVolver.setPreferredSize(new Dimension(230, 44));
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> {
            if (listener != null) {
                listener.onVolverPantallaPrincipal();
            }
        });

        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(iconoCancelado);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblMensaje);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblAvisoCargo);
        tarjeta.add(Box.createVerticalStrut(26));
        tarjeta.add(btnVolver);
        tarjeta.add(Box.createVerticalGlue());

        add(tarjeta, new GridBagConstraints());
    }

    public void setListener(ListenerCancelada listener) {
        this.listener = listener;
    }
}
