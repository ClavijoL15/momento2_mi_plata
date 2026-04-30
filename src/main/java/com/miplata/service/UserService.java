package com.miplata.service;

import com.miplata.model.Transaction;
import com.miplata.model.TransactionType;
import com.miplata.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que gestiona todos los usuarios del sistema bancario "Mi Plata".
 * Centraliza el registro, autenticación, búsqueda y eliminación de usuarios.
 */
public class UserService {

    // ===================== ATRIBUTOS =====================

    private List<User> users;          // Lista de todos los usuarios registrados
    private int nextUserId;            // Contador auto-incremental de IDs
    private User currentUser;          // Usuario actualmente en sesión

    // ===================== CONSTRUCTOR =====================

    public UserService() {
        this.users = new ArrayList<>();
        this.nextUserId = 1;
        this.currentUser = null;
    }

    // ===================== GETTERS Y SETTERS =====================

    public List<User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    // ===================== MÉTODOS DE REGISTRO =====================

    /**
     * Registra un nuevo usuario en el sistema.
     * @param username Nombre de usuario (único)
     * @param email    Correo electrónico
     * @param password Contraseña
     * @param balance  Saldo inicial
     * @return true si el registro fue exitoso, false si el usuario ya existe
     */
    public boolean registerUser(String username, String email, String password, double balance) {
        // Validar que el usuario no exista
        if (findUserByUsername(username) != null) {
            System.out.println("⚠ Error: El usuario '" + username + "' ya está registrado.");
            return false;
        }
        // Validar que el saldo inicial no sea negativo
        if (balance < 0) {
            System.out.println("⚠ Error: El saldo inicial no puede ser negativo.");
            return false;
        }
        // Crear y agregar el nuevo usuario
        User newUser = new User(nextUserId++, username, email, password, balance);
        users.add(newUser);
        System.out.println("✔ Usuario '" + username + "' registrado exitosamente.");
        return true;
    }

    // ===================== MÉTODOS DE AUTENTICACIÓN =====================

    /**
     * Intenta iniciar sesión con las credenciales dadas.
     * Controla máximo 3 intentos fallidos antes de bloquear la cuenta.
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return true si las credenciales son correctas y la cuenta está activa
     */
    public boolean login(String username, String password) {
        User user = findUserByUsername(username);

        if (user == null) {
            System.out.println("⚠ Usuario no encontrado.");
            return false;
        }

        if (!user.isActive()) {
            System.out.println("🔒 Cuenta bloqueada por 24 horas. Comunícate con tu banco.");
            return false;
        }

        if (!user.getPassword().equals(password)) {
            user.incrementLoginAttempts();
            int remaining = 3 - user.getLoginAttempts();

            if (!user.isActive()) {
                System.out.println("🔒 Cuenta bloqueada por 24 horas. Comunícate con tu banco.");
            } else {
                System.out.println("⚠ Clave incorrecta. Intentos restantes: " + remaining);
            }
            return false;
        }

        // Login exitoso
        user.resetLoginAttempts();
        this.currentUser = user;
        System.out.println("✔ Bienvenido, " + user.getUsername() + "!");
        return true;
    }

    /** Cierra la sesión del usuario actual. */
    public void logout() {
        if (currentUser != null) {
            System.out.println("✔ Sesión cerrada. ¡Hasta pronto, " + currentUser.getUsername() + "!");
            currentUser = null;
        }
    }

    // ===================== MÉTODOS DE BÚSQUEDA =====================

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username Nombre a buscar
     * @return El usuario encontrado o null si no existe
     */
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID a buscar
     * @return El usuario encontrado o null si no existe
     */
    public User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    // ===================== MÉTODOS DE ELIMINACIÓN =====================

    /**
     * Elimina un usuario del sistema por su nombre de usuario.
     * @param username Nombre del usuario a eliminar
     * @return true si fue eliminado, false si no se encontró
     */
    public boolean deleteUser(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("⚠ Usuario '" + username + "' no encontrado.");
            return false;
        }
        users.remove(user);
        System.out.println("✔ Usuario '" + username + "' eliminado del sistema.");
        return true;
    }

    /**
     * Elimina un usuario del sistema por su ID.
     * @param id ID del usuario a eliminar
     * @return true si fue eliminado, false si no se encontró
     */
    public boolean deleteUserById(int id) {
        User user = findUserById(id);
        if (user == null) {
            System.out.println("⚠ Usuario con ID " + id + " no encontrado.");
            return false;
        }
        users.remove(user);
        System.out.println("✔ Usuario con ID " + id + " eliminado del sistema.");
        return true;
    }

    // ===================== MÉTODOS DE ADMINISTRACIÓN =====================

    /** Muestra todos los usuarios registrados en el sistema. */
    public void printAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("====== LISTA DE USUARIOS ======");
        for (User user : users) {
            user.printInformation();
        }
    }

    /** Retorna la cantidad total de usuarios registrados. */
    public int getUserCount() {
        return users.size();
    }
}
