package com.freelance.marketplace.dto;

import com.freelance.marketplace.entity.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationDto {
    private Long id;
    private Long jobId;
    private Long freelancerId;
    private String freelancerName;
    private String freelancerEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
} 