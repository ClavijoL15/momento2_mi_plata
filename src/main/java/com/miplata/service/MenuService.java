package com.miplata.service;

import java.util.Scanner;

/**
 * Servicio que gestiona toda la interfaz de usuario por consola (prompt).
 * Muestra los menús e interactúa con el usuario mediante Scanner.
 */
public class MenuService {

    // ===================== ATRIBUTOS =====================

    private UserService userService;
    private AccountService accountService;
    private Scanner scanner;
    private boolean running;

    // ===================== CONSTRUCTOR =====================

    public MenuService(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    // ===================== MENÚ PRINCIPAL =====================

    /**
     * Muestra el menú principal y dirige al usuario a Iniciar o Registrar.
     */
    public void showMainMenu() {
        while (running) {
            printHeader("BANCO MI PLATA");
            System.out.println("  1. Iniciar Sesión");
            System.out.println("  2. Registrar");
            System.out.println("  3. Salir");
            System.out.println("-----------------------------");
            System.out.print("  Elija una opción: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    showLoginMenu();
                    break;
                case "2":
                    showRegisterMenu();
                    break;
                case "3":
                    running = false;
                    System.out.println("\n✔ ¡Gracias por usar Banco Mi Plata! Hasta pronto.");
                    break;
                default:
                    System.out.println("⚠ Opción inválida. Intente de nuevo.\n");
            }
        }
    }

    // ===================== MENÚ DE REGISTRO =====================

    /**
     * Muestra el formulario de registro de nuevo usuario.
     * Solicita: Identificación, Usuario, Correo, Clave, Repetir Clave, Saldo Inicial.
     */
    private void showRegisterMenu() {
        printHeader("REGISTRAR NUEVO USUARIO");

        System.out.print("  Nombre de usuario : ");
        String username = scanner.nextLine().trim();

        System.out.print("  Correo electrónico: ");
        String email = scanner.nextLine().trim();

        System.out.print("  Clave             : ");
        String password = scanner.nextLine().trim();

        System.out.print("  Repetir clave     : ");
        String confirmPassword = scanner.nextLine().trim();

        if (!password.equals(confirmPassword)) {
            System.out.println("⚠ Las claves no coinciden. Intente de nuevo.\n");
            return;
        }

        double initialBalance = 0;
        System.out.print("  Saldo inicial     : $");
        try {
            initialBalance = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Saldo inválido. Se asignará saldo de $0.\n");
        }

        userService.registerUser(username, email, password, initialBalance);
        System.out.println();
    }

    // ===================== MENÚ DE INICIO DE SESIÓN =====================

    /**
     * Muestra el formulario de inicio de sesión.
     * Permite máximo 3 intentos antes de bloquear la cuenta.
     */
    private void showLoginMenu() {
        printHeader("INICIAR SESIÓN");
        int attempts = 0;

        while (attempts < 3) {
            System.out.print("  Usuario : ");
            String username = scanner.nextLine().trim();

            System.out.print("  Clave   : ");
            String password = scanner.nextLine().trim();

            boolean success = userService.login(username, password);

            if (success) {
                showTransactionMenu();
                return;
            }

            attempts++;
            System.out.println("  Intentos realizados: " + attempts + "/3\n");

            if (attempts >= 3) {
                System.out.println("🔒 Cuenta bloqueada por 24 horas. Comunícate con tu banco.\n");
            }
        }
    }

    // ===================== MENÚ DE TRANSACCIONES =====================

    /**
     * Muestra el menú de transacciones (solo accesible tras iniciar sesión).
     * Opciones: Retirar, Consultar Saldo, Consignar, Consultar Movimientos, Transferir, Salir.
     */
    private void showTransactionMenu() {
        boolean inSession = true;

        while (inSession) {
            printHeader("CONSULTAS Y MOVIMIENTOS");
            System.out.println("  1. Retirar Dinero");
            System.out.println("  2. Consultar Saldo");
            System.out.println("  3. Consignar Dinero");
            System.out.println("  4. Consultar Movimientos");
            System.out.println("  5. Transferir a otro usuario  [RETO]");
            System.out.println("  6. Salir");
            System.out.println("-------------------------------");
            System.out.print("  Elija una opción: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    showWithdrawMenu();
                    break;
                case "2":
                    accountService.consultBalance();
                    System.out.println();
                    break;
                case "3":
                    showDepositMenu();
                    break;
                case "4":
                    accountService.consultTransactions();
                    System.out.println();
                    break;
                case "5":
                    showTransferMenu();
                    break;
                case "6":
                    userService.logout();
                    inSession = false;
                    System.out.println();
                    break;
                default:
                    System.out.println("⚠ Opción inválida. Intente de nuevo.\n");
            }
        }
    }

    // ===================== SUBMENÚS DE TRANSACCIONES =====================

    /** Submenú para retirar dinero. */
    private void showWithdrawMenu() {
        printHeader("RETIRAR DINERO");
        System.out.printf("  Saldo actual: $%,.2f%n", accountService.getBalance());
        System.out.print("  Monto a retirar: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            accountService.withdraw(amount);
        } catch (NumberFormatException e) {
            System.out.println("⚠ Monto inválido. Ingrese un número.");
        }
        System.out.println();
    }

    /** Submenú para consignar dinero. */
    private void showDepositMenu() {
        printHeader("CONSIGNAR DINERO");
        System.out.printf("  Saldo actual: $%,.2f%n", accountService.getBalance());
        System.out.print("  Monto a consignar: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            accountService.deposit(amount);
        } catch (NumberFormatException e) {
            System.out.println("⚠ Monto inválido. Ingrese un número.");
        }
        System.out.println();
    }

    /** Submenú para transferir dinero a otro usuario. */
    private void showTransferMenu() {
        printHeader("TRANSFERIR DINERO");
        System.out.printf("  Saldo actual: $%,.2f%n", accountService.getBalance());
        System.out.print("  Usuario destino: ");
        String target = scanner.nextLine().trim();
        System.out.print("  Monto a transferir: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            accountService.transfer(target, amount);
        } catch (NumberFormatException e) {
            System.out.println("⚠ Monto inválido. Ingrese un número.");
        }
        System.out.println();
    }

    // ===================== UTILIDADES =====================

    /** Imprime un encabezado visual para los menús. */
    private void printHeader(String title) {
        System.out.println();
        System.out.println("=============================");
        System.out.println("  " + title);
        System.out.println("=============================");
    }
}
