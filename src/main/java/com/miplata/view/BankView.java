package com.miplata.view;

import com.miplata.config.AppConfig;
import com.miplata.repository.UserRepository;
import com.miplata.service.AccountService;
import com.miplata.service.UserService;
import com.miplata.userinterface.MenuService;

/**
 * Punto de entrada visual de la aplicación.
 * Clase 'view' que inicializa todos los componentes y arranca el menú.
 */
public class BankApp {

    public static void launch() {
        System.out.println("══════════════════════════════════");
        System.out.println("  Bienvenido a " + AppConfig.BANK_NAME + " v" + AppConfig.BANK_VERSION);
        System.out.println("══════════════════════════════════\n");

        // Inicializar capas
        UserRepository userRepository = new UserRepository();
        UserService    userService    = new UserService(userRepository);
        AccountService accountService = new AccountService(userService);
        MenuService    menuService    = new MenuService(userService, accountService);

        // Datos de prueba
        loadTestData(userService);

        // Lanzar interfaz
        menuService.showMainMenu();
    }

    private static void loadTestData(UserService userService) {
        System.out.println("== Cargando datos de prueba ==");
        userService.registerUser("juan123", "juan@email.com", "1234", 500000);
        userService.registerUser("maria99", "maria@email.com", "abcd", 250000);
        System.out.println("== Usa 'juan123' / '1234' para probar ==\n");
    }
}