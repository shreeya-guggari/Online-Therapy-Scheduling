package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCounsellorIdAndStartTimeBetween(Long counsellorId, LocalDateTime start, LocalDateTime end);
}