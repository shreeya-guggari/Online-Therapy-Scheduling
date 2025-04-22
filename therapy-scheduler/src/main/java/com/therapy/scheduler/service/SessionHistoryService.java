package com.therapy.scheduler.service;

import com.therapy.scheduler.model.SessionHistory;
import com.therapy.scheduler.repository.SessionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionHistoryService {
    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;

    public SessionHistory saveSessionSummary(Long sessionId, String summary) {
        SessionHistory history = new SessionHistory();
        history.setSessionId(sessionId);
        history.setSummary(summary);
        return sessionHistoryRepository.save(history);
    }
}