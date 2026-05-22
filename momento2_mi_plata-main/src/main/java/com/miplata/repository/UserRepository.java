package com.miplata.repository;

import com.miplata.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de usuarios.
 * Centraliza el acceso y manipulación de la colección de usuarios en memoria.
 * Separa la lógica de almacenamiento de la lógica de negocio (service).
 */
public class UserRepository {

    // ===================== ATRIBUTOS =====================
    private final List<User> users;
    private int nextId;

    // ===================== CONSTRUCTOR =====================
    public UserRepository() {
        this.users  = new ArrayList<>();
        this.nextId = 1;
    }

    // ===================== CRUD =====================

    /** Guarda un nuevo usuario asignando su ID automáticamente. */
    public User save(User user) {
        user.setId(nextId++);
        users.add(user);
        return user;
    }

    /** Retorna todos los usuarios registrados. */
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    /** Busca un usuario por nombre (insensible a mayúsculas). */
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    /** Busca un usuario por ID. */
    public Optional<User> findById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    /** Elimina un usuario por nombre. Retorna true si fue eliminado. */
    public boolean deleteByUsername(String username) {
        return users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
    }

    /** Retorna el total de usuarios registrados. */
    public int count() {
        return users.size();
    }

    /** Verifica si ya existe un usuario con ese nombre. */
    public boolean existsByUsername(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}
