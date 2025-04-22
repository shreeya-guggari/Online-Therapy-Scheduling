package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findBySessionId(Long sessionId);
}