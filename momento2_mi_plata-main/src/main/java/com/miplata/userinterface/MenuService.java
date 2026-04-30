package com.miplata.userinterface;

import com.miplata.service.AccountService;
import com.miplata.service.UserService;

import java.util.Scanner;

/**
 * Servicio de interfaz de usuario por consola.
 * Movido al paquete 'userinterface' como parte de la reestructuración.
 */
public class MenuService {

    // ===================== ATRIBUTOS =====================
    private final UserService    userService;
    private final AccountService accountService;
    private final Scanner        scanner;
    private boolean              running;

    // ===================== CONSTRUCTOR =====================
    public MenuService(UserService userService, AccountService accountService) {
        this.userService    = userService;
        this.accountService = accountService;
        this.scanner        = new Scanner(System.in);
        this.running        = true;
    }

    // ===================== MENÚ PRINCIPAL =====================

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
                case "1": showLoginMenu();    break;
                case "2": showRegisterMenu(); break;
                case "3":
                    running = false;
                    System.out.println("\n✔ ¡Gracias por usar Banco Mi Plata! Hasta pronto.");
                    break;
                default: System.out.println("⚠ Opción inválida.\n");
            }
        }
    }

    // ===================== REGISTRO =====================

    private void showRegisterMenu() {
        printHeader("REGISTRAR NUEVO USUARIO");
        System.out.print("  Nombre de usuario : "); String username = scanner.nextLine().trim();
        System.out.print("  Correo electrónico: "); String email    = scanner.nextLine().trim();
        System.out.print("  Clave             : "); String password = scanner.nextLine().trim();
        System.out.print("  Repetir clave     : "); String confirm  = scanner.nextLine().trim();
        if (!password.equals(confirm)) { System.out.println("⚠ Las claves no coinciden.\n"); return; }
        double balance = 0;
        System.out.print("  Saldo inicial     : $");
        try { balance = Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("⚠ Saldo inválido. Se asignará $0.\n"); }
        userService.registerUser(username, email, password, balance);
        System.out.println();
    }

    // ===================== LOGIN =====================

    private void showLoginMenu() {
        printHeader("INICIAR SESIÓN");
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("  Usuario : "); String username = scanner.nextLine().trim();
            System.out.print("  Clave   : "); String password = scanner.nextLine().trim();
            if (userService.login(username, password)) { showTransactionMenu(); return; }
            attempts++;
            System.out.println("  Intentos realizados: " + attempts + "/3\n");
            if (attempts >= 3) System.out.println("🔒 Demasiados intentos fallidos.\n");
        }
    }

    // ===================== MENÚ TRANSACCIONES =====================

    private void showTransactionMenu() {
        boolean inSession = true;
        while (inSession) {
            printHeader("CONSULTAS Y MOVIMIENTOS");
            System.out.println("  1. Retirar Dinero");
            System.out.println("  2. Consultar Saldo");
            System.out.println("  3. Consignar Dinero");
            System.out.println("  4. Consultar Movimientos");
            System.out.println("  5. Transferir a otro usuario");
            System.out.println("  6. Cerrar Sesión");
            System.out.println("-------------------------------");
            System.out.print("  Elija una opción: ");
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": showWithdrawMenu();  break;
                case "2": accountService.consultBalance(); System.out.println(); break;
                case "3": showDepositMenu();   break;
                case "4": accountService.consultTransactions(); System.out.println(); break;
                case "5": showTransferMenu();  break;
                case "6": userService.logout(); inSession = false; System.out.println(); break;
                default:  System.out.println("⚠ Opción inválida.\n");
            }
        }
    }

    // ===================== SUBMENÚS =====================

    private void showWithdrawMenu() {
        printHeader("RETIRAR DINERO");
        System.out.printf("  Saldo actual: $%,.2f%n", accountService.getBalance());
        System.out.print("  Monto a retirar: $");
        try { accountService.withdraw(Double.parseDouble(scanner.nextLine().trim())); }
        catch (NumberFormatException e) { System.out.println("⚠ Monto inválido."); }
        System.out.println();
    }

    private void showDepositMenu() {
        printHeader("CONSIGNAR DINERO");
        System.out.printf("  Saldo actual: $%,.2f%n", accountService.getBalance());
        System.out.print("  Monto a consignar: $");
        try { accountService.deposit(Double.parseDouble(scanner.nextLine().trim())); }
        catch (NumberFormatException e) { System.out.println("⚠ Monto inválido."); }
        System.out.println();
    }

    private void showTransferMenu() {
        printHeader("TRANSFERIR DINERO");
        System.out.printf("  Saldo actual: $%,.2f%n", accountService.getBalance());
        System.out.print("  Usuario destino: ");    String target = scanner.nextLine().trim();
        System.out.print("  Monto a transferir: $");
        try { accountService.transfer(target, Double.parseDouble(scanner.nextLine().trim())); }
        catch (NumberFormatException e) { System.out.println("⚠ Monto inválido."); }
        System.out.println();
    }

    // ===================== UTILIDADES =====================

    private void printHeader(String title) {
        System.out.println("\n=============================");
        System.out.println("  " + title);
        System.out.println("=============================");
    }
}