package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.FreelancerProfileRequest;
import com.freelance.marketplace.dto.FreelancerProfileResponse;
import com.freelance.marketplace.entity.FreelancerProfile;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.FreelancerProfileRepository;
import com.freelance.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreelancerProfileService {
    @Autowired
    private FreelancerProfileRepository freelancerRepo;
    @Autowired
    private UserRepository userRepo;

    public FreelancerProfileResponse createProfile(Long userId, FreelancerProfileRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (freelancerRepo.existsByUserId(userId)) {
            throw new RuntimeException("Profile already exists for user");
        }

        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setWebsite(request.getWebsite());

        freelancerRepo.save(profile);

        return mapToResponse(profile);
    }

    public FreelancerProfileResponse updateProfile(Long userId, FreelancerProfileRequest request) {
        FreelancerProfile profile = freelancerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setWebsite(request.getWebsite());

        freelancerRepo.save(profile);

        return mapToResponse(profile);
    }

    public FreelancerProfileResponse getProfileByUserId(Long userId) {
        FreelancerProfile profile = freelancerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    private FreelancerProfileResponse mapToResponse(FreelancerProfile profile) {
        FreelancerProfileResponse response = new FreelancerProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setBio(profile.getBio());
        response.setSkills(profile.getSkills());
        response.setWebsite(profile.getWebsite());
        return response;
    }

    public FreelancerProfile createProfile(Long userId, FreelancerProfile profileData) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        profileData.setUser(user);
        return freelancerRepo.save(profileData);
    }

    public FreelancerProfile getProfile(Long userId) {
        return freelancerRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Freelancer profile not found"));
    }

    public FreelancerProfile updateProfile(Long userId, FreelancerProfile updatedData) {
        FreelancerProfile existing = getProfile(userId);
        existing.setBio(updatedData.getBio());
        existing.setSkills(updatedData.getSkills());
        existing.setWebsite(updatedData.getWebsite());
        return freelancerRepo.save(existing);
    }

    public void deleteProfile(Long userId) {
        FreelancerProfile profile = freelancerRepo.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Freelancer profile not found"));
        freelancerRepo.delete(profile);
    }
}
