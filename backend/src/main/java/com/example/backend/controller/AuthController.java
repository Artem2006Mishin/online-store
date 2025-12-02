package com.example.backend.controller;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.model.User;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    // обработка HTTP запросов
    // - регистрация новых пользователей
    // - логин существующих пользователей
    // - выход

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        User.Role role = User.Role.ROLE_USER;
        if(request.getRole() != null) {
            try {
                role = User.Role.valueOf("ROLE_" + request.getRole().toUpperCase());
            } catch (IllegalArgumentException e){
                role = User.Role.ROLE_USER;
            }
        }

        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                role
        );

        User savedUser = userService.registerUser(user);
        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(
                jwtToken,
                savedUser.getUsername(),
                savedUser.getRole().name()
        ));
    }

    @PostMapping("/register-moderator")
    public ResponseEntity<AuthResponse> registerModerator(@RequestBody RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                User.Role.ROLE_MODERATOR
        );

        User savedUser = userService.registerUser(user);
        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(
                jwtToken,
                savedUser.getUsername(),
                savedUser.getRole().name()
        ));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                User.Role.ROLE_ADMIN
        );

        User savedUser = userService.registerUser(user);
        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(
                jwtToken,
                savedUser.getUsername(),
                savedUser.getRole().name()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if(authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(
                    jwtToken,
                    user.getUsername(),
                    user.getRole().name()
            ));
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
}
