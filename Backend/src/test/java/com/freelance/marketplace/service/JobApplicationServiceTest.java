package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.JobApplicationDto;
import com.freelance.marketplace.entity.JobApplication;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.ApplicationStatus;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.repository.JobApplicationRepository;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import com.freelance.marketplace.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;

class JobApplicationServiceTest {
    @Mock private JobApplicationRepository jobApplicationRepository;
    @Mock private JobPostRepository jobPostRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private JobApplicationService service;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new JobApplicationService();
        // Use reflection to inject mocks into private fields
        Field jobAppRepoField = JobApplicationService.class.getDeclaredField("jobApplicationRepository");
        jobAppRepoField.setAccessible(true);
        jobAppRepoField.set(service, jobApplicationRepository);
        Field jobPostRepoField = JobApplicationService.class.getDeclaredField("jobPostRepository");
        jobPostRepoField.setAccessible(true);
        jobPostRepoField.set(service, jobPostRepository);
        Field userRepoField = JobApplicationService.class.getDeclaredField("userRepository");
        userRepoField.setAccessible(true);
        userRepoField.set(service, userRepository);
    }

    @Test
    void applyForJob_success() {
        JobPost job = new JobPost(); job.setId(1L);
        User freelancer = new User(); freelancer.setId(2L);
        when(jobPostRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(freelancer));
        when(jobApplicationRepository.findByJobIdAndFreelancerId(1L, 2L)).thenReturn(Optional.empty());
        when(jobApplicationRepository.save(any(JobApplication.class))).thenAnswer(inv -> inv.getArgument(0));
        JobApplicationDto dto = service.applyForJob(1L, 2L);
        assertNotNull(dto);
        assertEquals(1L, dto.getJobId());
        assertEquals(2L, dto.getFreelancerId());
    }

    @Test
    void applyForJob_alreadyApplied() {
        JobPost job = new JobPost(); job.setId(1L);
        User freelancer = new User(); freelancer.setId(2L);
        when(jobPostRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(freelancer));
        when(jobApplicationRepository.findByJobIdAndFreelancerId(1L, 2L)).thenReturn(Optional.of(new JobApplication()));
        assertThrows(IllegalStateException.class, () -> service.applyForJob(1L, 2L));
    }

    @Test
    void getApplicationsForJob_returnsList() {
        when(jobApplicationRepository.findByJobId(1L)).thenReturn(Collections.emptyList());
        List<JobApplicationDto> result = service.getApplicationsForJob(1L);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void acceptApplication_notFound() {
        when(jobApplicationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.acceptApplication(1L));
    }
} 