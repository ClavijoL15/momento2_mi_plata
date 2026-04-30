package com.miplata.domain;

import com.miplata.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class User {

    /**
     * Clase que representa un usuario/cliente del banco "Mi Plata".
     * Movida al paquete 'domain' como parte de la reestructuración de paquetes.
     */

        // ===================== ATRIBUTOS =====================
        private int id;
        private String username;
        private String email;
        private String password;
        private double balance;
        private boolean active;
        private int loginAttempts;
        private List<Transaction> transactions;

        // ===================== CONSTRUCTORES =====================

        public User() {
            this.transactions  = new ArrayList<>();
            this.active        = true;
            this.loginAttempts = 0;
        }

        public User(int id, String username, String email, String password, double balance) {
            this.id            = id;
            this.username      = username;
            this.email         = email;
            this.password      = password;
            this.balance       = balance;
            this.active        = true;
            this.loginAttempts = 0;
            this.transactions  = new ArrayList<>();
        }

        // ===================== GETTERS Y SETTERS =====================

        public int getId()                          { return id; }
        public void setId(int id)                   { this.id = id; }

        public String getUsername()                 { return username; }
        public void setUsername(String username)    { this.username = username; }

        public String getEmail()                    { return email; }
        public void setEmail(String email)          { this.email = email; }

        public String getPassword()                 { return password; }
        public void setPassword(String password)    { this.password = password; }

        public double getBalance()                  { return balance; }
        public void setBalance(double balance)      { this.balance = balance; }

        public boolean isActive()                   { return active; }
        public void setActive(boolean active)       { this.active = active; }

        public int getLoginAttempts()               { return loginAttempts; }
        public void setLoginAttempts(int loginAttempts) { this.loginAttempts = loginAttempts; }

        public List<Transaction> getTransactions()  { return transactions; }
        public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

        // ===================== MÉTODOS DE NEGOCIO =====================

        public void incrementLoginAttempts() {
            this.loginAttempts++;
            if (this.loginAttempts >= AppConfig.MAX_ATTEMPTS) {
                this.active = false;
            }
        }

        public void resetLoginAttempts() {
            this.loginAttempts = 0;
        }

        public void addTransaction(Transaction transaction) {
            this.transactions.add(transaction);
        }

        public void printInformation() {
            System.out.println("========== USUARIO ==========");
            System.out.println("ID       : " + id);
            System.out.println("Usuario  : " + username);
            System.out.println("Email    : " + email);
            System.out.printf ("Saldo    : $%,.2f%n", balance);
            System.out.println("Estado   : " + (active ? "Activa" : "BLOQUEADA"));
            System.out.println("Intentos : " + loginAttempts + "/" + AppConfig.MAX_ATTEMPTS);
            System.out.println("=============================");
        }
    }
