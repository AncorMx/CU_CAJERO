package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import vista.ui.ComponentesUI.BotonModerno;
import vista.ui.ComponentesUI.IconoEstadoVisual;
import vista.ui.ComponentesUI.PanelRedondeado;
import vista.ui.PaletaColores;

/**
 * Vista para presentar errores de transacción, específicamente:
 * 1. Saldo Insuficiente (con desglose de fondos faltantes).
 * 2. Transacción Fallida / Error técnico ATM-502 (con información de causas del cajero/servidor).
 */
public class VistaError extends JPanel {

    public interface ListenerAccionesError {
        void onIntentarOtroMonto();
        void onVolverInicio();
        void onReintentar();
        void onCancelarYSalir();
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenedor;
    private ListenerAccionesError listener;

    // Componentes para Saldo Insuficiente
    private final JLabel lblSaldoDisponibleIzq;
    private final JLabel lblClienteSaldo;
    private final JLabel lblSaldoActualSaldo;
    private final JLabel lblMontoSolicitadoSaldo;
    private final JLabel lblComisionSaldo;
    private final JLabel lblFaltanPill;

    // Componentes para Transacción Fallida
    private final JLabel lblCodigoError;

    public VistaError() {
        setOpaque(false);
        setLayout(new BorderLayout());

        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setOpaque(false);

        // ================= TARJETA 1: SALDO INSUFICIENTE =================
        JPanel panelSaldoInsuficiente = new JPanel(new GridLayout(1, 2, 20, 0));
        panelSaldoInsuficiente.setOpaque(false);
        panelSaldoInsuficiente.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Panel Izquierdo Saldo
        PanelRedondeado izqSaldo = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        izqSaldo.setLayout(new BoxLayout(izqSaldo, BoxLayout.Y_AXIS));
        izqSaldo.setBorder(new EmptyBorder(30, 24, 30, 24));

        IconoEstadoVisual iconoCruzRoja = new IconoEstadoVisual(IconoEstadoVisual.TipoIcono.ERROR_SALDO, 54);
        iconoCruzRoja.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTituloSaldo = new JLabel("Saldo Insuficiente");
        lblTituloSaldo.setFont(PaletaColores.FUENTE_TITULO_GRANDE);
        lblTituloSaldo.setForeground(PaletaColores.ROJO_ERROR);
        lblTituloSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtituloSaldo = new JLabel("No cuenta con fondos suficientes para realizar esta transacción.");
        lblSubtituloSaldo.setFont(PaletaColores.FUENTE_CUERPO);
        lblSubtituloSaldo.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lblSubtituloSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSaldoDisponibleIzq = new JLabel("Saldo disponible: $500.00");
        lblSaldoDisponibleIzq.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSaldoDisponibleIzq.setForeground(PaletaColores.TEXTO_MUTED);
        lblSaldoDisponibleIzq.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonModerno btnIntentarOtro = new BotonModerno(
            "Intentar otro monto",
            PaletaColores.AZUL_PRIMARIO,
            PaletaColores.AZUL_HOVER,
            Color.WHITE,
            8
        );
        btnIntentarOtro.setMaximumSize(new Dimension(190, 42));
        btnIntentarOtro.setPreferredSize(new Dimension(190, 42));
        btnIntentarOtro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIntentarOtro.addActionListener(e -> {
            if (listener != null) listener.onIntentarOtroMonto();
        });

        izqSaldo.add(Box.createVerticalGlue());
        izqSaldo.add(iconoCruzRoja);
        izqSaldo.add(Box.createVerticalStrut(18));
        izqSaldo.add(lblTituloSaldo);
        izqSaldo.add(Box.createVerticalStrut(8));
        izqSaldo.add(lblSubtituloSaldo);
        izqSaldo.add(Box.createVerticalStrut(12));
        izqSaldo.add(lblSaldoDisponibleIzq);
        izqSaldo.add(Box.createVerticalStrut(24));
        izqSaldo.add(btnIntentarOtro);
        izqSaldo.add(Box.createVerticalGlue());

        // Panel Derecho Saldo (Estado de cuenta)
        PanelRedondeado derSaldo = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        derSaldo.setLayout(new BorderLayout(0, 16));
        derSaldo.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel lblTituloDerSaldo = new JLabel("Estado de Cuenta");
        lblTituloDerSaldo.setFont(PaletaColores.FUENTE_TITULO);
        lblTituloDerSaldo.setForeground(PaletaColores.TEXTO_TITULO);
        derSaldo.add(lblTituloDerSaldo, BorderLayout.NORTH);

        JPanel panelDatosSaldo = new JPanel();
        panelDatosSaldo.setOpaque(false);
        panelDatosSaldo.setLayout(new BoxLayout(panelDatosSaldo, BoxLayout.Y_AXIS));

        lblClienteSaldo = new JLabel("Juan Pérez");
        lblClienteSaldo.setFont(PaletaColores.FUENTE_CUERPO_BOLD);

        lblSaldoActualSaldo = new JLabel("$500.00");
        lblSaldoActualSaldo.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblSaldoActualSaldo.setForeground(PaletaColores.ROJO_ERROR);

        lblMontoSolicitadoSaldo = new JLabel("$1,000.00");
        lblMontoSolicitadoSaldo.setFont(PaletaColores.FUENTE_CUERPO_BOLD);

        lblComisionSaldo = new JLabel("-$20.00");
        lblComisionSaldo.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblComisionSaldo.setForeground(PaletaColores.ROJO_TEXTO_COMISION);

        panelDatosSaldo.add(crearFila("Cliente:", lblClienteSaldo));
        panelDatosSaldo.add(Box.createVerticalStrut(14));
        panelDatosSaldo.add(crearFila("Saldo Actual:", lblSaldoActualSaldo));
        panelDatosSaldo.add(Box.createVerticalStrut(14));
        panelDatosSaldo.add(crearFila("Monto solicitado:", lblMontoSolicitadoSaldo));
        panelDatosSaldo.add(Box.createVerticalStrut(14));
        panelDatosSaldo.add(crearFila("Comisión (2%):", lblComisionSaldo));
        panelDatosSaldo.add(Box.createVerticalStrut(24));

        // Pill / Badge destacado rojo "Faltan: $520.00"
        PanelRedondeado pillFaltan = new PanelRedondeado(8, PaletaColores.ROJO_FONDO_SUAVE, new Color(254, 202, 202), 1);
        pillFaltan.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pillFaltan.setMaximumSize(new Dimension(320, 44));
        lblFaltanPill = new JLabel("Faltan: $520.00");
        lblFaltanPill.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFaltanPill.setForeground(PaletaColores.ROJO_ERROR);
        pillFaltan.add(lblFaltanPill);
        panelDatosSaldo.add(pillFaltan);

        derSaldo.add(panelDatosSaldo, BorderLayout.CENTER);

        BotonModerno btnVolverInicio = new BotonModerno(
            "Volver al inicio",
            PaletaColores.GRIS_BOTON,
            PaletaColores.GRIS_BOTON_HOVER,
            PaletaColores.GRIS_BOTON_TEXTO,
            8
        );
        btnVolverInicio.setPreferredSize(new Dimension(100, 42));
        btnVolverInicio.addActionListener(e -> {
            if (listener != null) listener.onVolverInicio();
        });
        derSaldo.add(btnVolverInicio, BorderLayout.SOUTH);

        panelSaldoInsuficiente.add(izqSaldo);
        panelSaldoInsuficiente.add(derSaldo);

        // ================= TARJETA 2: TRANSACCIÓN FALLIDA (ATM-502) =================
        JPanel panelTransaccionFallida = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTransaccionFallida.setOpaque(false);
        panelTransaccionFallida.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Izquierda Fallida
        PanelRedondeado izqFallida = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        izqFallida.setLayout(new BoxLayout(izqFallida, BoxLayout.Y_AXIS));
        izqFallida.setBorder(new EmptyBorder(30, 24, 30, 24));

        IconoEstadoVisual iconoAlerta = new IconoEstadoVisual(IconoEstadoVisual.TipoIcono.ERROR_SISTEMA, 54);
        iconoAlerta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTituloFallida = new JLabel("Transacción Fallida");
        lblTituloFallida.setFont(PaletaColores.FUENTE_TITULO_GRANDE);
        lblTituloFallida.setForeground(new Color(180, 83, 9)); // Amber 700
        lblTituloFallida.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtituloFallida = new JLabel("Ha ocurrido un error al procesar su solicitud.");
        lblSubtituloFallida.setFont(PaletaColores.FUENTE_CUERPO);
        lblSubtituloFallida.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lblSubtituloFallida.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblCodigoError = new JLabel("Código de error: ATM-502");
        lblCodigoError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCodigoError.setForeground(PaletaColores.TEXTO_MUTED);
        lblCodigoError.setAlignmentX(Component.CENTER_ALIGNMENT);

        BotonModerno btnReintentar = new BotonModerno(
            "Reintentar",
            PaletaColores.AZUL_PRIMARIO,
            PaletaColores.AZUL_HOVER,
            Color.WHITE,
            8
        );
        btnReintentar.setMaximumSize(new Dimension(160, 42));
        btnReintentar.setPreferredSize(new Dimension(160, 42));
        btnReintentar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReintentar.addActionListener(e -> {
            if (listener != null) listener.onReintentar();
        });

        izqFallida.add(Box.createVerticalGlue());
        izqFallida.add(iconoAlerta);
        izqFallida.add(Box.createVerticalStrut(18));
        izqFallida.add(lblTituloFallida);
        izqFallida.add(Box.createVerticalStrut(8));
        izqFallida.add(lblSubtituloFallida);
        izqFallida.add(Box.createVerticalStrut(10));
        izqFallida.add(lblCodigoError);
        izqFallida.add(Box.createVerticalStrut(24));
        izqFallida.add(btnReintentar);
        izqFallida.add(Box.createVerticalGlue());

        // Derecha Fallida (Información de causas)
        PanelRedondeado derFallida = new PanelRedondeado(16, PaletaColores.FONDO_BLANCO, PaletaColores.BORDE_SUAVE, 1);
        derFallida.setLayout(new BorderLayout(0, 16));
        derFallida.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel lblTituloInfo = new JLabel("Información");
        lblTituloInfo.setFont(PaletaColores.FUENTE_TITULO);
        lblTituloInfo.setForeground(PaletaColores.TEXTO_TITULO);
        derFallida.add(lblTituloInfo, BorderLayout.NORTH);

        JPanel panelCuerpoInfo = new JPanel();
        panelCuerpoInfo.setOpaque(false);
        panelCuerpoInfo.setLayout(new BoxLayout(panelCuerpoInfo, BoxLayout.Y_AXIS));

        JLabel lblCausas = new JLabel("Posibles causas:");
        lblCausas.setFont(PaletaColores.FUENTE_CUERPO_BOLD);
        lblCausas.setForeground(PaletaColores.TEXTO_TITULO);

        panelCuerpoInfo.add(lblCausas);
        panelCuerpoInfo.add(Box.createVerticalStrut(10));
        panelCuerpoInfo.add(crearItemCausa("• Problema de conexión con el servidor"));
        panelCuerpoInfo.add(Box.createVerticalStrut(8));
        panelCuerpoInfo.add(crearItemCausa("• Cajero sin efectivo disponible"));
        panelCuerpoInfo.add(Box.createVerticalStrut(8));
        panelCuerpoInfo.add(crearItemCausa("• Tarjeta bloqueada temporalmente"));
        panelCuerpoInfo.add(Box.createVerticalStrut(24));

        PanelRedondeado boxSoporte = new PanelRedondeado(8, PaletaColores.AZUL_FONDO_SELECCION, new Color(191, 219, 254), 1);
        boxSoporte.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel lblContactoBanco = new JLabel("Si el problema persiste, contacte a su banco.");
        lblContactoBanco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblContactoBanco.setForeground(PaletaColores.AZUL_PRIMARIO);
        boxSoporte.add(lblContactoBanco);
        panelCuerpoInfo.add(boxSoporte);

        derFallida.add(panelCuerpoInfo, BorderLayout.CENTER);

        BotonModerno btnCancelarSalir = new BotonModerno(
            "Cancelar y salir",
            PaletaColores.GRIS_BOTON,
            PaletaColores.GRIS_BOTON_HOVER,
            PaletaColores.GRIS_BOTON_TEXTO,
            8
        );
        btnCancelarSalir.setPreferredSize(new Dimension(100, 42));
        btnCancelarSalir.addActionListener(e -> {
            if (listener != null) listener.onCancelarYSalir();
        });
        derFallida.add(btnCancelarSalir, BorderLayout.SOUTH);

        panelTransaccionFallida.add(izqFallida);
        panelTransaccionFallida.add(derFallida);

        // Añadir vistas al contenedor de cartas
        panelContenedor.add(panelSaldoInsuficiente, "SALDO_INSUFICIENTE");
        panelContenedor.add(panelTransaccionFallida, "TRANSACCION_FALLIDA");

        add(panelContenedor, BorderLayout.CENTER);
    }

    public void setListener(ListenerAccionesError listener) {
        this.listener = listener;
    }

    /**
     * Configura y muestra el modo Saldo Insuficiente con los datos de la cuenta.
     */
    public void mostrarSaldoInsuficiente(String titular, double saldoActual, double montoSolicitado, double comision, double faltante) {
        lblClienteSaldo.setText(titular);
        lblSaldoActualSaldo.setText(String.format("$%,.2f", saldoActual));
        lblSaldoDisponibleIzq.setText(String.format("Saldo disponible: $%,.2f", saldoActual));
        lblMontoSolicitadoSaldo.setText(String.format("$%,.2f", montoSolicitado));
        lblComisionSaldo.setText(String.format("-$%,.2f", comision));
        lblFaltanPill.setText(String.format("Faltan: $%,.2f", faltante));

        cardLayout.show(panelContenedor, "SALDO_INSUFICIENTE");
    }

    /**
     * Configura y muestra el modo Transacción Fallida (ATM-502 u otro código).
     */
    public void mostrarTransaccionFallida(String codigoError) {
        lblCodigoError.setText("Código de error: " + (codigoError != null ? codigoError : "ATM-502"));
        cardLayout.show(panelContenedor, "TRANSACCION_FALLIDA");
    }

    private JPanel crearFila(String etiquetaTexto, JLabel componenteValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        JLabel etiqueta = new JLabel(etiquetaTexto);
        etiqueta.setFont(PaletaColores.FUENTE_CUERPO);
        etiqueta.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        fila.add(etiqueta, BorderLayout.WEST);
        fila.add(componenteValor, BorderLayout.EAST);
        return fila;
    }

    private JLabel crearItemCausa(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(PaletaColores.FUENTE_CUERPO);
        lbl.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        return lbl;
    }
}
