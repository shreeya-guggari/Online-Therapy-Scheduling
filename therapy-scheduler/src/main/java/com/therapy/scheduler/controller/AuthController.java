package com.therapy.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import com.therapy.scheduler.service.AuthService;
import com.therapy.scheduler.model.User;
import com.therapy.scheduler.model.Prescription;
import com.therapy.scheduler.model.Role;
import com.therapy.scheduler.service.PrescriptionService;
import com.therapy.scheduler.service.AppointmentService; // Added
import com.therapy.scheduler.model.Appointment; // Added
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.thymeleaf.exceptions.TemplateInputException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private final AuthService authService;
    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService; // Added

    public AuthController(AuthService authService, PrescriptionService prescriptionService, AppointmentService appointmentService) {
        this.authService = authService;
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
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
    public String showTherapistNotes(Model model) {
        model.addAttribute("sessionId", 123L); // Example sessionId, replace with actual logic
        return "therapist-notes";
    }

    @PostMapping("/therapist/notes")
    public String uploadNotes(
            @RequestParam String patientUsername,
            @RequestParam String notes,
            @RequestParam("prescription") MultipartFile prescription,
            @RequestParam(required = false) Long sessionId,
            Model model) {
        try {
            prescriptionService.saveNotesAndPrescription(patientUsername, notes, prescription, sessionId);
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Upload failed: " + e.getMessage());
            return "therapist-notes";
        }
    }

    @GetMapping("/patient/prescriptions")
    public String viewPrescriptions(Model model) {
        String currentUsername = getCurrentUsername();
        System.out.println("Fetching prescriptions for user: " + currentUsername);
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsForUser(currentUsername);
        if (prescriptions == null) {
            System.out.println("Prescriptions list is null for user: " + currentUsername);
            prescriptions = new ArrayList<Prescription>();
        }
        model.addAttribute("prescriptions", prescriptions);
        System.out.println("Prescriptions retrieved: " + prescriptions.size());
        return "patient-prescriptions";
    }

    @GetMapping("/therapist/appointments")
    public String showTherapistAppointments(Model model) {
        String currentUsername = getCurrentUsername();
        List<Appointment> appointments = appointmentService.getAppointmentsForTherapist(currentUsername);
        model.addAttribute("appointments", appointments);
        model.addAttribute("sessionId", 123L); // Example, replace with actual logic
        return "therapist-appointments";
    }

    @PostMapping("/therapist/appointments")
    public String scheduleTherapistAppointment(
            @RequestParam String patientUsername,
            @RequestParam String dateTime,
            @RequestParam int duration,
            @RequestParam(required = false) Long sessionId,
            Model model) {
        try {
            String therapistUsername = getCurrentUsername();
            System.out.println("Scheduling appointment for patient: " + patientUsername + ", at: " + dateTime + ", duration: " + duration + ", sessionId: " + sessionId);
            appointmentService.scheduleTherapistAppointment(patientUsername, therapistUsername, dateTime, duration, sessionId);
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Scheduling failed: " + e.getMessage());
            return "therapist-appointments";
        }
    }

    @GetMapping("/patient/appointments")
    public String showPatientAppointments(Model model) {
        String currentUsername = getCurrentUsername();
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(currentUsername);
        model.addAttribute("appointments", appointments);
        model.addAttribute("sessionId", 123L); // Example, replace with actual logic
        return "patient-appointments";
    }

    @PostMapping("/patient/appointments")
    public String schedulePatientAppointment(
            @RequestParam String therapistUsername,
            @RequestParam String dateTime,
            @RequestParam(required = false) Long sessionId,
            Model model) {
        try {
            String patientUsername = getCurrentUsername();
            System.out.println("Scheduling appointment with therapist: " + therapistUsername + ", at: " + dateTime + ", sessionId: " + sessionId);
            appointmentService.schedulePatientAppointment(therapistUsername, patientUsername, dateTime, sessionId);
            return "redirect:/patient/home";
        } catch (Exception e) {
            model.addAttribute("error", "Scheduling failed: " + e.getMessage());
            return "patient-appointments";
        }
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @ExceptionHandler(TemplateInputException.class)
    public String handleTemplateInputException(TemplateInputException e, Model model) {
        System.out.println("Template error: " + e.getMessage());
        model.addAttribute("error", "Template not found: " + e.getTemplateName());
        return "error";
    }
}