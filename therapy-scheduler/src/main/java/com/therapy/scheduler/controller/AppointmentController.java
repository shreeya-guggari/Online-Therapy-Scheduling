package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Appointment;
import com.therapy.scheduler.service.TextAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private TextAppointmentService appointmentService;

    @GetMapping("/new")
    public String newAppointmentPage(Model model) {
        return "appointment";
    }

    @PostMapping
    public String bookAppointment(@RequestParam Long patientId, @RequestParam Long counsellorId,
                                 @RequestParam String startTime, @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        Appointment appointment = appointmentService.createAppointment(patientId, counsellorId, start, end);
        appointmentService.bookAppointment(appointment);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Appointment> apiBookAppointment(@RequestParam Long patientId, @RequestParam Long counsellorId,
                                                         @RequestParam String startTime, @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        Appointment appointment = appointmentService.createAppointment(patientId, counsellorId, start, end);
        appointment = appointmentService.bookAppointment(appointment);
        return ResponseEntity.ok(appointment);
    }
}