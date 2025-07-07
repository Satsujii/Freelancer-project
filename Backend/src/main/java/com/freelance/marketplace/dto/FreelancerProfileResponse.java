package com.freelance.marketplace.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class FreelancerProfileResponse {
    private Long id;
    private Long userId;
    private String title;
    private String bio;
    private List<String> skills;
    private String location;
    private Double hourlyRate;
    private String website;
    private LocalDateTime createdAt;

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
