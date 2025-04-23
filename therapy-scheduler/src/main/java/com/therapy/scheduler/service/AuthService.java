package com.therapy.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.therapy.scheduler.model.User;
import com.therapy.scheduler.model.Role;
import com.therapy.scheduler.repository.UserRepository;
import java.sql.Timestamp;
import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String username, String password, String email, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.valueOf(role.toUpperCase()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        System.out.println("Registering user: " + user.getUsername() + ", role: " + user.getRole());
        userRepository.save(user);
        System.out.println("User saved successfully: " + user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(() -> "ROLE_" + user.getRole().name())
        );
    }
}