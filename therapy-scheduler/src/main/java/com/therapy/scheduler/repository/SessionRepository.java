package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}