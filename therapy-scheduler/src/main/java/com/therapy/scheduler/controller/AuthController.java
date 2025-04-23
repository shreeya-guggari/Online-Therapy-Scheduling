package com.therapy.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.therapy.scheduler.service.AuthService;
import com.therapy.scheduler.model.User;
import com.therapy.scheduler.model.Role;
import com.therapy.scheduler.service.PrescriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class AuthController {

    private final AuthService authService;
    private final PrescriptionService prescriptionService;

    public AuthController(AuthService authService, PrescriptionService prescriptionService) {
        this.authService = authService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam String role, Model model, HttpServletRequest request) {
        System.out.println("Starting registration for user: " + user.getUsername() + ", role: " + role);
        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
            System.out.println("Role set to: " + user.getRole());
            user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            System.out.println("CreatedAt set to: " + user.getCreatedAt());
            authService.register(user.getUsername(), user.getPassword(), user.getEmail(), role);
            System.out.println("User registered successfully, redirecting to /login");
            request.getSession().invalidate();
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Registration failed: " + e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        System.out.println("Handling GET request for /login");
        return "login";
    }

    @GetMapping("/therapist/home")
    public String therapistHome() {
        return "therapist-home";
    }

    @GetMapping("/patient/home")
    public String patientHome() {
        return "patient-home";
    }

    @GetMapping("/therapist/notes")
    public String showTherapistNotes() {
        return "therapist-notes";
    }

    @PostMapping("/therapist/notes")
    public String uploadNotes(@RequestParam String patientUsername, @RequestParam String notes,
                             @RequestParam("prescription") MultipartFile prescription, Model model) {
        try {
            prescriptionService.saveNotesAndPrescription(patientUsername, notes, prescription);
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Upload failed: " + e.getMessage());
            return "therapist-notes";
        }
    }

    @GetMapping("/patient/prescriptions")
    public String viewPrescriptions(Model model) {
        // Get the current logged-in user's username
        String currentUsername = getCurrentUsername();
        model.addAttribute("prescriptions", prescriptionService.getPrescriptionsForUser(currentUsername));
        return "patient-prescriptions";
    }

    @GetMapping("/therapist/appointments")
    public String showTherapistAppointments() {
        return "therapist-appointments";
    }

    @PostMapping("/therapist/appointments")
    public String scheduleTherapistAppointment(@RequestParam String patientUsername,
                                              @RequestParam String dateTime,
                                              @RequestParam int duration, Model model) {
        try {
            // Stub: Add appointment scheduling logic (e.g., save to database)
            System.out.println("Scheduling appointment for patient: " + patientUsername + ", at: " + dateTime + ", duration: " + duration);
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Scheduling failed: " + e.getMessage());
            return "therapist-appointments";
        }
    }

    @GetMapping("/patient/appointments")
    public String showPatientAppointments() {
        return "patient-appointments";
    }

    @PostMapping("/patient/appointments")
    public String schedulePatientAppointment(@RequestParam String therapistUsername,
                                            @RequestParam String dateTime, Model model) {
        try {
            // Stub: Add appointment scheduling logic (e.g., save to database)
            System.out.println("Scheduling appointment with therapist: " + therapistUsername + ", at: " + dateTime);
            return "redirect:/patient/home";
        } catch (Exception e) {
            model.addAttribute("error", "Scheduling failed: " + e.getMessage());
            return "patient-appointments";
        }
    }

    // Helper method to get the current logged-in user's username
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}