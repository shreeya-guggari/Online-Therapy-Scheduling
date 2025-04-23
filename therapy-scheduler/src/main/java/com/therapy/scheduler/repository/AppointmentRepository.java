package com.therapy.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.therapy.scheduler.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // For TextAppointmentService
    List<Appointment> findByCounsellorIdAndStartTimeBetween(Long counsellorId, LocalDateTime start, LocalDateTime end);

    // For AppointmentService
    List<Appointment> findByTherapistUsername(String therapistUsername);
    List<Appointment> findByPatientUsername(String patientUsername);
    List<Appointment> findBySessionId(Long sessionId);
}