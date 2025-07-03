package com.freelance.marketplace.service;

import com.freelance.marketplace.entity.ClientProfile;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.ClientProfileRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientProfileService {

    @Autowired
    private ClientProfileRepository clientRepo;

    @Autowired
    private UserRepository userRepo;

    public ClientProfile createProfile(Long userId, ClientProfile profileData) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        profileData.setUser(user);
        return clientRepo.save(profileData);
    }

    public ClientProfile getProfile(Long userId) {
        return clientRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Client profile not found"));
    }

    public ClientProfile updateProfile(Long userId, ClientProfile updatedData) {
        ClientProfile existing = getProfile(userId);
        existing.setBio(updatedData.getBio());
        existing.setCompanyName(updatedData.getCompanyName());
        existing.setWebsite(updatedData.getWebsite());
        return clientRepo.save(existing);
    }
}
