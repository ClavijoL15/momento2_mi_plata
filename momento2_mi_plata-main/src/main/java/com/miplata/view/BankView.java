package com.miplata.view;

import com.miplata.config.AppConfig;
import com.miplata.domain.AccountType;
import com.miplata.repository.UserRepository;
import com.miplata.service.AccountService;
import com.miplata.service.UserService;
import com.miplata.userinterface.MenuService;

/**
 * Punto de entrada visual de la aplicación.
 * PR4: Datos de prueba incluyen los 3 tipos de cuenta.
 *      Probar cambio de contraseña con cualquier usuario.
 */
public class BankApp {

    public static void launch() {
        System.out.println("══════════════════════════════════");
        System.out.println("  Bienvenido a " + AppConfig.BANK_NAME + " v" + AppConfig.BANK_VERSION);
        System.out.println("══════════════════════════════════\n");

        UserRepository userRepository = new UserRepository();
        UserService    userService    = new UserService(userRepository);
        AccountService accountService = new AccountService(userService);
        MenuService    menuService    = new MenuService(userService, accountService);

        loadTestData(userService);
        menuService.showMainMenu();
    }

    private static void loadTestData(UserService userService) {
        System.out.println("== Datos de prueba ==");
        userService.registerUser("juan123",  "juan@email.com",  "1234", 500000,  AccountType.AHORROS);
        userService.registerUser("maria99",  "maria@email.com", "abcd", 100000,  AccountType.CORRIENTE);
        userService.registerUser("carlos55", "carlos@email.com","5678", 3000000, AccountType.TARJETA_CREDITO);
        System.out.println("  juan123  / 1234  → Ahorros       $500.000");
        System.out.println("  maria99  / abcd  → Corriente     $100.000");
        System.out.println("  carlos55 / 5678  → T. Crédito    Cupo $3.000.000");
        System.out.println("  [Opción 6 en el menú para cambiar contraseña]");
        System.out.println();
    }
}
