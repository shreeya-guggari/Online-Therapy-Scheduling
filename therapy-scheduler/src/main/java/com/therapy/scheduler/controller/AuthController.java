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
    public String register(@ModelAttribute User user, @RequestParam String role, Model model) {
        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
            user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            authService.register(user.getUsername(), user.getPassword(), user.getEmail(), role);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
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
        String currentUsername = "currentUser"; // Replace with actual logged-in user logic (e.g., from SecurityContext)
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
            // Add appointment scheduling logic
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
            // Add appointment scheduling logic
            return "redirect:/patient/home";
        } catch (Exception e) {
            model.addAttribute("error", "Scheduling failed: " + e.getMessage());
            return "patient-appointments";
        }
    }
    
}