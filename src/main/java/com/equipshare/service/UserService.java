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

        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // kim just made some changes into this method it wasn't working on my system
    // added some print statements to debug the code, but it is working now
    public Optional<User> authenticateUser(String email, String password, String role) {
        System.out.println(" Authenticating user: " + email + " with role: " + role);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(" User found: " + user.getEmail());

            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            System.out.println(" Password match: " + passwordMatches);

            if (passwordMatches) {
                boolean isBorrower = "borrower".equalsIgnoreCase(role) && Boolean.TRUE.equals(user.getIsBorrower());
                boolean isOwner = "owner".equalsIgnoreCase(role) && Boolean.TRUE.equals(user.getIsOwner());

                System.out.println(" Role check — isBorrower: " + isBorrower + ", isOwner: " + isOwner);

                if (isBorrower || isOwner) {
                    System.out.println(" Authentication successful");
                    return Optional.of(user);
                } else {
                    System.out.println(" Role mismatch");
                }
            } else {
                System.out.println(" Password incorrect");
            }
        } else {
            System.out.println(" User not found with email: " + email);
        }

        return Optional.empty();
    }


    public Optional<User> getUserByEmail(String email) {
        return email == null ? Optional.empty() : userRepository.findByEmail(email);
    }

    // updating users, called in BorrowerProflieController
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
       // existingUser.setContact(user.getContact());
       // existingUser.setAddress(user.getAddress());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
    }
}

