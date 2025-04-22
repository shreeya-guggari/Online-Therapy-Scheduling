package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Payment;
import com.therapy.scheduler.service.UpiProcessor;
import com.therapy.scheduler.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UpiProcessor UpiProcessor;

    @GetMapping
    public String paymentPage(Model model) {
        return "payment";
    }

    @PostMapping
    public String processPayment(@RequestParam Long appointmentId, @RequestParam Double amount,
                                @RequestParam String paymentMethod) {
        Payment payment = new Payment();
        payment.setAppointmentId(appointmentId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        paymentService.setPaymentProcessor(UpiProcessor);
        paymentService.process(payment);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Payment> apiProcessPayment(@RequestParam Long appointmentId, @RequestParam Double amount,
                                                    @RequestParam String paymentMethod) {
        Payment payment = new Payment();
        payment.setAppointmentId(appointmentId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        paymentService.setPaymentProcessor(UpiProcessor);
        payment = paymentService.process(payment);
        return ResponseEntity.ok(payment);
    }
}