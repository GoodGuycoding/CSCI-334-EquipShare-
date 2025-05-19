package com.equipshare.service;

import com.equipshare.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import com.equipshare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder ) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Generate UUID and set as ID if not already set
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        userRepository.save(user);
    }

    public Optional<User> authenticateUser(String email, String password, String role) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                boolean isBorrower = "borrower".equals(role) && user.getIsBorrower();
                boolean isOwner = "owner".equals(role) && user.getIsOwner(); // <-- change here
                if (isBorrower || isOwner) {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

}

