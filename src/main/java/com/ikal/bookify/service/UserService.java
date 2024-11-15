package com.ikal.bookify.service;

import com.ikal.bookify.dto.JwtResponse;
import com.ikal.bookify.dto.UserInfo;
import com.ikal.bookify.model.User;
import com.ikal.bookify.repository.UserRepository;
import com.ikal.bookify.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            if (encoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(encoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public Optional<UserInfo> getUserProfile(String username) {
        User user = userRepository.findByUserName(username);
        if (user != null) {
            UserInfo userInfo = new UserInfo(user.getUsername(), user.getEmail(), user.getCountry());
            return Optional.of(userInfo);
        }
        return Optional.empty();
    }
}
