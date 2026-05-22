package com.miplata.service;

import com.miplata.config.AppConfig;
import com.miplata.domain.AccountType;
import com.miplata.domain.Transaction;
import com.miplata.domain.TransactionType;
import com.miplata.domain.User;

import java.util.List;

/**
 * Servicio de operaciones bancarias.
 * PR3: El retiro en CUENTA CORRIENTE permite un sobregiro del 20% del saldo base.
 *
 * Ejemplo: saldo $100.000 → puede retirar hasta $120.000 (saldo queda en -$20.000).
 * La deuda de sobregiro se cobra con interés en la próxima consignación.
 */
public class AccountService {

    // ===================== ATRIBUTOS =====================
    private final UserService userService;
    private int nextTransactionId;

    // ===================== CONSTRUCTOR =====================
    public AccountService(UserService userService) {
        this.userService       = userService;
        this.nextTransactionId = 1;
    }

    // ===================== CONSULTAR SALDO =====================

    public void consultBalance() {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return; }
        System.out.println("========== SALDO ==========");
        System.out.println("Usuario : " + user.getUsername());
        System.out.println("Cuenta  : " + user.getAccountType().getLabel());
        if (user.getAccountType() == AccountType.TARJETA_CREDITO) {
            System.out.printf("Cupo total : $%,.2f%n", user.getCreditLimit());
            System.out.printf("Deuda      : $%,.2f%n", user.getDebt());
            System.out.printf("Disponible : $%,.2f%n", user.getAvailableCredit());
        } else {
            System.out.printf("Saldo    : $%,.2f%n", user.getBalance());
            if (user.getAccountType() == AccountType.CORRIENTE) {
                System.out.printf("Sobregiro máx.: $%,.2f%n", calcMaxOverdraft(user));
                System.out.printf("Límite retiro : $%,.2f%n", calcMaxWithdrawal(user));
            }
        }
        System.out.println("===========================");
    }

    public double getBalance() {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return -1; }
        return user.getAccountType() == AccountType.TARJETA_CREDITO
                ? user.getAvailableCredit() : user.getBalance();
    }

    public double getBalanceByUser(String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) { System.out.println("⚠ Usuario no encontrado."); return -1; }
        return user.getBalance();
    }

    // ===================== RETIRAR =====================

    public boolean withdraw(double amount) {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return false; }
        if (amount <= 0)  { System.out.println("⚠ El monto debe ser positivo."); return false; }

        if (user.getAccountType() == AccountType.TARJETA_CREDITO) {
            return withdrawCreditCard(user, amount);
        } else if (user.getAccountType() == AccountType.CORRIENTE) {
            return withdrawCorriente(user, amount);  // ← PR3: con sobregiro
        } else {
            return withdrawAhorros(user, amount);
        }
    }

    /** Retiro estándar para AHORROS — sin sobregiro. */
    private boolean withdrawAhorros(User user, double amount) {
        if (amount > user.getBalance()) {
            System.out.printf("⚠ Saldo insuficiente. Disponible: $%,.2f%n", user.getBalance());
            return false;
        }
        double newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);
        user.addTransaction(new Transaction(nextTransactionId++, TransactionType.RETIRO, amount, newBalance));
        System.out.println("✔ Retiro exitoso.");
        System.out.printf("   Monto retirado : $%,.2f%n", amount);
        System.out.printf("   Nuevo saldo     : $%,.2f%n", newBalance);
        return true;
    }

    /**
     * Retiro para CUENTA CORRIENTE con sobregiro del 20%.
     * Si el saldo es $100.000, el límite de retiro es $120.000.
     * Si el saldo queda negativo, se indica la deuda de sobregiro.
     */
    private boolean withdrawCorriente(User user, double amount) {
        double maxWithdrawal = calcMaxWithdrawal(user);

        if (amount > maxWithdrawal) {
            System.out.printf("⚠ Excede el límite de retiro con sobregiro.%n");
            System.out.printf("   Saldo actual     : $%,.2f%n", user.getBalance());
            System.out.printf("   Sobregiro 20%%    : $%,.2f%n", calcMaxOverdraft(user));
            System.out.printf("   Límite de retiro : $%,.2f%n", maxWithdrawal);
            return false;
        }

        double newBalance = user.getBalance() - amount;
        user.setBalance(newBalance);

        String desc = newBalance < 0
                ? "Retiro c/sobregiro"
                : TransactionType.RETIRO.getLabel();

        user.addTransaction(new Transaction(nextTransactionId++, TransactionType.RETIRO, amount, newBalance, desc));

        System.out.println("✔ Retiro exitoso.");
        System.out.printf("   Monto retirado : $%,.2f%n", amount);
        System.out.printf("   Nuevo saldo     : $%,.2f%n", newBalance);
        if (newBalance < 0) {
            System.out.printf("   ⚠ Tienes un sobregiro de: $%,.2f%n", Math.abs(newBalance));
            System.out.println("   Recuerda cubrir el saldo negativo en tu próxima consignación.");
        }
        return true;
    }

    /** Compra con tarjeta de crédito. */
    private boolean withdrawCreditCard(User user, double amount) {
        if (amount > user.getAvailableCredit()) {
            System.out.printf("⚠ Cupo insuficiente. Disponible: $%,.2f%n", user.getAvailableCredit());
            return false;
        }
        user.setDebt(user.getDebt() + amount);
        user.addTransaction(new Transaction(nextTransactionId++, TransactionType.RETIRO,
                amount, user.getAvailableCredit(), "Compra con tarjeta"));
        System.out.println("✔ Compra con tarjeta registrada.");
        System.out.printf("   Monto     : $%,.2f%n", amount);
        System.out.printf("   Deuda     : $%,.2f%n", user.getDebt());
        System.out.printf("   Disponible: $%,.2f%n", user.getAvailableCredit());
        return true;
    }

    // ===================== CONSIGNAR =====================

    public boolean deposit(double amount) {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return false; }
        if (amount <= 0)  { System.out.println("⚠ El monto debe ser positivo."); return false; }

        if (user.getAccountType() == AccountType.TARJETA_CREDITO) {
            double payment = Math.min(amount, user.getDebt());
            user.setDebt(user.getDebt() - payment);
            user.addTransaction(new Transaction(nextTransactionId++, TransactionType.CONSIGNACION,
                    payment, user.getAvailableCredit(), "Pago de tarjeta"));
            System.out.println("✔ Pago de tarjeta exitoso.");
            System.out.printf("   Pago: $%,.2f  |  Deuda restante: $%,.2f%n", payment, user.getDebt());
        } else {
            double newBalance = user.getBalance() + amount;
            user.setBalance(newBalance);
            String desc = newBalance >= 0 && (newBalance - amount) < 0
                    ? "Pago de sobregiro"
                    : TransactionType.CONSIGNACION.getLabel();
            user.addTransaction(new Transaction(nextTransactionId++, TransactionType.CONSIGNACION, amount, newBalance, desc));
            System.out.println("✔ Consignación exitosa.");
            System.out.printf("   Monto: $%,.2f  |  Nuevo saldo: $%,.2f%n", amount, newBalance);
            if (newBalance < 0) {
                System.out.printf("   ⚠ Aún tienes sobregiro de: $%,.2f%n", Math.abs(newBalance));
            }
        }
        return true;
    }

    // ===================== TRANSFERIR =====================

    public boolean transfer(String targetUsername, double amount) {
        User sender   = userService.getCurrentUser();
        User receiver = userService.findUserByUsername(targetUsername);
        if (sender == null)   { System.out.println("⚠ No hay sesión activa."); return false; }
        if (receiver == null) { System.out.println("⚠ Usuario destino no encontrado."); return false; }
        if (sender.getUsername().equalsIgnoreCase(targetUsername)) {
            System.out.println("⚠ No puedes transferirte a ti mismo."); return false; }
        if (sender.getAccountType() == AccountType.TARJETA_CREDITO) {
            System.out.println("⚠ No se pueden hacer transferencias desde una tarjeta de crédito."); return false; }
        if (amount <= 0) { System.out.println("⚠ El monto debe ser positivo."); return false; }

        // Para CORRIENTE: permite transferir con sobregiro también
        double limit = sender.getAccountType() == AccountType.CORRIENTE
                ? calcMaxWithdrawal(sender) : sender.getBalance();

        if (amount > limit) {
            System.out.printf("⚠ Saldo/límite insuficiente. Máximo disponible: $%,.2f%n", limit);
            return false;
        }

        double senderNew   = sender.getBalance()  - amount;
        double receiverNew = receiver.getBalance() + amount;
        sender.setBalance(senderNew);
        receiver.setBalance(receiverNew);
        sender.addTransaction(new Transaction(nextTransactionId++, TransactionType.TRANSFERENCIA_ENVIADA,
                amount, senderNew, "Transferencia a " + receiver.getUsername(), receiver.getUsername()));
        receiver.addTransaction(new Transaction(nextTransactionId++, TransactionType.TRANSFERENCIA_RECIBIDA,
                amount, receiverNew, "Transferencia de " + sender.getUsername(), sender.getUsername()));
        System.out.println("✔ Transferencia exitosa.");
        System.out.printf("   A: %s  |  Monto: $%,.2f  |  Tu saldo: $%,.2f%n",
                receiver.getUsername(), amount, senderNew);
        if (senderNew < 0) System.out.printf("   ⚠ Sobregiro activo: $%,.2f%n", Math.abs(senderNew));
        return true;
    }

    // ===================== HISTORIAL =====================

    public void consultTransactions() {
        User user = userService.getCurrentUser();
        if (user == null) { System.out.println("⚠ No hay sesión activa."); return; }
        List<Transaction> txs = user.getTransactions();
        System.out.println("============================================================");
        System.out.println("             MOVIMIENTOS - " + user.getUsername());
        System.out.println("============================================================");
        if (txs.isEmpty()) {
            System.out.println("  No hay movimientos registrados.");
        } else {
            System.out.printf("| %-19s | %-18s | %12s | %12s |%n", "Fecha y Hora","Concepto","Valor","Saldo");
            System.out.println("|---------------------|--------------------|--------------|--------------| ");
            txs.forEach(Transaction::printInformation);
        }
        System.out.println("============================================================");
        if (user.getAccountType() != AccountType.TARJETA_CREDITO) {
            System.out.printf("Saldo actual: $%,.2f%n", user.getBalance());
            if (user.getAccountType() == AccountType.CORRIENTE && user.getBalance() < 0)
                System.out.printf("⚠ Sobregiro activo: $%,.2f%n", Math.abs(user.getBalance()));
        }
    }

    public List<Transaction> getTransactionHistory() {
        User user = userService.getCurrentUser();
        return user == null ? null : user.getTransactions();
    }

    // ===================== HELPERS SOBREGIRO =====================

    /** Calcula el sobregiro máximo permitido (20% del saldo BASE, si saldo > 0). */
    private double calcMaxOverdraft(User user) {
        double base = Math.max(user.getBalance(), 0);
        return base * AppConfig.OVERDRAFT_PERCENTAGE;
    }

    /** Calcula el máximo que se puede retirar incluyendo sobregiro. */
    private double calcMaxWithdrawal(User user) {
        return user.getBalance() + calcMaxOverdraft(user);
    }
}
