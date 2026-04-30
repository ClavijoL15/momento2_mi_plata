package com.miplata.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario/cliente del banco "Mi Plata".
 * Contiene la información personal, credenciales y estado de la cuenta.
 */
public class User {

    // ===================== ATRIBUTOS =====================

    private int id;
    private String username;
    private String email;
    private String password;
    private double balance;          // Saldo actual
    private boolean active;          // true = activa, false = bloqueada
    private int loginAttempts;       // Contador de intentos fallidos (máx. 3)
    private List<Transaction> transactions; // Historial de movimientos

    // ===================== CONSTRUCTORES =====================

    /** Constructor vacío */
    public User() {
        this.transactions = new ArrayList<>();
        this.active = true;
        this.loginAttempts = 0;
    }

    /** Constructor con parámetros principales */
    public User(int id, String username, String email, String password, double balance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.active = true;
        this.loginAttempts = 0;
        this.transactions = new ArrayList<>();
    }

    // ===================== GETTERS Y SETTERS =====================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // ===================== MÉTODOS DE NEGOCIO =====================

    /**
     * Incrementa el contador de intentos fallidos de login.
     * Si llega a 3, bloquea la cuenta automáticamente.
     */
    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= 3) {
            this.active = false;
        }
    }

    /** Resetea el contador de intentos al iniciar sesión correctamente. */
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
    }

    /** Agrega una transacción al historial del usuario. */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /** Imprime la información del usuario (sin mostrar la contraseña). */
    public void printInformation() {
        System.out.println("========== USUARIO ==========");
        System.out.println("ID       : " + id);
        System.out.println("Usuario  : " + username);
        System.out.println("Email    : " + email);
        System.out.printf("Saldo    : $%,.2f%n", balance);
        System.out.println("Estado   : " + (active ? "Activa" : "BLOQUEADA"));
        System.out.println("Intentos : " + loginAttempts + "/3");
        System.out.println("=============================");
    }
}
