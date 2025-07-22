package com.freelance.marketplace.dto;

import com.freelance.marketplace.entity.JobStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobPostResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal budget;
    private String deadline;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long clientId;
    private String clientName;
    private String clientEmail;
    private String clientCompanyName;
    private Long freelancerId;
}
