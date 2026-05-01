package com.miplata.domain;

import com.miplata.config.AppConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario/cliente del banco "Mi Plata".
 * PR2: Se agrega el campo accountType (AHORROS, CORRIENTE, TARJETA_CREDITO).
 *      Para TARJETA_CREDITO se maneja también creditLimit y debt.
 */
public class User {

    // ===================== ATRIBUTOS =====================
    private int id;
    private String username;
    private String email;
    private String password;
    private double balance;
    private boolean active;
    private int loginAttempts;
    private List<Transaction> transactions;

    // --- NUEVO PR2 ---
    private AccountType accountType;
    private double creditLimit;
    private double debt;

    // ===================== CONSTRUCTORES =====================

    public User() {
        this.transactions  = new ArrayList<>();
        this.active        = true;
        this.loginAttempts = 0;
        this.accountType   = AccountType.AHORROS;
        this.creditLimit   = 0;
        this.debt          = 0;
    }

    public User(int id, String username, String email, String password, double balance) {
        this(id, username, email, password, balance, AccountType.AHORROS);
    }

    public User(int id, String username, String email, String password,
                double balance, AccountType accountType) {
        this.id            = id;
        this.username      = username;
        this.email         = email;
        this.password      = password;
        this.balance       = balance;
        this.active        = true;
        this.loginAttempts = 0;
        this.transactions  = new ArrayList<>();
        this.accountType   = accountType;
        this.creditLimit   = 0;
        this.debt          = 0;
    }

    // ===================== GETTERS Y SETTERS =====================

    public int getId()                               { return id; }
    public void setId(int id)                        { this.id = id; }
    public String getUsername()                      { return username; }
    public void setUsername(String username)         { this.username = username; }
    public String getEmail()                         { return email; }
    public void setEmail(String email)               { this.email = email; }
    public String getPassword()                      { return password; }
    public void setPassword(String password)         { this.password = password; }
    public double getBalance()                       { return balance; }
    public void setBalance(double balance)           { this.balance = balance; }
    public boolean isActive()                        { return active; }
    public void setActive(boolean active)            { this.active = active; }
    public int getLoginAttempts()                    { return loginAttempts; }
    public void setLoginAttempts(int v)              { this.loginAttempts = v; }
    public List<Transaction> getTransactions()       { return transactions; }
    public void setTransactions(List<Transaction> t) { this.transactions = t; }

    // --- NUEVO PR2 ---
    public AccountType getAccountType()                    { return accountType; }
    public void setAccountType(AccountType accountType)    { this.accountType = accountType; }
    public double getCreditLimit()                         { return creditLimit; }
    public void setCreditLimit(double creditLimit)         { this.creditLimit = creditLimit; }
    public double getDebt()                                { return debt; }
    public void setDebt(double debt)                       { this.debt = debt; }
    public double getAvailableCredit()                     { return creditLimit - debt; }

    // ===================== MÉTODOS DE NEGOCIO =====================

    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= AppConfig.MAX_ATTEMPTS) this.active = false;
    }

    public void resetLoginAttempts() { this.loginAttempts = 0; }
    public void addTransaction(Transaction t) { this.transactions.add(t); }

    public void printInformation() {
        System.out.println("========== USUARIO ==========");
        System.out.println("ID       : " + id);
        System.out.println("Usuario  : " + username);
        System.out.println("Email    : " + email);
        System.out.println("Cuenta   : " + accountType.getLabel());
        if (accountType == AccountType.TARJETA_CREDITO) {
            System.out.printf("Cupo     : $%,.2f%n", creditLimit);
            System.out.printf("Deuda    : $%,.2f%n", debt);
            System.out.printf("Disponible: $%,.2f%n", getAvailableCredit());
        } else {
            System.out.printf("Saldo    : $%,.2f%n", balance);
        }
        System.out.println("Estado   : " + (active ? "Activa" : "BLOQUEADA"));
        System.out.println("=============================");
    }
}
