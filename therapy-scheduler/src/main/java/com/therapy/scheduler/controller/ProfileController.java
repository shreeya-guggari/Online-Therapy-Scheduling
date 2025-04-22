package com.therapy.scheduler.controller;

import com.therapy.scheduler.model.Profile;
import com.therapy.scheduler.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam Long userId, @RequestParam String fullName,
                               @RequestParam String phone, @RequestParam String address) {
        profileService.updateProfile(userId, fullName, phone, address);
        return "redirect:/";
    }

    @PostMapping("/api")
    public ResponseEntity<Profile> apiUpdateProfile(@RequestParam Long userId, @RequestParam String fullName,
                                                   @RequestParam String phone, @RequestParam String address) {
        Profile profile = profileService.updateProfile(userId, fullName, phone, address);
        return ResponseEntity.ok(profile);
    }
}