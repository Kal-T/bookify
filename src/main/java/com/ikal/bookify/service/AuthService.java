package com.ikal.bookify.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ikal.bookify.dto.JwtResponse;
import com.ikal.bookify.dto.RegisterRequest;
import com.ikal.bookify.exception.UserNotFoundException;
import com.ikal.bookify.model.User;
import com.ikal.bookify.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public JwtResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (encoder.matches(password, user.getPasswordHash())) {
            try {
                return new JwtResponse(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        User user = new User(registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()), registerRequest.getUsername(), registerRequest.getCountry());
        userRepository.save(user);
    }

    public Optional<JwtResponse> refreshToken(String authHeader) throws IOException {
        final String refreshToken;
        final String userEmail;

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);
                return Optional.of(jwtResponse);
            }
        }

        return Optional.empty();
    }
}
