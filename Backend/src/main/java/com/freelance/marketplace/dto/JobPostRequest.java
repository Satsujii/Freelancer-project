package com.freelance.marketplace.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobPostRequest {
    private String title;
    private String description;
    private String deadline;
    private BigDecimal budget;
}
