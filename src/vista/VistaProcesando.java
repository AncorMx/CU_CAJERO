package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import vista.ui.ComponentesUI.PanelRedondeado;
import vista.ui.ComponentesUI.SpinnerAnimado;
import vista.ui.PaletaColores;

/**
 * Pantalla intermedia de procesamiento de la transacción.
 */
public class VistaProcesando extends JPanel {
    private final SpinnerAnimado spinner;

    public VistaProcesando() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        PanelRedondeado tarjeta = new PanelRedondeado(20, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(40, 60, 40, 60));
        tarjeta.setPreferredSize(new Dimension(460, 260));

        spinner = new SpinnerAnimado(64);
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Procesando su transacción");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(30, 58, 138)); // Blue 900
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Por favor espere...");
        lblSubtitulo.setFont(PaletaColores.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAdvertencia = new JLabel("No retire su tarjeta");
        lblAdvertencia.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAdvertencia.setForeground(PaletaColores.TEXTO_MUTED);
        lblAdvertencia.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(spinner);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(lblAdvertencia);
        tarjeta.add(Box.createVerticalGlue());

        add(tarjeta, new GridBagConstraints());
    }

    public void iniciarAnimacion() {
        spinner.iniciar();
    }

    public void detenerAnimacion() {
        spinner.detener();
    }
}
