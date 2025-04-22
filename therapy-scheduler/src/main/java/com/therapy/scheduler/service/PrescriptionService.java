package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Prescription;
import com.therapy.scheduler.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public Prescription createPrescription(Long sessionId, String details) {
        Prescription prescription = new Prescription();
        prescription.setSessionId(sessionId);
        prescription.setDetails(details);
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsBySessionId(Long sessionId) {
        return prescriptionRepository.findBySessionId(sessionId);
    }
}