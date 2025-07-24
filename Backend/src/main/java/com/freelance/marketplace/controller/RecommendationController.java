package com.freelance.marketplace.controller;

import com.freelance.marketplace.dto.JobPostResponse;
import com.freelance.marketplace.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public List<JobPostResponse> getRecommendations(@PathVariable Long userId) {
        return recommendationService.recommendJobsForFreelancer(userId);
    }
} 