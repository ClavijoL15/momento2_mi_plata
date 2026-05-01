package com.miplata.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una transacción bancaria.
 * Movida al paquete 'domain' como parte de la reestructuración de paquetes.
 */
public class Transaction {

    // ===================== ATRIBUTOS =====================
    private int id;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime dateTime;
    private String description;
    private String targetUser;

    // ===================== CONSTRUCTORES =====================

    public Transaction() {
        this.dateTime = LocalDateTime.now();
    }

    public Transaction(int id, TransactionType type, double amount, double balanceAfter) {
        this.id          = id;
        this.type        = type;
        this.amount      = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime    = LocalDateTime.now();
        this.description = type.getLabel();
    }

    public Transaction(int id, TransactionType type, double amount, double balanceAfter, String description) {
        this.id          = id;
        this.type        = type;
        this.amount      = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime    = LocalDateTime.now();
        this.description = description;
    }

    public Transaction(int id, TransactionType type, double amount, double balanceAfter,
                       String description, String targetUser) {
        this.id          = id;
        this.type        = type;
        this.amount      = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime    = LocalDateTime.now();
        this.description = description;
        this.targetUser  = targetUser;
    }

    // ===================== GETTERS Y SETTERS =====================

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public TransactionType getType()          { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public double getAmount()                 { return amount; }
    public void setAmount(double amount)      { this.amount = amount; }

    public double getBalanceAfter()                   { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter)  { this.balanceAfter = balanceAfter; }

    public LocalDateTime getDateTime()                { return dateTime; }
    public void setDateTime(LocalDateTime dateTime)   { this.dateTime = dateTime; }

    public String getDescription()                    { return description; }
    public void setDescription(String description)    { this.description = description; }

    public String getTargetUser()                     { return targetUser; }
    public void setTargetUser(String targetUser)      { this.targetUser = targetUser; }

    // ===================== MÉTODOS DE NEGOCIO =====================

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public void printInformation() {
        System.out.printf("| %-19s | %-14s | %,12.2f | %,12.2f |%n",
                getFormattedDateTime(), description, amount, balanceAfter);
    }
}

