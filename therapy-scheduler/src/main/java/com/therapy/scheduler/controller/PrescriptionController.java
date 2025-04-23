package com.therapy.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.therapy.scheduler.service.PrescriptionService;

@Controller
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/prescription/create")
    public String createPrescription(@RequestParam Long sessionId, @RequestParam String notes, Model model) {
        try {
            prescriptionService.createPrescription(sessionId, notes);
            model.addAttribute("message", "Prescription created successfully.");
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create prescription: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/prescription/create-with-file")
    public String createPrescriptionWithFile(@RequestParam Long sessionId, @RequestParam String notes,
                                           @RequestParam("prescriptionFile") MultipartFile prescriptionFile, Model model) {
        try {
            prescriptionService.createPrescription(sessionId, notes, prescriptionFile);
            model.addAttribute("message", "Prescription with file created successfully.");
            return "redirect:/therapist/home";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create prescription: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/prescriptions/session")
    public String getPrescriptionsBySessionId(@RequestParam Long sessionId, Model model) {
        model.addAttribute("prescriptions", prescriptionService.getPrescriptionsBySessionId(sessionId));
        return "prescription-list";
    }
}