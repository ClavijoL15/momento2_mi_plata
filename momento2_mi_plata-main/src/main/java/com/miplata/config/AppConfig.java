package com.miplata.config;

public class AppConfig {

    public static final String BANK_NAME           = "Mi Plata";
    public static final String BANK_VERSION        = "1.0.0";
    public static final int MAX_ATTEMPTS = 3;
    public static final double DEFAULT_BALANCE      = 0.0;

    // --- NUEVO PR3 ---
    /** Porcentaje del saldo que se puede usar como sobregiro en cuenta corriente (20%). */
    public static final double OVERDRAFT_PERCENTAGE = 0.20;

    private AppConfig() {}
}
