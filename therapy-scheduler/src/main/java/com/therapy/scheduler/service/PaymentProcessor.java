package com.therapy.scheduler.service;

public interface PaymentProcessor {
    boolean processPayment(com.therapy.scheduler.model.Payment payment);
}