package com.therapy.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.therapy.scheduler.model.Prescription;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientUsername(String patientUsername);
    List<Prescription> findBySessionId(Long sessionId);
}