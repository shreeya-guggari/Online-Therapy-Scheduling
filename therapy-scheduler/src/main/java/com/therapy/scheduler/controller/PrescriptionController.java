package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Prescription;
import com.therapy.scheduler.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping
    public String prescriptionPage(Model model) {
        return "prescription";
    }

    @PostMapping
    public String createPrescription(@RequestParam Long sessionId, @RequestParam String details) {
        prescriptionService.createPrescription(sessionId, details);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Prescription> apiCreatePrescription(@RequestParam Long sessionId, @RequestParam String details, Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COUNSELLOR") || a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        Prescription prescription = prescriptionService.createPrescription(sessionId, details);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsBySession(@PathVariable Long sessionId, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(403).build();
        }
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsBySessionId(sessionId);
        return ResponseEntity.ok(prescriptions);
    }
}