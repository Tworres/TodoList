package com.tworres.todolist.controller;

import com.tworres.todolist.model.User;
import com.tworres.todolist.security.JwtTokenUtil;
import com.tworres.todolist.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(UserService userService, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            
            // Remove password from response
            registeredUser.setPassword(null);
            
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        // Validate credentials
        return userService.findByEmail(email)
                .filter(user -> userService.checkPassword(password, user.getPassword()))
                .map(user -> {
                    // Generate JWT token
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    String token = jwtTokenUtil.generateToken(userDetails);
                    
                    // Set JWT in cookie
                    Cookie jwtCookie = new Cookie("jwt", token);
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setSecure(true); // Enable in production
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge(24 * 60 * 60); // 1 day
                    jwtCookie.setAttribute("SameSite", "Strict");
                    response.addCookie(jwtCookie);
                    
                    // Return token in response body
                    Map<String, Object> tokenResponse = new HashMap<>();
                    tokenResponse.put("token", token);
                    tokenResponse.put("type", "Bearer");
                    tokenResponse.put("email", email);
                    tokenResponse.put("name", user.getName());
                    
                    return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Invalid email or password");
                    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
                });
    }
}