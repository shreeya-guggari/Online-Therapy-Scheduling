package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Feedback;
import com.therapy.scheduler.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public String feedbackPage(Model model) {
        return "feedback";
    }

    @PostMapping
    public String submitFeedback(@RequestParam Long appointmentId, @RequestParam Long patientId,
                                 @RequestParam int rating, @RequestParam String comments) {
        feedbackService.submitFeedback(appointmentId, patientId, rating, comments);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Feedback> apiSubmitFeedback(@RequestParam Long appointmentId, @RequestParam Long patientId,
                                                     @RequestParam int rating, @RequestParam String comments) {
        Feedback feedback = feedbackService.submitFeedback(appointmentId, patientId, rating, comments);
        return ResponseEntity.ok(feedback);
    }
}