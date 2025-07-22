package com.freelance.marketplace.repository;

import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findByClient(User client);
    List<JobPost> findByClientId(Long clientId);
    List<JobPost> findByTitleContainingIgnoreCase(String title);
    List<JobPost> findByBudgetBetween(java.math.BigDecimal min, java.math.BigDecimal max);
    List<JobPost> findByStatus(com.freelance.marketplace.entity.JobStatus status);
}
