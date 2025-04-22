package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Notification;
import com.therapy.scheduler.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String notificationPage(Model model) {
        return "notification";
    }

    @PostMapping
    public String sendNotification(@RequestParam Long userId, @RequestParam String message,
                                  @RequestParam String type) {
        notificationService.sendNotification(userId, message, type);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Notification> apiSendNotification(@RequestParam Long userId, @RequestParam String message,
                                                           @RequestParam String type) {
        Notification notification = notificationService.sendNotification(userId, message, type);
        return ResponseEntity.ok(notification);
    }
}