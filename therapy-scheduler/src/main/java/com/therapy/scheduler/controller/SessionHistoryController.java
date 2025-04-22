package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.SessionHistory;
import com.therapy.scheduler.service.SessionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/session-history")
public class SessionHistoryController {
    @Autowired
    private SessionHistoryService sessionHistoryService;

    @GetMapping
    public String sessionHistoryPage(Model model) {
        return "session_history";
    }

    @PostMapping
    public String saveSessionSummary(@RequestParam Long sessionId, @RequestParam String summary) {
        sessionHistoryService.saveSessionSummary(sessionId, summary);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<SessionHistory> apiSaveSessionSummary(@RequestParam Long sessionId, @RequestParam String summary) {
        SessionHistory history = sessionHistoryService.saveSessionSummary(sessionId, summary);
        return ResponseEntity.ok(history);
    }
}