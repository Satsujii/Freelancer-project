package com.freelance.marketplace.controller;

import com.freelance.marketplace.dto.JobApplicationDto;
import com.freelance.marketplace.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.freelance.marketplace.security.CustomUserPrincipal;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<JobApplicationDto> applyForJob(@PathVariable Long jobId, @AuthenticationPrincipal CustomUserPrincipal currentUser) {
        JobApplicationDto application = jobApplicationService.applyForJob(jobId, currentUser.getId());
        return ResponseEntity.ok(application);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<JobApplicationDto>> getApplicationsForJob(@PathVariable Long jobId) {
        List<JobApplicationDto> applications = jobApplicationService.getApplicationsForJob(jobId);
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/accept/{applicationId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<JobApplicationDto> acceptApplication(@PathVariable Long applicationId) {
        JobApplicationDto application = jobApplicationService.acceptApplication(applicationId);
        return ResponseEntity.ok(application);
    }

    @PostMapping("/reject/{applicationId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<JobApplicationDto> rejectApplication(@PathVariable Long applicationId) {
        JobApplicationDto application = jobApplicationService.rejectApplication(applicationId);
        return ResponseEntity.ok(application);
    }
} 