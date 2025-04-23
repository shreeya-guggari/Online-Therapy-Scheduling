package com.therapy.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.therapy.scheduler.model.Prescription;
import com.therapy.scheduler.repository.PrescriptionRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public void saveNotesAndPrescription(String patientUsername, String notes, MultipartFile prescription) throws IOException {
        Path directory = Paths.get(UPLOAD_DIR);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String fileName = patientUsername + "_" + System.currentTimeMillis() + ".pdf";
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, prescription.getBytes());

        Prescription prescriptionEntity = new Prescription();
        prescriptionEntity.setPatientUsername(patientUsername);
        prescriptionEntity.setNotes(notes);
        prescriptionEntity.setFilePath(fileName);
        prescriptionEntity.setUploadDate(new Timestamp(System.currentTimeMillis()));
        prescriptionRepository.save(prescriptionEntity);
    }

    public List<Prescription> getPrescriptionsForUser(String username) {
        return prescriptionRepository.findByPatientUsername(username);
    }

    public void createPrescription(Long sessionId, String notes) {
        Prescription prescription = new Prescription();
        prescription.setSessionId(sessionId);
        prescription.setNotes(notes);
        prescription.setUploadDate(new Timestamp(System.currentTimeMillis()));
        prescriptionRepository.save(prescription);
    }

    public void createPrescription(Long sessionId, String notes, MultipartFile prescriptionFile) throws IOException {
        Path directory = Paths.get(UPLOAD_DIR);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String fileName = "session_" + sessionId + "_" + System.currentTimeMillis() + ".pdf";
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, prescriptionFile.getBytes());

        Prescription prescription = new Prescription();
        prescription.setSessionId(sessionId);
        prescription.setNotes(notes);
        prescription.setFilePath(fileName);
        prescription.setUploadDate(new Timestamp(System.currentTimeMillis()));
        prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsBySessionId(Long sessionId) {
        return prescriptionRepository.findBySessionId(sessionId);
    }
}