package com.miplata;

import com.miplata.view.BankApp;

/**
 * Clase principal del Sistema Bancario "Mi Plata".
 * Punto de entrada único — delega todo a BankApp (view).
 *
 * Nueva arquitectura de paquetes:
 *   config        → Constantes y configuración global
 *   domain        → Entidades: User, Transaction, TransactionType
 *   repository    → Acceso a datos en memoria: UserRepository
 *   service       → Lógica de negocio: UserService, AccountService
 *   userinterface → Menús por consola: MenuService
 *   util          → Helpers: ConsoleUtil
 *   view          → Punto de arranque visual: BankApp
 */
public class Main {
    public static void main(String[] args) {
        BankApp.launch();
    }
}