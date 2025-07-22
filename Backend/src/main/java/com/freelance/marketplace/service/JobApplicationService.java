package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.JobApplicationDto;
import com.freelance.marketplace.entity.JobApplication;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.ApplicationStatus;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.exception.ResourceNotFoundException;
import com.freelance.marketplace.repository.JobApplicationRepository;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private UserRepository userRepository;

    public JobApplicationDto applyForJob(Long jobId, Long freelancerId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found"));

        Optional<JobApplication> existingApplication = jobApplicationRepository.findByJobIdAndFreelancerId(jobId, freelancerId);
        if (existingApplication.isPresent()) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setFreelancer(freelancer);

        JobApplication savedApplication = jobApplicationRepository.save(application);
        return convertToDto(savedApplication);
    }

    public List<JobApplicationDto> getApplicationsForJob(Long jobId) {
        return jobApplicationRepository.findByJobId(jobId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDto> getApplicationsForFreelancer(Long freelancerId) {
        return jobApplicationRepository.findByFreelancerId(freelancerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDto> getApplicationsForClient(Long clientId) {
        // This is a simplified implementation. A more efficient approach would be to
        // add a custom query to the JobApplicationRepository.
        return jobPostRepository.findByClientId(clientId).stream()
                .flatMap(job -> jobApplicationRepository.findByJobId(job.getId()).stream())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public JobApplicationDto acceptApplication(Long applicationId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(ApplicationStatus.ACCEPTED);

        JobPost job = application.getJob();
        job.setFreelancer(application.getFreelancer());
        job.setStatus(JobStatus.IN_PROGRESS); // Set status to IN_PROGRESS

        // Reject other applications for the same job
        jobApplicationRepository.findByJobId(job.getId()).stream()
                .filter(app -> !app.getId().equals(applicationId) && app.getStatus() == ApplicationStatus.PENDING)
                .forEach(app -> {
                    app.setStatus(ApplicationStatus.REJECTED);
                    jobApplicationRepository.save(app);
                });


        jobPostRepository.save(job);
        return convertToDto(jobApplicationRepository.save(application));
    }

    public JobApplicationDto rejectApplication(Long applicationId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        application.setStatus(ApplicationStatus.REJECTED);
        return convertToDto(jobApplicationRepository.save(application));
    }

    private JobApplicationDto convertToDto(JobApplication application) {
        JobApplicationDto dto = new JobApplicationDto();
        dto.setId(application.getId());
        dto.setJobId(application.getJob().getId());
        dto.setFreelancerId(application.getFreelancer().getId());
        dto.setFreelancerName(application.getFreelancer().getName());
        dto.setFreelancerEmail(application.getFreelancer().getEmail());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());
        return dto;
    }
} 