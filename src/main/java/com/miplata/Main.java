package com.miplata;

import com.miplata.service.AccountService;
import com.miplata.service.MenuService;
import com.miplata.service.UserService;

/**
 * Clase principal del Sistema Bancario "Mi Plata".
 *
 * Punto de entrada de la aplicación. Inicializa los servicios
 * y lanza el menú principal por consola.
 *
 * Arquitectura:
 *   Main
 *    └── MenuService      → Interfaz de usuario (prompt/consola)
 *         ├── UserService  → Registro, login, búsqueda y eliminación de usuarios
 *         └── AccountService → Saldo, retiros, consignaciones, transferencias
 */
public class Main {

    public static void main(String[] args) {

        // Inicializar servicios
        UserService userService = new UserService();
        AccountService accountService = new AccountService(userService);
        MenuService menuService = new MenuService(userService, accountService);

        // Datos de prueba (opcional - comentar si no se desean)
        loadTestData(userService);

        // Lanzar la aplicación
        menuService.showMainMenu();
    }

    /**
     * Carga datos de prueba para facilitar el testing.
     * Puedes modificar o eliminar este método.
     */
    private static void loadTestData(UserService userService) {
        System.out.println("== Cargando datos de prueba ==");
        userService.registerUser("juan123", "juan@email.com", "1234", 500000);
        userService.registerUser("maria99", "maria@email.com", "abcd", 250000);
        System.out.println("== Datos cargados. Usa 'juan123' / '1234' para probar ==\n");
    }
}
