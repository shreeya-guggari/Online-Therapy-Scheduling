package com.therapy.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.therapy.scheduler.service.AuthService; 
import com.therapy.scheduler.model.User; 
import com.therapy.scheduler.model.Role;


@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Bind an empty User object to the form
        return "register"; // Returns register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam String role, Model model) {
        try {
            user.setRole(Role.valueOf(role.toUpperCase())); // Set role from form
            user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            authService.register(user.getUsername(), user.getPassword(), user.getEmail(), role);
            return "redirect:/login"; // Redirect to login after success
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register"; // Return to form with error
        }
    }
}