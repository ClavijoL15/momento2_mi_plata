package com.miplata.domain;

/**
 * Enumeración de los tipos de transacciones disponibles en el sistema bancario.
 * Movida al paquete 'domain' como parte de la reestructuración de paquetes.
 */
public enum TransactionType {

    CONSIGNACION("Consignación"),
    RETIRO("Retiro"),
    TRANSFERENCIA_ENVIADA("Transferencia Enviada"),
    TRANSFERENCIA_RECIBIDA("Transferencia Recibida");

    // ===================== ATRIBUTOS =====================
    private final String label;

    // ===================== CONSTRUCTOR =====================
    TransactionType(String label) {
        this.label = label;
    }

    // ===================== GETTER =====================
    public String getLabel() {
        return label;
    }
}
