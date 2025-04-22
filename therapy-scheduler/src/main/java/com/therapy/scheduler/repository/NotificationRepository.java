package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}