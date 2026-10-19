package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.PrevisualizacionRetiro;
import vista.ui.ComponentesUI.BotonModerno;
import vista.ui.ComponentesUI.PanelRedondeado;
import vista.ui.PaletaColores;

/**
 * Vista para la previsualización del estado de cuenta antes del retiro.
 */
public class VistaEstadoCuenta extends JPanel {

    public interface ListenerConfirmacionRetiro {
        void onConfirmarRetiro();
    }

    private final JLabel lblClienteValor;
    private final JLabel lblSaldoActualValor;
    private final JLabel lblMontoRetirarValor;
    private final JLabel lblComisionEtiqueta;
    private final JLabel lblComisionValor;
    private final JLabel lblMontoFinalValor;
    private final BotonModerno btnRetirarEfectivo;
    private ListenerConfirmacionRetiro listener;

    public VistaEstadoCuenta() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));

        PanelRedondeado tarjetaPrincipal = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        tarjetaPrincipal.setLayout(new BorderLayout(0, 20));
        tarjetaPrincipal.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Título
        JLabel lblTitulo = new JLabel("Estado de Cuenta");
        lblTitulo.setFont(PaletaColores.FUENTE_TITULO);
        lblTitulo.setForeground(PaletaColores.TEXTO_TITULO);
        tarjetaPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de detalles y filas clave-valor
        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setOpaque(false);
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));

        lblClienteValor = new JLabel("Juan Pérez");
        lblClienteValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblClienteValor.setForeground(PaletaColores.TEXTO_TITULO);

        lblSaldoActualValor = new JLabel("$5,000.00");
        lblSaldoActualValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblSaldoActualValor.setForeground(PaletaColores.VERDE_EXITO);

        lblMontoRetirarValor = new JLabel("$600.00");
        lblMontoRetirarValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblMontoRetirarValor.setForeground(PaletaColores.TEXTO_TITULO);

        lblComisionEtiqueta = new JLabel("Comisión (2%):");
        lblComisionEtiqueta.setFont(PaletaColores.FUENTE_CUERPO);
        lblComisionEtiqueta.setForeground(PaletaColores.TEXTO_SECUNDARIO);

        lblComisionValor = new JLabel("-$12.00");
        lblComisionValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblComisionValor.setForeground(PaletaColores.ROJO_TEXTO_COMISION);

        lblMontoFinalValor = new JLabel("$4,388.00");
        lblMontoFinalValor.setFont(PaletaColores.FUENTE_VALOR_DESTACADO);
        lblMontoFinalValor.setForeground(PaletaColores.AZUL_PRIMARIO);

        panelCuerpo.add(crearFila("Cliente:", lblClienteValor));
        panelCuerpo.add(Box.createVerticalStrut(14));
        panelCuerpo.add(crearFila("Saldo Actual:", lblSaldoActualValor));
        panelCuerpo.add(Box.createVerticalStrut(14));
        panelCuerpo.add(crearFila("Monto a retirar:", lblMontoRetirarValor));
        panelCuerpo.add(Box.createVerticalStrut(14));
        panelCuerpo.add(crearFilaDinamica(lblComisionEtiqueta, lblComisionValor));
        panelCuerpo.add(Box.createVerticalStrut(18));

        // Separador sutil
        JSeparator separador = new JSeparator();
        separador.setForeground(PaletaColores.BORDE_SUAVE);
        panelCuerpo.add(separador);
        panelCuerpo.add(Box.createVerticalStrut(18));

        // Monto final estimado
        JLabel lblFinalEtiqueta = new JLabel("Monto final estimado:");
        lblFinalEtiqueta.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblFinalEtiqueta.setForeground(PaletaColores.TEXTO_TITULO);
        panelCuerpo.add(crearFilaDinamica(lblFinalEtiqueta, lblMontoFinalValor));

        tarjetaPrincipal.add(panelCuerpo, BorderLayout.CENTER);

        // Botón verde "Retirar Efectivo" en la parte inferior
        btnRetirarEfectivo = new BotonModerno(
            "Retirar Efectivo",
            PaletaColores.VERDE_EXITO,
            PaletaColores.VERDE_HOVER,
            Color.WHITE,
            8
        );
        btnRetirarEfectivo.setPreferredSize(new Dimension(100, 48));
        btnRetirarEfectivo.addActionListener(e -> {
            if (listener != null) {
                listener.onConfirmarRetiro();
            }
        });
        tarjetaPrincipal.add(btnRetirarEfectivo, BorderLayout.SOUTH);

        add(tarjetaPrincipal, BorderLayout.CENTER);
    }

    public void setListener(ListenerConfirmacionRetiro listener) {
        this.listener = listener;
    }

    /**
     * Actualiza la vista con los datos recalculados por el servicio.
     */
    public void actualizarEstado(PrevisualizacionRetiro prev) {
        if (prev == null) return;
        lblClienteValor.setText(prev.getTitular());
        lblSaldoActualValor.setText(String.format("$%,.2f", prev.getSaldoActual()));
        lblMontoRetirarValor.setText(String.format("$%,.2f", prev.getMontoRetiro()));
        lblComisionEtiqueta.setText(prev.getEtiquetaComision());
        lblComisionValor.setText(String.format("-$%,.2f", prev.getComision()));
        lblMontoFinalValor.setText(String.format("$%,.2f", prev.getSaldoEstimado()));

        // Si no es viable (saldo insuficiente antes de enviar), colorear el saldo estimado
        if (!prev.isEsViable()) {
            lblMontoFinalValor.setForeground(PaletaColores.ROJO_ERROR);
        } else {
            lblMontoFinalValor.setForeground(PaletaColores.AZUL_PRIMARIO);
        }
    }

    public void setBotonHabilitado(boolean habilitado) {
        btnRetirarEfectivo.setEnabled(habilitado);
    }

    private JPanel crearFila(String etiquetaTexto, JLabel componenteValor) {
        JLabel etiqueta = new JLabel(etiquetaTexto);
        etiqueta.setFont(PaletaColores.FUENTE_CUERPO);
        etiqueta.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        return crearFilaDinamica(etiqueta, componenteValor);
    }

    private JPanel crearFilaDinamica(JLabel componenteEtiqueta, JLabel componenteValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.add(componenteEtiqueta, BorderLayout.WEST);
        fila.add(componenteValor, BorderLayout.EAST);
        return fila;
    }
}
