package com.tworres.todolist.service;

import com.tworres.todolist.model.User;

import java.util.Optional;

public interface UserService {
    
    /**
     * Register a new user
     * @param user the user to register
     * @return the registered user
     * @throws RuntimeException if a user with the same email already exists
     */
    User registerUser(User user);
    
    /**
     * Find a user by email
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if the provided password matches the user's password
     * @param rawPassword the raw password to check
     * @param encodedPassword the encoded password to compare against
     * @return true if the passwords match, false otherwise
     */
    boolean checkPassword(String rawPassword, String encodedPassword);
}