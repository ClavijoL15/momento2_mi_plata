package com.miplata.service;

import com.miplata.model.Transaction;
import com.miplata.model.TransactionType;
import com.miplata.model.User;

import java.util.List;

/**
 * Servicio que gestiona todas las operaciones bancarias de una cuenta:
 * consultar saldo, retirar, consignar y transferir.
 */
public class AccountService {

    // ===================== ATRIBUTOS =====================

    private UserService userService;    // Referencia al servicio de usuarios
    private int nextTransactionId;      // Contador de IDs de transacciones

    // ===================== CONSTRUCTOR =====================

    public AccountService(UserService userService) {
        this.userService = userService;
        this.nextTransactionId = 1;
    }

    // ===================== CONSULTAR SALDO =====================

    /**
     * Muestra el saldo actual del usuario en sesión.
     */
    public void consultBalance() {
        User user = userService.getCurrentUser();
        if (user == null) {
            System.out.println("⚠ No hay sesión activa.");
            return;
        }
        System.out.println("========== SALDO ==========");
        System.out.println("Usuario : " + user.getUsername());
        System.out.printf("Saldo   : $%,.2f%n", user.getBalance());
        System.out.println("===========================");
    }

    /**
     * Retorna el saldo del usuario en sesión.
     * @return Saldo actual o -1 si no hay sesión
     */
    public double getBalance() {
        User user = userService.getCurrentUser();
        if (user == null) {
            System.out.println("⚠ No hay sesión activa.");
            return -1;
        }
        return user.getBalance();
    }

    /**
     * Retorna el saldo de un usuario específico por su nombre.
     * @param username Nombre del usuario
     * @return Saldo o -1 si no existe
     */
    public double getBalanceByUser(String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            System.out.println("⚠ Usuario no encontrado.");
            return -1;
        }
        return user.getBalance();
    }

    // ===================== RETIRAR DINERO =====================

    /**
     * Realiza un retiro de dinero de la cuenta del usuario en sesión.
     * Valida que el monto no supere el saldo disponible.
     * @param amount Monto a retirar
     * @return true si el retiro fue exitoso, false si no
     */
    public boolean withdraw(double amount) {
        User user = userService.getCurrentUser();

        if (user == null) {
            System.out.println("⚠ No hay sesión activa.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("⚠ El monto debe ser un valor positivo.");
            return false;
        }
        if (amount > user.getBalance()) {
            System.out.printf("⚠ Saldo insuficiente. Saldo disponible: $%,.2f%n", user.getBalance());
            return false;
        }

        // Ejecutar retiro
        double newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);

        // Registrar transacción
        Transaction tx = new Transaction(nextTransactionId++, TransactionType.RETIRO, amount, newBalance);
        user.addTransaction(tx);

        System.out.println("✔ Retiro exitoso.");
        System.out.printf("   Monto retirado : $%,.2f%n", amount);
        System.out.printf("   Nuevo saldo     : $%,.2f%n", newBalance);
        return true;
    }

    // ===================== CONSIGNAR DINERO =====================

    /**
     * Realiza una consignación (depósito) en la cuenta del usuario en sesión.
     * Valida que el monto sea positivo.
     * @param amount Monto a consignar
     * @return true si la consignación fue exitosa, false si no
     */
    public boolean deposit(double amount) {
        User user = userService.getCurrentUser();

        if (user == null) {
            System.out.println("⚠ No hay sesión activa.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("⚠ El monto a consignar debe ser un valor positivo.");
            return false;
        }

        // Ejecutar consignación
        double newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);

        // Registrar transacción
        Transaction tx = new Transaction(nextTransactionId++, TransactionType.CONSIGNACION, amount, newBalance);
        user.addTransaction(tx);

        System.out.println("✔ Consignación exitosa.");
        System.out.printf("   Monto consignado : $%,.2f%n", amount);
        System.out.printf("   Nuevo saldo       : $%,.2f%n", newBalance);
        return true;
    }

    // ===================== TRANSFERIR DINERO (RETO) =====================

    /**
     * Transfiere dinero del usuario en sesión hacia otro usuario.
     * (Funcionalidad opcional/reto del proyecto)
     * @param targetUsername Usuario destino
     * @param amount         Monto a transferir
     * @return true si la transferencia fue exitosa, false si no
     */
    public boolean transfer(String targetUsername, double amount) {
        User sender = userService.getCurrentUser();
        User receiver = userService.findUserByUsername(targetUsername);

        if (sender == null) {
            System.out.println("⚠ No hay sesión activa.");
            return false;
        }
        if (receiver == null) {
            System.out.println("⚠ Usuario destino '" + targetUsername + "' no encontrado.");
            return false;
        }
        if (sender.getUsername().equalsIgnoreCase(targetUsername)) {
            System.out.println("⚠ No puedes transferirte dinero a ti mismo.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("⚠ El monto debe ser un valor positivo.");
            return false;
        }
        if (amount > sender.getBalance()) {
            System.out.printf("⚠ Saldo insuficiente. Saldo disponible: $%,.2f%n", sender.getBalance());
            return false;
        }

        // Ejecutar transferencia
        double senderNewBalance = sender.getBalance() - amount;
        double receiverNewBalance = receiver.getBalance() + amount;

        sender.setBalance(senderNewBalance);
        receiver.setBalance(receiverNewBalance);

        // Registrar transacciones en ambos usuarios
        Transaction txSender = new Transaction(
                nextTransactionId++,
                TransactionType.TRANSFERENCIA_ENVIADA,
                amount,
                senderNewBalance,
                "Transferencia a " + receiver.getUsername(),
                receiver.getUsername()
        );
        Transaction txReceiver = new Transaction(
                nextTransactionId++,
                TransactionType.TRANSFERENCIA_RECIBIDA,
                amount,
                receiverNewBalance,
                "Transferencia de " + sender.getUsername(),
                sender.getUsername()
        );

        sender.addTransaction(txSender);
        receiver.addTransaction(txReceiver);

        System.out.println("✔ Transferencia exitosa.");
        System.out.printf("   Transferido a  : %s%n", receiver.getUsername());
        System.out.printf("   Monto           : $%,.2f%n", amount);
        System.out.printf("   Tu nuevo saldo  : $%,.2f%n", senderNewBalance);
        return true;
    }

    // ===================== CONSULTAR MOVIMIENTOS =====================

    /**
     * Muestra el historial de transacciones del usuario en sesión.
     */
    public void consultTransactions() {
        User user = userService.getCurrentUser();

        if (user == null) {
            System.out.println("⚠ No hay sesión activa.");
            return;
        }

        List<Transaction> transactions = user.getTransactions();

        System.out.println("============================================================");
        System.out.println("             CONSULTA DE MOVIMIENTOS - " + user.getUsername());
        System.out.println("============================================================");

        if (transactions.isEmpty()) {
            System.out.println("  No hay movimientos registrados.");
        } else {
            System.out.printf("| %-19s | %-14s | %12s | %12s |%n",
                    "Fecha y Hora", "Concepto", "Valor", "Saldo");
            System.out.println("|---------------------|----------------|--------------|--------------|");

            for (Transaction tx : transactions) {
                tx.printInformation();
            }
        }
        System.out.println("============================================================");
        System.out.printf("Saldo actual: $%,.2f%n", user.getBalance());
    }

    /**
     * Retorna la lista de transacciones del usuario en sesión.
     * @return Lista de transacciones o null si no hay sesión
     */
    public List<Transaction> getTransactionHistory() {
        User user = userService.getCurrentUser();
        if (user == null) return null;
        return user.getTransactions();
    }
}
