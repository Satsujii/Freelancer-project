package com.freelance.marketplace.service;


import com.freelance.marketplace.dto.JobPostRequest;
import com.freelance.marketplace.dto.JobPostResponse;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.exception.ResourceNotFoundException;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import com.freelance.marketplace.repository.ClientProfileRepository;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.entity.ClientProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class JobService {
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final ClientProfileRepository clientProfileRepository;

    public JobService(JobPostRepository jobPostRepository, UserRepository userRepository, ClientProfileRepository clientProfileRepository) {
        this.jobPostRepository = jobPostRepository;
        this.userRepository = userRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    @Transactional
    public JobPostResponse createJob(JobPostRequest request, Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        JobPost job = new JobPost();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setBudget(request.getBudget());
        job.setDeadline(LocalDate.parse(request.getDeadline()));
        job.setClient(client);
        job = jobPostRepository.save(job);
        return toJobResponse(job);
    }

    public JobPostResponse getJob(Long jobId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return toJobResponse(job);
    }

    public List<JobPostResponse> getAllJobs() {
        return jobPostRepository.findAll().stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    public List<JobPostResponse> getJobsByClient(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        return jobPostRepository.findByClient(client).stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    public List<JobPostResponse> getJobsForFreelancers(String title, BigDecimal minBudget, BigDecimal maxBudget, JobStatus status) {
        List<JobPost> jobs = jobPostRepository.findAll();
        return jobs.stream()
            .filter(j -> title == null || j.getTitle().toLowerCase().contains(title.toLowerCase()))
            .filter(j -> minBudget == null || j.getBudget().compareTo(minBudget) >= 0)
            .filter(j -> maxBudget == null || j.getBudget().compareTo(maxBudget) <= 0)
            .filter(j -> status == null || j.getStatus() == status)
            .map(this::toJobResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public JobPostResponse updateJob(Long jobId, JobPostRequest request, Long clientId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Unauthorized");
        }
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setBudget(request.getBudget());
        job.setDeadline(LocalDate.parse(request.getDeadline()));
        job = jobPostRepository.save(job);
        return toJobResponse(job);
    }

    @Transactional
    public JobPostResponse updateJobStatus(Long jobId, JobStatus newStatus, Long clientId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Unauthorized");
        }
        job.setStatus(newStatus);
        job = jobPostRepository.save(job);
        return toJobResponse(job);
    }

    @Transactional
    public JobPostResponse markJobAsCompleted(Long jobId, Long freelancerId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (job.getFreelancer() == null || !job.getFreelancer().getId().equals(freelancerId)) {
            throw new RuntimeException("Unauthorized: You are not assigned to this job");
        }
        if (job.getStatus() != JobStatus.IN_PROGRESS && job.getStatus() != JobStatus.ASSIGNED) {
            throw new RuntimeException("Job is not in progress or assigned");
        }
        job.setStatus(JobStatus.COMPLETED);
        job = jobPostRepository.save(job);
        return toJobResponse(job);
    }

    @Transactional
    public void deleteJob(Long jobId, Long clientId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Unauthorized");
        }
        jobPostRepository.delete(job);
    }

    // Mapping method
    private JobPostResponse toJobResponse(JobPost job) {
        JobPostResponse resp = new JobPostResponse();
        resp.setId(job.getId());
        resp.setTitle(job.getTitle());
        resp.setDescription(job.getDescription());
        resp.setBudget(job.getBudget());
        resp.setDeadline(String.valueOf(job.getDeadline()));
        resp.setStatus(job.getStatus());
        resp.setCreatedAt(job.getCreatedAt());
        resp.setUpdatedAt(job.getUpdatedAt());
        resp.setClientId(job.getClient().getId());
        resp.setClientName(job.getClient().getName());
        resp.setClientEmail(job.getClient().getEmail());
        ClientProfile clientProfile = clientProfileRepository.findByUserId(job.getClient().getId()).orElse(null);
        resp.setClientCompanyName(clientProfile != null ? clientProfile.getCompanyName() : null);
        if (job.getFreelancer() != null) {
            resp.setFreelancerId(job.getFreelancer().getId());
        }
        return resp;
    }
}
