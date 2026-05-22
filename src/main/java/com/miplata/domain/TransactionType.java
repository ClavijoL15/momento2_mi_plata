package com.miplata.domain;

public enum TransactionType {

    CONSIGNACION("Consignacion"),
    RETIRO("Retiro"),
    TRANSFERENCIA_ENVIADA("Transferencia enviada"),
    TRANSFERENCIA_RECIBIDA("Tranferencia recibida");

    private final String label;

    TransactionType (String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }


}
