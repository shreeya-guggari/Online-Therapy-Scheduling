package com.therapy.scheduler.service;

import com.therapy.scheduler.model.Profile;
import com.therapy.scheduler.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile updateProfile(Long userId, String fullName, String phone, String address) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElse(new Profile());
        profile.setUserId(userId);
        profile.setFullName(fullName);
        profile.setPhone(phone);
        profile.setAddress(address);
        return profileRepository.save(profile);
    }
}