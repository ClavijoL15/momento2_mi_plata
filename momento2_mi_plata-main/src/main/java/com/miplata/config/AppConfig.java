package com.miplata.config;

/**
 * Clase de configuración global de la aplicación.
 * Centraliza todas las constantes y parámetros configurables del sistema.
 */
public class AppConfig {

    // ===================== CONFIGURACIÓN GENERAL =====================
    public static final String BANK_NAME        = "Mi Plata";
    public static final String BANK_VERSION     = "1.0.0";

    // ===================== CONFIGURACIÓN DE SEGURIDAD =====================
    public static final int MAX_LOGIN_ATTEMPTS  = 3;

    // ===================== CONFIGURACIÓN DE CUENTA =====================
    public static final double DEFAULT_BALANCE  = 0.0;

    // Constructor privado - clase utilitaria
    private AppConfig() {}
}
