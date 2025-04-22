package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Payment;
import org.springframework.stereotype.Service;

@Service
public class UpiProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(Payment payment) {
        payment.setStatus("COMPLETED");
        return true;
    }
}