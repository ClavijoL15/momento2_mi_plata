package com.miplata.service;

import com.miplata.domain.Transaction;
import com.miplata.domain.TransactionType;
import com.miplata.domain.User;

import java.util.List;

/**
 * Servicio de operaciones bancarias.
 * Actualizado para usar los dominios del paquete 'domain'.
 */
public class AccountService {

    // ===================== ATRIBUTOS =====================
    private final UserService userService;
    private int nextTransactionId;

    // ===================== CONSTRUCTOR =====================
    public AccountService(UserService userService) {
        this.userService       = userService;
        this.nextTransactionId = 1;
    }

    // ===================== CONSULTAR SALDO =====================

    public void consultBalance() {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return; }
        System.out.println("========== SALDO ==========");
        System.out.println("Usuario : " + user.getUsername());
        System.out.printf ("Saldo   : $%,.2f%n", user.getBalance());
        System.out.println("===========================");
    }

    public double getBalance() {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return -1; }
        return user.getBalance();
    }

    public double getBalanceByUser(String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) { System.out.println("⚠ Usuario no encontrado."); return -1; }
        return user.getBalance();
    }

    // ===================== RETIRAR =====================

    public boolean withdraw(double amount) {
        User user = userService.getCurrentUser();
        if (user == null)     { System.out.println("⚠ No hay sesión activa."); return false; }
        if (amount <= 0)      { System.out.println("⚠ El monto debe ser positivo."); return false; }
        if (amount > user.getBalance()) {
            System.out.printf("⚠ Saldo insuficiente. Disponible: $%,.2f%n", user.getBalance());
            return false;
        }
        double newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);
        user.addTransaction(new Transaction(nextTransactionId++, TransactionType.RETIRO, amount, newBalance));
        System.out.println("✔ Retiro exitoso.");
        System.out.printf ("   Monto retirado : $%,.2f%n", amount);
        System.out.printf ("   Nuevo saldo     : $%,.2f%n", newBalance);
        return true;
    }

    // ===================== CONSIGNAR =====================

    public boolean deposit(double amount) {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return false; }
        if (amount <= 0)  { System.out.println("⚠ El monto debe ser positivo."); return false; }
        double newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        user.addTransaction(new Transaction(nextTransactionId++, TransactionType.CONSIGNACION, amount, newBalance));
        System.out.println("✔ Consignación exitosa.");
        System.out.printf ("   Monto consignado : $%,.2f%n", amount);
        System.out.printf ("   Nuevo saldo       : $%,.2f%n", newBalance);
        return true;
    }

    // ===================== TRANSFERIR =====================

    public boolean transfer(String targetUsername, double amount) {
        User sender   = userService.getCurrentUser();
        User receiver = userService.findUserByUsername(targetUsername);
        if (sender == null)   { System.out.println("⚠ No hay sesión activa."); return false; }
        if (receiver == null) { System.out.println("⚠ Usuario destino no encontrado."); return false; }
        if (sender.getUsername().equalsIgnoreCase(targetUsername)) {
            System.out.println("⚠ No puedes transferirte dinero a ti mismo."); return false;
        }
        if (amount <= 0)               { System.out.println("⚠ El monto debe ser positivo."); return false; }
        if (amount > sender.getBalance()) {
            System.out.printf("⚠ Saldo insuficiente. Disponible: $%,.2f%n", sender.getBalance()); return false;
        }
        double senderNew   = sender.getBalance()   - amount;
        double receiverNew = receiver.getBalance()  + amount;
        sender.setBalance(senderNew);
        receiver.setBalance(receiverNew);
        sender.addTransaction(new Transaction(nextTransactionId++,
                TransactionType.TRANSFERENCIA_ENVIADA, amount, senderNew,
                "Transferencia a " + receiver.getUsername(), receiver.getUsername()));
        receiver.addTransaction(new Transaction(nextTransactionId++,
                TransactionType.TRANSFERENCIA_RECIBIDA, amount, receiverNew,
                "Transferencia de " + sender.getUsername(), sender.getUsername()));
        System.out.println("✔ Transferencia exitosa.");
        System.out.printf ("   Transferido a : %s%n", receiver.getUsername());
        System.out.printf ("   Monto          : $%,.2f%n", amount);
        System.out.printf ("   Tu nuevo saldo : $%,.2f%n", senderNew);
        return true;
    }

    // ===================== HISTORIAL =====================

    public void consultTransactions() {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return; }
        List<Transaction> txs = user.getTransactions();
        System.out.println("============================================================");
        System.out.println("             MOVIMIENTOS - " + user.getUsername());
        System.out.println("============================================================");
        if (txs.isEmpty()) {
            System.out.println("  No hay movimientos registrados.");
        } else {
            System.out.printf("| %-19s | %-14s | %12s | %12s |%n", "Fecha y Hora","Concepto","Valor","Saldo");
            System.out.println("|---------------------|----------------|--------------|--------------| ");
            txs.forEach(Transaction::printInformation);
        }
        System.out.println("============================================================");
        System.out.printf("Saldo actual: $%,.2f%n", user.getBalance());
    }

    public List<Transaction> getTransactionHistory() {
        User user = userService.getCurrentUser();
        return user == null ? null : user.getTransactions();
    }
}
