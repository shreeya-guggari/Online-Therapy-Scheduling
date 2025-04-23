package com.therapy.scheduler.service;

import org.springframework.stereotype.Service;
import com.therapy.scheduler.model.Appointment;
import com.therapy.scheduler.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger; // Added
import org.slf4j.LoggerFactory; // Added

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class); // Added

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public void scheduleTherapistAppointment(String patientUsername, String therapistUsername, String dateTime, int duration, Long sessionId) throws Exception {
        LocalDateTime startTime = LocalDateTime.parse(dateTime);
        LocalDateTime endTime = startTime.plusMinutes(duration);

        // Check for conflicts
        List<Appointment> therapistAppointments = appointmentRepository.findByTherapistUsername(therapistUsername);
        for (Appointment existing : therapistAppointments) {
            LocalDateTime existingStart = existing.getStartTime() != null ? existing.getStartTime() : existing.getDateTime();
            LocalDateTime existingEnd = existing.getEndTime() != null ? existing.getEndTime() : existing.getDateTime().plusMinutes(existing.getDuration());
            if (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart)) {
                throw new Exception("Scheduling conflict: Therapist is already booked from " + existingStart + " to " + existingEnd);
            }
        }

        Appointment appointment = new Appointment();
        appointment.setTherapistUsername(therapistUsername);
        appointment.setPatientUsername(patientUsername);
        appointment.setDateTime(startTime);
        appointment.setDuration(duration);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setSessionId(sessionId);
        appointment.setStatus("PENDING");

        logger.info("Saving appointment: therapist={}, patient={}, startTime={}, endTime={}, sessionId={}, status={}",
                therapistUsername, patientUsername, startTime, endTime, sessionId, appointment.getStatus()); // Added

        appointmentRepository.save(appointment);

        logger.info("Appointment saved successfully with ID: {}", appointment.getId()); // Added
    }

    @Transactional
    public void schedulePatientAppointment(String therapistUsername, String patientUsername, String dateTime, Long sessionId) throws Exception {
        LocalDateTime startTime = LocalDateTime.parse(dateTime);
        int defaultDuration = 60;
        LocalDateTime endTime = startTime.plusMinutes(defaultDuration);

        // Check for conflicts
        List<Appointment> therapistAppointments = appointmentRepository.findByTherapistUsername(therapistUsername);
        for (Appointment existing : therapistAppointments) {
            LocalDateTime existingStart = existing.getStartTime() != null ? existing.getStartTime() : existing.getDateTime();
            LocalDateTime existingEnd = existing.getEndTime() != null ? existing.getEndTime() : existing.getDateTime().plusMinutes(existing.getDuration());
            if (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart)) {
                throw new Exception("Scheduling conflict: Therapist is already booked from " + existingStart + " to " + existingEnd);
            }
        }

        Appointment appointment = new Appointment();
        appointment.setTherapistUsername(therapistUsername);
        appointment.setPatientUsername(patientUsername);
        appointment.setDateTime(startTime);
        appointment.setDuration(defaultDuration);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setSessionId(sessionId);
        appointment.setStatus("PENDING");

        logger.info("Saving appointment: therapist={}, patient={}, startTime={}, endTime={}, sessionId={}, status={}",
                therapistUsername, patientUsername, startTime, endTime, sessionId, appointment.getStatus()); // Added

        appointmentRepository.save(appointment);

        logger.info("Appointment saved successfully with ID: {}", appointment.getId()); // Added
    }

    public List<Appointment> getAppointmentsForTherapist(String therapistUsername) {
        return appointmentRepository.findByTherapistUsername(therapistUsername);
    }

    public List<Appointment> getAppointmentsForPatient(String patientUsername) {
        return appointmentRepository.findByPatientUsername(patientUsername);
    }

    public List<Appointment> getAppointmentsBySessionId(Long sessionId) {
        return appointmentRepository.findBySessionId(sessionId);
    }
}