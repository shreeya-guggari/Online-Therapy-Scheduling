package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Payment;
import com.therapy.scheduler.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    private PaymentProcessor processor;

    public void setPaymentProcessor(PaymentProcessor processor) {
        this.processor = processor;
    }

    public Payment process(Payment payment) {
        if (processor.processPayment(payment)) {
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment processing failed");
    }
}