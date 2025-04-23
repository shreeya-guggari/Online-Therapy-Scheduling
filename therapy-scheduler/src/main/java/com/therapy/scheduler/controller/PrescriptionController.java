package com.therapy.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.therapy.scheduler.service.PrescriptionService;
import com.therapy.scheduler.service.AppointmentService; // Added
import com.therapy.scheduler.model.Appointment; // Added
import java.util.List;

@Controller
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService; // Added

    public PrescriptionController(PrescriptionService prescriptionService, AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/prescription/create")
    public String createPrescription(
            @RequestParam(required = false) Long sessionId,
            @RequestParam String patientUsername,
            Model model) {
        try {
            String defaultNotes = "Prescription created via PrescriptionController";
            prescriptionService.saveNotesAndPrescription(patientUsername, defaultNotes, null, sessionId);
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create prescription: " + e.getMessage());
            return "create-prescription";
        }
    }

    @PostMapping("/prescription/createWithFile")
    public String createPrescriptionWithFile(
            @RequestParam(required = false) Long sessionId,
            @RequestParam String patientUsername,
            @RequestParam("prescription") MultipartFile prescription,
            Model model) {
        try {
            String defaultNotes = "Prescription with file created via PrescriptionController";
            prescriptionService.saveNotesAndPrescription(patientUsername, defaultNotes, prescription, sessionId);
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create prescription: " + e.getMessage());
            return "create-prescription";
        }
    }

    @GetMapping("/prescription/create")
    public String showCreatePrescriptionForm(Model model) {
        Long sessionId = 123L; // Example, replace with actual logic
        model.addAttribute("sessionId", sessionId);
        List<Appointment> appointments = appointmentService.getAppointmentsBySessionId(sessionId);
        model.addAttribute("appointments", appointments);
        return "create-prescription";
    }
}