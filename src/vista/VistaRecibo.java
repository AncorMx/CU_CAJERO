package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Recibo;
import vista.ui.ComponentesUI.BotonModerno;
import vista.ui.ComponentesUI.IconoEstadoVisual;
import vista.ui.ComponentesUI.PanelRedondeado;
import vista.ui.PaletaColores;

/**
 * Vista para el resultado de operación exitosa y visualización del recibo.
 */
public class VistaRecibo extends JPanel {

    public interface ListenerAccionesRecibo {
        void onVolverInicio();
        void onImprimirOEnviar(Recibo recibo);
    }

    private final JLabel lblFechaHora;
    private final JLabel lblCuentaValor;
    private final JLabel lblClienteValor;
    private final JLabel lblConceptoValor;
    private final JLabel lblMontoValor;
    private final JLabel lblComisionValor;
    private final JLabel lblTotalValor;
    private Recibo reciboActual;
    private ListenerAccionesRecibo listener;

    public VistaRecibo() {
        setOpaque(false);
        setLayout(new GridLayout(1, 2, 20, 0));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        // ================= PANEL IZQUIERDO: MENSAJE DE ÉXITO =================
        PanelRedondeado panelIzquierdo = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(new EmptyBorder(30, 24, 30, 24));

        IconoEstadoVisual iconoExito = new IconoEstadoVisual(IconoEstadoVisual.TipoIcono.EXITO, 54);
        iconoExito.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTituloExito = new JLabel("¡Operación Exitosa!");
        lblTituloExito.setFont(PaletaColores.FUENTE_TITULO_GRANDE);
        lblTituloExito.setForeground(PaletaColores.VERDE_EXITO);
        lblTituloExito.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInstruccion = new JLabel("Por favor, retire su efectivo y su tarjeta.");
        lblInstruccion.setFont(PaletaColores.FUENTE_SUBTITULO);
        lblInstruccion.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonModerno btnPantallaPrincipal = new BotonModerno(
            "Pantalla Principal",
            PaletaColores.AZUL_PRIMARIO,
            PaletaColores.AZUL_HOVER,
            Color.WHITE,
            8
        );
        btnPantallaPrincipal.setMaximumSize(new Dimension(200, 42));
        btnPantallaPrincipal.setPreferredSize(new Dimension(180, 42));
        btnPantallaPrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPantallaPrincipal.addActionListener(e -> {
            if (listener != null) {
                listener.onVolverInicio();
            }
        });

        panelIzquierdo.add(Box.createVerticalGlue());
        panelIzquierdo.add(iconoExito);
        panelIzquierdo.add(Box.createVerticalStrut(18));
        panelIzquierdo.add(lblTituloExito);
        panelIzquierdo.add(Box.createVerticalStrut(8));
        panelIzquierdo.add(lblInstruccion);
        panelIzquierdo.add(Box.createVerticalStrut(24));
        panelIzquierdo.add(btnPantallaPrincipal);
        panelIzquierdo.add(Box.createVerticalGlue());

        // ================= PANEL DERECHO: TICKET DE RECIBO =================
        PanelRedondeado panelDerecho = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        panelDerecho.setLayout(new BorderLayout(0, 16));
        panelDerecho.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel lblTituloRecibo = new JLabel("Recibo de Retiro");
        lblTituloRecibo.setFont(PaletaColores.FUENTE_TITULO);
        lblTituloRecibo.setForeground(PaletaColores.TEXTO_TITULO);
        panelDerecho.add(lblTituloRecibo, BorderLayout.NORTH);

        // Subtarjeta con estilo de ticket físico
        PanelRedondeado tarjetaTicket = new PanelRedondeado(12, new Color(248, 250, 252), PaletaColores.BORDE_SUAVE, 1);
        tarjetaTicket.setLayout(new BoxLayout(tarjetaTicket, BoxLayout.Y_AXIS));
        tarjetaTicket.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel lblEncabezadoAtm = new JLabel("CAJERO AUTOMÁTICO");
        lblEncabezadoAtm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEncabezadoAtm.setForeground(PaletaColores.TEXTO_TITULO);
        lblEncabezadoAtm.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblFechaHora = new JLabel("Fecha: 24/05/2024 10:30 AM");
        lblFechaHora.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFechaHora.setForeground(PaletaColores.TEXTO_MUTED);
        lblFechaHora.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjetaTicket.add(lblEncabezadoAtm);
        tarjetaTicket.add(Box.createVerticalStrut(4));
        tarjetaTicket.add(lblFechaHora);
        tarjetaTicket.add(Box.createVerticalStrut(12));

        lblCuentaValor = new JLabel("**** **** **** 4589");
        lblCuentaValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);

        lblClienteValor = new JLabel("Juan Pérez");
        lblClienteValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);

        lblConceptoValor = new JLabel("Retiro Efectivo");
        lblConceptoValor.setFont(PaletaColores.FUENTE_CUERPO);

        lblMontoValor = new JLabel("$600.00");
        lblMontoValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);

        lblComisionValor = new JLabel("$12.00");
        lblComisionValor.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblComisionValor.setForeground(PaletaColores.ROJO_TEXTO_COMISION);

        lblTotalValor = new JLabel("$612.00");
        lblTotalValor.setFont(PaletaColores.FUENTE_VALOR_DESTACADO);
        lblTotalValor.setForeground(PaletaColores.AZUL_PRIMARIO);

        tarjetaTicket.add(crearFilaTicket("Cuenta:", lblCuentaValor));
        tarjetaTicket.add(Box.createVerticalStrut(8));
        tarjetaTicket.add(crearFilaTicket("Cliente:", lblClienteValor));
        tarjetaTicket.add(Box.createVerticalStrut(8));
        tarjetaTicket.add(crearFilaTicket("Concepto:", lblConceptoValor));
        tarjetaTicket.add(Box.createVerticalStrut(8));
        tarjetaTicket.add(crearFilaTicket("Monto:", lblMontoValor));
        tarjetaTicket.add(Box.createVerticalStrut(8));
        tarjetaTicket.add(crearFilaTicket("Comisión:", lblComisionValor));
        tarjetaTicket.add(Box.createVerticalStrut(10));

        JSeparator sep = new JSeparator();
        sep.setForeground(PaletaColores.BORDE_SUAVE);
        tarjetaTicket.add(sep);
        tarjetaTicket.add(Box.createVerticalStrut(10));

        JLabel lblTotalEtiqueta = new JLabel("TOTAL:");
        lblTotalEtiqueta.setFont(PaletaColores.FUENTE_VALOR_DESTACADO);
        lblTotalEtiqueta.setForeground(PaletaColores.AZUL_PRIMARIO);
        tarjetaTicket.add(crearFilaTicketCustom(lblTotalEtiqueta, lblTotalValor));

        panelDerecho.add(tarjetaTicket, BorderLayout.CENTER);

        BotonModerno btnImprimir = new BotonModerno(
            "Imprimir / Enviar por correo",
            PaletaColores.GRIS_BOTON,
            PaletaColores.GRIS_BOTON_HOVER,
            PaletaColores.GRIS_BOTON_TEXTO,
            8
        );
        btnImprimir.setPreferredSize(new Dimension(100, 42));
        btnImprimir.addActionListener(e -> {
            if (listener != null && reciboActual != null) {
                listener.onImprimirOEnviar(reciboActual);
            }
        });
        panelDerecho.add(btnImprimir, BorderLayout.SOUTH);

        add(panelIzquierdo);
        add(panelDerecho);
    }

    public void setListener(ListenerAccionesRecibo listener) {
        this.listener = listener;
    }

    public void mostrarRecibo(Recibo recibo) {
        this.reciboActual = recibo;
        if (recibo == null) return;

        lblFechaHora.setText("Fecha: " + recibo.getFechaHoraFormateada());
        lblCuentaValor.setText(recibo.getNumeroCuenta());
        lblClienteValor.setText(recibo.getTitular());
        lblConceptoValor.setText(recibo.getConcepto());
        lblMontoValor.setText(String.format("$%,.2f", recibo.getMontoRetiro()));
        lblComisionValor.setText(String.format("$%,.2f", recibo.getComision()));
        lblTotalValor.setText(String.format("$%,.2f", recibo.getTotalDebitado()));
    }

    private JPanel crearFilaTicket(String etiqueta, JLabel valor) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(PaletaColores.FUENTE_CUERPO);
        lbl.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        return crearFilaTicketCustom(lbl, valor);
    }

    private JPanel crearFilaTicketCustom(JLabel etiqueta, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(etiqueta, BorderLayout.WEST);
        p.add(valor, BorderLayout.EAST);
        return p;
    }
}
