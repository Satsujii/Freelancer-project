package com.freelance.marketplace.repository;

import com.freelance.marketplace.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Optional<JobApplication> findByJobIdAndFreelancerId(Long jobId, Long freelancerId);
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByFreelancerId(Long freelancerId);
} 