package app;

import controlador.ControladorRetiro;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import modelo.Cuenta;
import modelo.InventarioEfectivoCajero;
import modelo.PoliticaComisionPorcentaje;
import modelo.ServicioRetiro;
import vista.VentanaPrincipalATM;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Cuenta cuentaPrueba = new Cuenta("**** 4589", "Juan Pérez", 5000.00);
            InventarioEfectivoCajero inventario = new InventarioEfectivoCajero(50000.00);
            PoliticaComisionPorcentaje politicaComision = new PoliticaComisionPorcentaje(0.02, 5.00);

            ServicioRetiro servicioRetiro = new ServicioRetiro(inventario, politicaComision);
            ControladorRetiro controlador = new ControladorRetiro(cuentaPrueba, servicioRetiro);

            VentanaPrincipalATM ventana = new VentanaPrincipalATM();
            controlador.registrarVista(ventana);

            ventana.setVisible(true);
        });
    }
}
