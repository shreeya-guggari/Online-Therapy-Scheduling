package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Session;
import com.therapy.scheduler.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    private List<SessionObserver> observers = new ArrayList<>();

    public void addObserver(SessionObserver observer) {
        observers.add(observer);
    }

    public Session startSession(Long appointmentId, String sessionType) {
        Session session = new Session();
        session.setAppointmentId(appointmentId);
        session.setSessionType(sessionType);
        session.setStatus("ONGOING");
        session.setStartedAt(new Timestamp(System.currentTimeMillis()));
        session = sessionRepository.save(session);
        notifyObservers(session);
        return session;
    }

    public Session updateNotes(Long sessionId, String notes) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setNotes(notes);
        return sessionRepository.save(session);
    }

    public Session getSessionById(Long sessionId) { // Added method
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    private void notifyObservers(Session session) {
        for (SessionObserver observer : observers) {
            observer.update(session);
        }
    }
}