package com.therapy.scheduler.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.therapy.scheduler.model.Prescription;
import com.therapy.scheduler.repository.PrescriptionRepository;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public void saveNotesAndPrescription(String patientUsername, String notes, MultipartFile prescription, Long sessionId) throws Exception {
        Prescription p = new Prescription();
        p.setPatientUsername(patientUsername);
        p.setNotes(notes);
        p.setFilePath(prescription != null ? prescription.getOriginalFilename() : null);
        p.setUploadDate(new Timestamp(System.currentTimeMillis()));
        p.setSessionId(sessionId);
        prescriptionRepository.save(p);
    }

    public void saveNotesAndPrescription(String patientUsername, String notes, MultipartFile prescription) throws Exception {
        saveNotesAndPrescription(patientUsername, notes, prescription, null);
    }

    public List<Prescription> getPrescriptionsForUser(String username) {
        return prescriptionRepository.findByPatientUsername(username);
    }

    public List<Prescription> getPrescriptionsBySessionId(Long sessionId) {
        return prescriptionRepository.findBySessionId(sessionId);
    }
}