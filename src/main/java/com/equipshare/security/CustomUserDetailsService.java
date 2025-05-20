package com.equipshare.security;

import com.equipshare.model.User;
import com.equipshare.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username)
                .flatMap(u -> {
                    if (Boolean.TRUE.equals(u.getIsOwner()) || Boolean.TRUE.equals(u.getIsBorrower())) {
                        return Optional.of(u);
                    }
                    return Optional.empty();
                });

        return user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or invalid role"));
    }
}


