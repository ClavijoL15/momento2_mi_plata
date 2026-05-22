package com.miplata.service;

import com.miplata.domain.User;
import com.miplata.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de usuarios.
 * Contiene la lógica de negocio: registro, autenticación y gestión.
 * Delega el almacenamiento al UserRepository.
 */
public class UserService {

    // ===================== ATRIBUTOS =====================
    private final UserRepository userRepository;
    private User currentUser;

    // ===================== CONSTRUCTOR =====================
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser    = null;
    }

    // ===================== GETTERS =====================

    public User getCurrentUser()              { return currentUser; }
    public void setCurrentUser(User user)     { this.currentUser = user; }

    // ===================== REGISTRO =====================

    public boolean registerUser(String username, String email, String password, double balance) {
        if (userRepository.existsByUsername(username)) {
            System.out.println("⚠ Error: El usuario '" + username + "' ya está registrado.");
            return false;
        }
        if (balance < 0) {
            System.out.println("⚠ Error: El saldo inicial no puede ser negativo.");
            return false;
        }
        User newUser = new User(0, username, email, password, balance);
        userRepository.save(newUser);
        System.out.println("✔ Usuario '" + username + "' registrado exitosamente.");
        return true;
    }

    // ===================== AUTENTICACIÓN =====================

    public boolean login(String username, String password) {
        Optional<User> opt = userRepository.findByUsername(username);

        if (opt.isEmpty()) {
            System.out.println("⚠ Usuario no encontrado.");
            return false;
        }

        User user = opt.get();

        if (!user.isActive()) {
            System.out.println("🔒 Cuenta bloqueada. Comunícate con tu banco.");
            return false;
        }

        if (!user.getPassword().equals(password)) {
            user.incrementLoginAttempts();
            if (!user.isActive()) {
                System.out.println("🔒 Cuenta bloqueada. Comunícate con tu banco.");
            } else {
                int remaining = 3 - user.getLoginAttempts();
                System.out.println("⚠ Clave incorrecta. Intentos restantes: " + remaining);
            }
            return false;
        }

        user.resetLoginAttempts();
        this.currentUser = user;
        System.out.println("✔ Bienvenido, " + user.getUsername() + "!");
        return true;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("✔ Sesión cerrada. ¡Hasta pronto, " + currentUser.getUsername() + "!");
            currentUser = null;
        }
    }

    // ===================== BÚSQUEDA =====================

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // ===================== ADMINISTRACIÓN =====================

    public boolean deleteUser(String username) {
        boolean removed = userRepository.deleteByUsername(username);
        if (!removed) System.out.println("⚠ Usuario '" + username + "' no encontrado.");
        else          System.out.println("✔ Usuario '" + username + "' eliminado.");
        return removed;
    }

    public void printAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("====== LISTA DE USUARIOS ======");
        users.forEach(User::printInformation);
    }

    public int getUserCount() {
        return userRepository.count();
    }
}