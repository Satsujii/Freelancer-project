package com.freelance.marketplace.controller;


import com.freelance.marketplace.dto.JobPostRequest;
import com.freelance.marketplace.dto.JobPostResponse;
import com.freelance.marketplace.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.freelance.marketplace.security.CustomUserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Get all jobs (public)
    @GetMapping("/public")
    public List<JobPostResponse> getAllPublicJobs() {
        return jobService.getAllJobs();
    }

    // Get all jobs (public)
    @GetMapping("")
    public List<JobPostResponse> getAllJobs() {
        return jobService.getAllJobs();
    }

    // Get a single job by ID
    @GetMapping("/{id}")
    public JobPostResponse getJob(@PathVariable Long id) {
        return jobService.getJob(id);
    }

    // Create a job (Client only)
    @PostMapping
    public ResponseEntity<JobPostResponse> createJob(@RequestBody JobPostRequest request,
                                                     @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        Long clientId = getCurrentUserId(userDetails);
        JobPostResponse job = jobService.createJob(request, clientId);
        return ResponseEntity.ok(job);
    }

    // Update a job (Client only)
    @PutMapping("/{id}")
    public ResponseEntity<JobPostResponse> updateJob(@PathVariable Long id,
                                                 @RequestBody JobPostRequest request,
                                                 @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        Long clientId = getCurrentUserId(userDetails);
        JobPostResponse job = jobService.updateJob(id, request, clientId);
        return ResponseEntity.ok(job);
    }

    // Delete a job (Client only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        Long clientId = getCurrentUserId(userDetails);
        jobService.deleteJob(id, clientId);
        return ResponseEntity.noContent().build();
    }

    // List jobs by current client
    @GetMapping("/my-jobs")
    public List<JobPostResponse> getJobsByClient(@AuthenticationPrincipal CustomUserPrincipal userDetails) {
        Long clientId = getCurrentUserId(userDetails);
        return jobService.getJobsByClient(clientId);
    }

    // Utility: fetch current user ID from CustomUserPrincipal
    private Long getCurrentUserId(CustomUserPrincipal userDetails) {
        return userDetails.getUser().getId();
    }
}
