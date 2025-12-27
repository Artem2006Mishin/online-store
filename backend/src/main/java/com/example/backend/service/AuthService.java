package com.example.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.model.User;
import com.example.backend.dto.RegisterDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto login(UserDto userDto) {
        try {
            // проверка логина/пароля Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getEmail(),
                            userDto.getPassword()));

            // если сюда дошли — креды верные
            User user = userRepository.findByEmail(userDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found: " + userDto.getEmail()));

            // увеличиваем счётчик входов
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);

            String token = jwtService.generateToken(userDto.getEmail());
            return new UserResponseDto(userDto.getEmail(), token);

        } catch (BadCredentialsException e) {
            // важно пробросить именно BadCredentialsException,
            // контроллер превратит его в 401
            throw e;
        }
    }

    @Transactional
    public UserResponseDto register(RegisterDto registerDto) {
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException(
                    "User with email " + registerDto.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        user.setLoginCount(user.getLoginCount() + 1); // первая авторизация

        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new UserResponseDto(user.getEmail(), token);
    }
}
