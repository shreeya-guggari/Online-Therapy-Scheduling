package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}