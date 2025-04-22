package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Feedback;
import com.therapy.scheduler.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback submitFeedback(Long appointmentId, Long patientId, int rating, String comments) {
        Feedback feedback = new Feedback();
        feedback.setAppointmentId(appointmentId);
        feedback.setPatientId(patientId);
        feedback.setRating(rating);
        feedback.setComments(comments);
        return feedbackRepository.save(feedback);
    }
}