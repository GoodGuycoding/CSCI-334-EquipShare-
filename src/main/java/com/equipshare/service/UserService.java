package com.equipshare.service;

import com.equipshare.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import com.equipshare.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
