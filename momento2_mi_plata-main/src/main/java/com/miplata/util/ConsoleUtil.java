package com.miplata.util;

/**
 * Clase utilitaria para formatear la salida por consola.
 * Centraliza helpers de impresión reutilizables en toda la app.
 */
public class ConsoleUtil {

    private ConsoleUtil() {}

    /** Imprime un encabezado con título centrado entre líneas. */
    public static void printHeader(String title) {
        System.out.println("\n=============================");
        System.out.println("  " + title);
        System.out.println("=============================");
    }

    /** Imprime un separador simple. */
    public static void printSeparator() {
        System.out.println("-----------------------------");
    }

    /** Imprime una línea larga. */
    public static void printLongSeparator() {
        System.out.println("============================================================");
    }

    /** Imprime un mensaje de éxito con prefijo ✔. */
    public static void printSuccess(String message) {
        System.out.println("✔ " + message);
    }

    /** Imprime un mensaje de advertencia con prefijo ⚠. */
    public static void printWarning(String message) {
        System.out.println("⚠ " + message);
    }

    /** Imprime un mensaje de error con prefijo ✖. */
    public static void printError(String message) {
        System.out.println("✖ " + message);
    }
}
