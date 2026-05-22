package com.miplata.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una transacción bancaria.
 * Registra retiros, consignaciones y transferencias con fecha, hora y saldo resultante.
 */
public class Transaction {

    // ===================== ATRIBUTOS =====================

    private int id;
    private TransactionType type;       // Tipo: RETIRO, CONSIGNACION, TRANSFERENCIA
    private double amount;              // Valor de la transacción
    private double balanceAfter;        // Saldo después de la operación
    private LocalDateTime dateTime;     // Fecha y hora de la transacción
    private String description;         // Descripción opcional
    private String targetUser;          // Usuario destino (solo en transferencias)

    // ===================== CONSTRUCTORES =====================

    /** Constructor vacío */
    public Transaction() {
        this.dateTime = LocalDateTime.now();
    }

    /** Constructor para retiros y consignaciones */
    public Transaction(int id, TransactionType type, double amount, double balanceAfter) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();
        this.description = type.getLabel();
    }

    /** Constructor para transacciones con descripción personalizada */
    public Transaction(int id, TransactionType type, double amount, double balanceAfter, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();
        this.description = description;
    }

    /** Constructor para transferencias entre usuarios */
    public Transaction(int id, TransactionType type, double amount, double balanceAfter, String description, String targetUser) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();
        this.description = description;
        this.targetUser = targetUser;
    }

    // ===================== GETTERS Y SETTERS =====================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    // ===================== MÉTODOS DE NEGOCIO =====================

    /** Retorna la fecha y hora formateada como String. */
    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    /** Imprime la información de la transacción en formato tabla. */
    public void printInformation() {
        System.out.printf("| %-19s | %-14s | %,12.2f | %,12.2f |%n",
                getFormattedDateTime(),
                description,
                amount,
                balanceAfter);
    }
}
