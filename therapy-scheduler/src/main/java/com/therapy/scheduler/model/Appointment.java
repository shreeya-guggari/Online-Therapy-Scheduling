package com.therapy.scheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fields for AppointmentService (username-based)
    @Column(name = "therapist_username")
    private String therapistUsername;

    @Column(name = "patient_username")
    private String patientUsername;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private Integer duration; // Nullable for TextAppointmentService

    // Fields for TextAppointmentService (ID-based)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "counsellor_id")
    private Long counsellorId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String status;

    @Column(name = "session_id")
    private Long sessionId; // Shared field for both services

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTherapistUsername() { return therapistUsername; }
    public void setTherapistUsername(String therapistUsername) { this.therapistUsername = therapistUsername; }

    public String getPatientUsername() { return patientUsername; }
    public void setPatientUsername(String patientUsername) { this.patientUsername = patientUsername; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getCounsellorId() { return counsellorId; }
    public void setCounsellorId(Long counsellorId) { this.counsellorId = counsellorId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
}