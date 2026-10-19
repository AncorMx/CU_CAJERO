package vista.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 * Biblioteca de componentes visuales modernos para la interfaz gráfica del ATM.
 */
public final class ComponentesUI {

    private ComponentesUI() {}

    /**
     * Panel con esquinas redondeadas, fondo plano y borde sutil.
     */
    public static class PanelRedondeado extends JPanel {
        private int radioEsquinas;
        private Color colorFondo;
        private Color colorBorde;
        private int grosorBorde;

        public PanelRedondeado(int radioEsquinas, Color colorFondo, Color colorBorde, int grosorBorde) {
            super();
            this.radioEsquinas = radioEsquinas;
            this.colorFondo = colorFondo;
            this.colorBorde = colorBorde;
            this.grosorBorde = grosorBorde;
            setOpaque(false);
        }

        public PanelRedondeado(int radioEsquinas, Color colorFondo) {
            this(radioEsquinas, colorFondo, PaletaColores.BORDE_SUAVE, 1);
        }

        public void setColorFondo(Color colorFondo) {
            this.colorFondo = colorFondo;
            repaint();
        }

        public void setColorBorde(Color colorBorde) {
            this.colorBorde = colorBorde;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Fondo
            g2.setColor(colorFondo);
            g2.fill(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, radioEsquinas, radioEsquinas));

            // Borde
            if (colorBorde != null && grosorBorde > 0) {
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(grosorBorde));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, width - 1.5, height - 1.5, radioEsquinas, radioEsquinas));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Botón moderno con soporte de hover, esquinas redondeadas y colores personalizados.
     */
    public static class BotonModerno extends JButton {
        private Color colorBase;
        private Color colorHover;
        private Color colorPresionado;
        private Color colorBorde;
        private int radioEsquinas;
        private boolean isHover = false;

        public BotonModerno(String texto, Color fondo, Color hover, Color textoColor, int radio) {
            super(texto);
            this.colorBase = fondo;
            this.colorHover = hover;
            this.colorPresionado = hover.darker();
            this.colorBorde = null;
            this.radioEsquinas = radio;

            setForeground(textoColor);
            setFont(PaletaColores.FUENTE_BOTON);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        isHover = true;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
            });
        }

        public BotonModerno(String texto, Color fondo, Color hover, Color textoColor) {
            this(texto, fondo, hover, textoColor, 10);
        }

        public void setColorBorde(Color borde) {
            this.colorBorde = borde;
            repaint();
        }

        public void setColorBase(Color fondo, Color hover) {
            this.colorBase = fondo;
            this.colorHover = hover;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color actualFondo = isHover ? colorHover : colorBase;
            if (getModel().isPressed()) {
                actualFondo = colorPresionado;
            }
            if (!isEnabled()) {
                actualFondo = PaletaColores.GRIS_BOTON;
            }

            g2.setColor(actualFondo);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radioEsquinas, radioEsquinas));

            if (colorBorde != null) {
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, radioEsquinas, radioEsquinas));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Botón para selección de montos en la cuadrícula ($200, $400, $600, etc.).
     * Muestra borde azul y fondo claro cuando está seleccionado.
     */
    public static class BotonMonto extends JButton {
        private final double monto;
        private boolean seleccionado = false;
        private boolean isHover = false;

        public BotonMonto(String texto, double monto) {
            super(texto);
            this.monto = monto;
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(PaletaColores.AZUL_PRIMARIO);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(140, 52));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
            });
        }

        public double getMonto() {
            return monto;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (seleccionado) {
                g2.setColor(PaletaColores.AZUL_FONDO_SELECCION);
                g2.fill(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 10, 10));
                g2.setColor(PaletaColores.AZUL_PRIMARIO);
                g2.setStroke(new BasicStroke(2.0f));
                g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 10, 10));
                setForeground(PaletaColores.AZUL_PRIMARIO);
            } else {
                g2.setColor(isHover ? new Color(248, 250, 252) : Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 10, 10));
                g2.setColor(isHover ? PaletaColores.AZUL_PRIMARIO : PaletaColores.BORDE_SUAVE);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 10, 10));
                setForeground(PaletaColores.AZUL_PRIMARIO);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Indicador circular giratorio tipo spinner para la pantalla de procesamiento.
     */
    public static class SpinnerAnimado extends JComponent {
        private int angulo = 0;
        private final Timer timer;

        public SpinnerAnimado(int diametro) {
            setPreferredSize(new Dimension(diametro, diametro));
            setMinimumSize(new Dimension(diametro, diametro));
            setOpaque(false);

            timer = new Timer(30, e -> {
                angulo = (angulo + 10) % 360;
                repaint();
            });
        }

        public void iniciar() {
            if (!timer.isRunning()) {
                timer.start();
            }
        }

        public void detener() {
            if (timer.isRunning()) {
                timer.stop();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            // Borde base circular suave
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(224, 231, 255));
            g2.drawOval(x, y, size, size);

            // Arco activo giratorio
            g2.setColor(PaletaColores.AZUL_PRIMARIO);
            g2.drawArc(x, y, size, size, angulo, 110);

            g2.dispose();
        }
    }

    /**
     * Dibuja los iconos vectoriales de estado:
     * EXITO, ERROR_SALDO, ERROR_SISTEMA, CANCELADO.
     */
    public static class IconoEstadoVisual extends JComponent {
        public enum TipoIcono { EXITO, ERROR_SALDO, ERROR_SISTEMA, CANCELADO }

        private final TipoIcono tipo;
        private final int diametro;

        public IconoEstadoVisual(TipoIcono tipo, int diametro) {
            this.tipo = tipo;
            this.diametro = diametro;
            setPreferredSize(new Dimension(diametro, diametro));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int d = Math.min(w, h) - 6;
            int x = (w - d) / 2;
            int y = (h - d) / 2;

            switch (tipo) {
                case EXITO:
                    // Círculo Verde
                    g2.setColor(PaletaColores.VERDE_EXITO);
                    g2.fillOval(x, y, d, d);
                    // Checkmark Blanco
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int x1 = x + (int)(d * 0.28);
                    int y1 = y + (int)(d * 0.52);
                    int x2 = x + (int)(d * 0.44);
                    int y2 = y + (int)(d * 0.68);
                    int x3 = x + (int)(d * 0.72);
                    int y3 = y + (int)(d * 0.36);
                    g2.drawLine(x1, y1, x2, y2);
                    g2.drawLine(x2, y2, x3, y3);
                    break;

                case ERROR_SALDO:
                    // Círculo Rojo
                    g2.setColor(PaletaColores.ROJO_ERROR);
                    g2.fillOval(x, y, d, d);
                    // Cruz Blanca
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int pad = (int)(d * 0.30);
                    g2.drawLine(x + pad, y + pad, x + d - pad, y + d - pad);
                    g2.drawLine(x + d - pad, y + pad, x + pad, y + d - pad);
                    break;

                case ERROR_SISTEMA:
                    // Triángulo naranja/ámbar con bordes suavizados
                    g2.setColor(PaletaColores.AMBAR_ALERTA);
                    Polygon tri = new Polygon();
                    tri.addPoint(x + d / 2, y + 2);
                    tri.addPoint(x + d, y + d - 2);
                    tri.addPoint(x, y + d - 2);
                    g2.fillPolygon(tri);
                    // Signo de exclamación blanco
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + d / 2, y + (int)(d * 0.35), x + d / 2, y + (int)(d * 0.65));
                    g2.fillOval(x + d / 2 - 2, y + (int)(d * 0.76), 5, 5);
                    break;

                case CANCELADO:
                    // Círculo Gris pizarra
                    g2.setColor(new Color(148, 163, 184)); // Slate 400
                    g2.fillOval(x, y, d, d);
                    // Cruz Blanca
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int p = (int)(d * 0.30);
                    g2.drawLine(x + p, y + p, x + d - p, y + d - p);
                    g2.drawLine(x + d - p, y + p, x + p, y + d - p);
                    break;
            }

            g2.dispose();
        }
    }
}
