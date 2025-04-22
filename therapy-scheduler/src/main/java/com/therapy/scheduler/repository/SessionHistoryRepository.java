package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.SessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Long> {
}