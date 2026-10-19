package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Recibo;
import vista.ui.PaletaColores;

/**
 * Ventana principal que representa la interfaz del Cajero Automático.
 * Gestiona el intercambio de vistas según el estado del flujo de retiro.
 */
public class VentanaPrincipalATM extends JFrame {

    public static final String CARD_SELECCION = "SELECCION";
    public static final String CARD_PROCESANDO = "PROCESANDO";
    public static final String CARD_RECIBO = "RECIBO";
    public static final String CARD_ERROR = "ERROR";
    public static final String CARD_CANCELADA = "CANCELADA";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenedorCartas;

    // Componentes de vista
    private final VistaListaMontos vistaListaMontos;
    private final VistaEstadoCuenta vistaEstadoCuenta;
    private final VistaProcesando vistaProcesando;
    private final VistaRecibo vistaRecibo;
    private final VistaError vistaError;
    private final VistaCancelada vistaCancelada;

    public VentanaPrincipalATM() {
        super("Cajero Automático");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 560);
        setMinimumSize(new Dimension(860, 520));
        setLocationRelativeTo(null);
        getContentPane().setBackground(PaletaColores.FONDO_APP);

        setLayout(new BorderLayout());

        // Barra superior
        JPanel headerBar = crearHeaderATM();
        add(headerBar, BorderLayout.NORTH);

        // Contenedor de vistas
        panelContenedorCartas = new JPanel(cardLayout);
        panelContenedorCartas.setOpaque(false);
        panelContenedorCartas.setBorder(new EmptyBorder(16, 24, 20, 24));

        vistaListaMontos = new VistaListaMontos();
        vistaEstadoCuenta = new VistaEstadoCuenta();
        vistaProcesando = new VistaProcesando();
        vistaRecibo = new VistaRecibo();
        vistaError = new VistaError();
        vistaCancelada = new VistaCancelada();

        // Panel principal de selección y resumen
        JPanel panelPantallaPrincipal = new JPanel(new GridLayout(1, 2, 20, 0));
        panelPantallaPrincipal.setOpaque(false);
        panelPantallaPrincipal.add(vistaListaMontos);
        panelPantallaPrincipal.add(vistaEstadoCuenta);

        // Registro de tarjetas
        panelContenedorCartas.add(panelPantallaPrincipal, CARD_SELECCION);
        panelContenedorCartas.add(vistaProcesando, CARD_PROCESANDO);
        panelContenedorCartas.add(vistaRecibo, CARD_RECIBO);
        panelContenedorCartas.add(vistaError, CARD_ERROR);
        panelContenedorCartas.add(vistaCancelada, CARD_CANCELADA);

        add(panelContenedorCartas, BorderLayout.CENTER);

        cardLayout.show(panelContenedorCartas, CARD_SELECCION);
    }

    private JPanel crearHeaderATM() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PaletaColores.BORDE_SUAVE),
            new EmptyBorder(12, 24, 12, 24)
        ));

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelIzquierdo.setOpaque(false);

        JLabel lblLogo = new JLabel("ATM BANCO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLogo.setForeground(PaletaColores.AZUL_PRIMARIO);

        JLabel lblSep = new JLabel("|");
        lblSep.setForeground(PaletaColores.TEXTO_MUTED);

        JLabel lblTitulo = new JLabel("Terminal #4589 - Retiro de Efectivo");
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(PaletaColores.TEXTO_TITULO);

        panelIzquierdo.add(lblLogo);
        panelIzquierdo.add(lblSep);
        panelIzquierdo.add(lblTitulo);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecho.setOpaque(false);

        JLabel lblStatus = new JLabel("● En Línea");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(PaletaColores.VERDE_EXITO);
        panelDerecho.add(lblStatus);

        header.add(panelIzquierdo, BorderLayout.WEST);
        header.add(panelDerecho, BorderLayout.EAST);
        return header;
    }

    public void mostrarPantallaSeleccion() {
        vistaProcesando.detenerAnimacion();
        cardLayout.show(panelContenedorCartas, CARD_SELECCION);
    }

    public void mostrarPantallaProcesando() {
        vistaProcesando.iniciarAnimacion();
        cardLayout.show(panelContenedorCartas, CARD_PROCESANDO);
    }

    public void mostrarPantallaRecibo(Recibo recibo) {
        vistaProcesando.detenerAnimacion();
        vistaRecibo.mostrarRecibo(recibo);
        cardLayout.show(panelContenedorCartas, CARD_RECIBO);
    }

    public void mostrarPantallaSaldoInsuficiente(String titular, double saldoActual, double montoSolicitado, double comision, double faltante) {
        vistaProcesando.detenerAnimacion();
        vistaError.mostrarSaldoInsuficiente(titular, saldoActual, montoSolicitado, comision, faltante);
        cardLayout.show(panelContenedorCartas, CARD_ERROR);
    }

    public void mostrarPantallaTransaccionFallida(String codigoError) {
        vistaProcesando.detenerAnimacion();
        vistaError.mostrarTransaccionFallida(codigoError);
        cardLayout.show(panelContenedorCartas, CARD_ERROR);
    }

    public void mostrarPantallaCancelada() {
        vistaProcesando.detenerAnimacion();
        cardLayout.show(panelContenedorCartas, CARD_CANCELADA);
    }

    public VistaListaMontos getVistaListaMontos() {
        return vistaListaMontos;
    }

    public VistaEstadoCuenta getVistaEstadoCuenta() {
        return vistaEstadoCuenta;
    }

    public VistaProcesando getVistaProcesando() {
        return vistaProcesando;
    }

    public VistaRecibo getVistaRecibo() {
        return vistaRecibo;
    }

    public VistaError getVistaError() {
        return vistaError;
    }

    public VistaCancelada getVistaCancelada() {
        return vistaCancelada;
    }
}
