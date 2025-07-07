package com.freelance.marketplace.service;


import com.freelance.marketplace.dto.JobPostRequest;
import com.freelance.marketplace.dto.JobPostResponse;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.exception.ResourceNotFoundException;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    public JobService(JobPostRepository jobPostRepository, UserRepository userRepository) {
        this.jobPostRepository = jobPostRepository;
        this.userRepository = userRepository;
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
        return resp;
    }
}
