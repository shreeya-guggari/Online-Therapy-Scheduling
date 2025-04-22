package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Session;
import com.therapy.scheduler.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping("/new")
    public String newSessionPage(Model model) {
        return "session";
    }

    @PostMapping
    public String startSession(@RequestParam Long appointmentId, @RequestParam String sessionType) {
        sessionService.startSession(appointmentId, sessionType);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Session> apiStartSession(@RequestParam Long appointmentId, @RequestParam String sessionType) {
        Session session = sessionService.startSession(appointmentId, sessionType);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<Session> updateSessionNotes(@PathVariable Long id, @RequestParam String notes, Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COUNSELLOR") || a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        Session session = sessionService.updateNotes(id, notes);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/{id}/notes")
    public ResponseEntity<String> getSessionNotes(@PathVariable Long id, Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COUNSELLOR") || a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        Session session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session.getNotes());
    }
}