package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Appointment;
import com.therapy.scheduler.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public abstract class AppointmentService {
    @Autowired
    protected AppointmentRepository appointmentRepository;

    public abstract Appointment createAppointment(Long patientId, Long counsellorId, LocalDateTime startTime, LocalDateTime endTime);

    public Appointment bookAppointment(Appointment appointment) {
        boolean isAvailable = appointmentRepository
                .findByCounsellorIdAndStartTimeBetween(
                        appointment.getCounsellorId(),
                        appointment.getStartTime(),
                        appointment.getEndTime()
                ).isEmpty();

        if (isAvailable) {
            return appointmentRepository.save(appointment);
        }
        throw new RuntimeException("Counsellor not available");
    }
}