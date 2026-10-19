package vista.ui;

import java.awt.Color;
import java.awt.Font;

/**
 * Paleta de colores y constantes tipográficas para la interfaz.
 */
public final class PaletaColores {
    private PaletaColores() {}

    // Fondos generales
    public static final Color FONDO_APP = new Color(241, 245, 249); // Slate 100
    public static final Color FONDO_BLANCO = new Color(255, 255, 255);
    public static final Color BORDE_SUAVE = new Color(226, 232, 240); // Slate 200
    public static final Color BORDE_ACTIVO = new Color(37, 99, 235); // Blue 600

    // Colores de Texto
    public static final Color TEXTO_TITULO = new Color(15, 23, 42); // Slate 900
    public static final Color TEXTO_SUBTITULO = new Color(71, 85, 105); // Slate 600
    public static final Color TEXTO_SECUNDARIO = new Color(100, 116, 139); // Slate 500
    public static final Color TEXTO_MUTED = new Color(148, 163, 184); // Slate 400

    // Colores de Marca y Estados
    public static final Color AZUL_PRIMARIO = new Color(37, 99, 235); // #2563EB
    public static final Color AZUL_HOVER = new Color(29, 78, 216); // #1D4ED8
    public static final Color AZUL_FONDO_SELECCION = new Color(239, 246, 255); // #EFF6FF

    public static final Color VERDE_EXITO = new Color(22, 163, 74); // #16A34A
    public static final Color VERDE_HOVER = new Color(21, 128, 61); // #15803D
    public static final Color VERDE_FONDO_SUAVE = new Color(240, 253, 244);

    public static final Color ROJO_ERROR = new Color(220, 38, 38); // #DC2626
    public static final Color ROJO_FONDO_SUAVE = new Color(254, 242, 242);
    public static final Color ROJO_TEXTO_COMISION = new Color(239, 68, 68);

    public static final Color AMBAR_ALERTA = new Color(217, 119, 6); // #D97706
    public static final Color AMBAR_FONDO_SUAVE = new Color(255, 251, 235);

    public static final Color GRIS_BOTON = new Color(226, 232, 240);
    public static final Color GRIS_BOTON_HOVER = new Color(203, 213, 225);
    public static final Color GRIS_BOTON_TEXTO = new Color(51, 65, 85);

    // Tipografías
    public static final Font FUENTE_TITULO_GRANDE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_CUERPO = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_CUERPO_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_VALOR_DESTACADO = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_RECIBO_MONO = new Font("Monospaced", Font.PLAIN, 12);
}
