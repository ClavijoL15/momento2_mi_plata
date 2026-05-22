package com.miplata.domain;

/**
 * Enumeración de los tipos de cuenta disponibles en Mi Plata.
 *
 *  AHORROS         → Cuenta estándar de ahorros. No permite sobregiro.
 *  CORRIENTE       → Cuenta corriente. Permite sobregiro del 20 % del saldo.
 *  TARJETA_CREDITO → Tarjeta de crédito. Maneja cupo disponible y deuda.
 */
public enum AccountType {

    AHORROS("Cuenta de Ahorros"),
    CORRIENTE("Cuenta Corriente"),
    TARJETA_CREDITO("Tarjeta de Crédito");

    // ===================== ATRIBUTOS =====================
    private final String label;

    // ===================== CONSTRUCTOR =====================
    AccountType(String label) {
        this.label = label;
    }

    // ===================== GETTER =====================
    public String getLabel() {
        return label;
    }
}
