package app;

import java.util.Scanner;
import modelo.Cuenta;
import modelo.InventarioEfectivoCajero;
import modelo.PoliticaComisionPorcentaje;
import modelo.PrevisualizacionRetiro;
import modelo.Recibo;
import modelo.ServicioRetiro;
import modelo.excepcion.CajeroException;
import modelo.excepcion.EfectivoCajeroInsuficienteException;
import modelo.excepcion.MontoInvalidoException;
import modelo.excepcion.SaldoInsuficienteException;

/**
 * Modo Consola interactivo para el Cajero Automático (ATM).
 * Permite ejecutar el caso de uso completo "Retirar Efectivo" desde terminal.
 */
public class SimuladorConsolaATM {

    public static void main(String[] args) {
        Cuenta cuenta = new Cuenta("**** 4589", "Juan Pérez", 5000.00);
        InventarioEfectivoCajero inventario = new InventarioEfectivoCajero(50000.00);
        PoliticaComisionPorcentaje politica = new PoliticaComisionPorcentaje(0.02, 5.00);
        ServicioRetiro servicio = new ServicioRetiro(inventario, politica);

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        System.out.println("=================================================");
        System.out.println("        SISTEMA DE CAJERO AUTOMÁTICO (ATM)       ");
        System.out.println("          Caso de Uso: Retirar Efectivo          ");
        System.out.println("=================================================");

        while (!salir) {
            System.out.println("\n-------------------------------------------------");
            System.out.printf("Cliente: %s | Cuenta: %s | Saldo: $%.2f\n",
                cuenta.getTitular(), cuenta.getNumeroCuenta(), cuenta.getSaldo());
            System.out.println("-------------------------------------------------");
            System.out.println("Seleccione un monto disponible:");
            System.out.println("1) $200.00");
            System.out.println("2) $400.00");
            System.out.println("3) $600.00");
            System.out.println("4) $800.00");
            System.out.println("5) $1,000.00");
            System.out.println("6) Otro monto");
            System.out.println("7) Simular Falla de Servidor (ATM-502)");
            System.out.println("8) Restablecer Saldo a $5,000.00");
            System.out.println("9) Salir");
            System.out.print("Elija una opción [1-9]: ");

            String opcion = scanner.nextLine().trim();
            double montoSolicitado = 0.0;

            switch (opcion) {
                case "1": montoSolicitado = 200.00; break;
                case "2": montoSolicitado = 400.00; break;
                case "3": montoSolicitado = 600.00; break;
                case "4": montoSolicitado = 800.00; break;
                case "5": montoSolicitado = 1000.00; break;
                case "6":
                    System.out.print("Ingrese monto deseado (múltiplo de $200.00): $");
                    try {
                        montoSolicitado = Double.parseDouble(scanner.nextLine().trim());
                        servicio.validarMonto(montoSolicitado);
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Formato numérico inválido.");
                        continue;
                    } catch (MontoInvalidoException e) {
                        System.out.println("[ERROR] " + e.getMessage());
                        continue;
                    }
                    break;
                case "7":
                    boolean actual = servicio.isSimularErrorServidor();
                    servicio.setSimularErrorServidor(!actual);
                    System.out.println("\n[SIMULACIÓN] Modo falla servidor ATM-502: " + (!actual ? "ACTIVADO" : "DESACTIVADO"));
                    continue;
                case "8":
                    cuenta.restablecerSaldoPrueba();
                    servicio.setSimularErrorServidor(false);
                    System.out.println("\n[INFO] Saldo restablecido exitosamente a $5,000.00.");
                    continue;
                case "9":
                    salir = true;
                    System.out.println("\n¡Gracias por utilizar el Cajero Automático!");
                    continue;
                default:
                    System.out.println("[ERROR] Opción no válida. Intente de nuevo.");
                    continue;
            }

            // Paso 3: Previsualización de Estado de Cuenta
            PrevisualizacionRetiro prev = servicio.previsualizarRetiro(cuenta, montoSolicitado);
            System.out.println("\n================ ESTADO DE CUENTA ================");
            System.out.printf("Cliente:                %s\n", prev.getTitular());
            System.out.printf("Saldo Actual:           $%.2f\n", prev.getSaldoActual());
            System.out.printf("Monto a retirar:        $%.2f\n", prev.getMontoRetiro());
            System.out.printf("%-23s -$%.2f\n", prev.getEtiquetaComision(), prev.getComision());
            System.out.println("-------------------------------------------------");
            System.out.printf("Monto final estimado:   $%.2f\n", prev.getSaldoEstimado());
            System.out.println("=================================================");

            System.out.print("¿Desea confirmar el retiro? (S = Confirmar / C = Cancelar): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();

            if (!confirmacion.equals("S")) {
                servicio.registrarCancelacion(cuenta, montoSolicitado);
                System.out.println("\n-------------------------------------------------");
                System.out.println("              TRANSACCIÓN CANCELADA              ");
                System.out.println("   Su operación ha sido cancelada exitosamente.  ");
                System.out.println("   No se ha realizado ningún cargo a su cuenta.  ");
                System.out.println("-------------------------------------------------");
                continue;
            }

            // Paso 4: Procesamiento y Transacción Final
            System.out.println("\nProcesando su transacción... Por favor espere...");
            try {
                Thread.sleep(800); // Pequeña pausa de simulación
            } catch (InterruptedException ignored) {}

            try {
                Recibo recibo = servicio.ejecutarRetiro(cuenta, montoSolicitado);
                System.out.println("\n¡OPERACIÓN EXITOSA!");
                System.out.println("Por favor, retire su efectivo y su tarjeta.\n");
                System.out.println(recibo.generarTextoTicket());

            } catch (SaldoInsuficienteException ex) {
                System.out.println("\n=================================================");
                System.out.println("               SALDO INSUFICIENTE                ");
                System.out.println("No cuenta con fondos suficientes para realizar   ");
                System.out.println("esta transacción.                                ");
                System.out.printf("Saldo disponible: $%.2f\n", ex.getSaldoDisponible());
                System.out.printf("Faltan: $%.2f\n", ex.getMontoFaltante());
                System.out.println("=================================================");

            } catch (EfectivoCajeroInsuficienteException ex) {
                System.out.println("\n=================================================");
                System.out.println("               TRANSACCIÓN FALLIDA               ");
                System.out.println("Código de error: " + ex.getCodigoError());
                System.out.println("El cajero no tiene suficiente efectivo disponible.");
                System.out.println("=================================================");

            } catch (CajeroException ex) {
                System.out.println("\n=================================================");
                System.out.println("               TRANSACCIÓN FALLIDA               ");
                System.out.println("Código de error: " + ex.getCodigoError());
                System.out.println("Ha ocurrido un error al procesar su solicitud.   ");
                System.out.println("Posibles causas: Problema de conexión servidor.  ");
                System.out.println("=================================================");
            }
        }

        scanner.close();
    }
}
