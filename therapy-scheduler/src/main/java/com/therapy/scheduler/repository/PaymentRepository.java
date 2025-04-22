package com.therapy.scheduler.repository;

import com.therapy.scheduler.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}