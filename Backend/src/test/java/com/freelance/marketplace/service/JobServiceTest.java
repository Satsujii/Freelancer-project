package com.freelance.marketplace.service;

import com.freelance.marketplace.dto.JobPostRequest;
import com.freelance.marketplace.dto.JobPostResponse;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import com.freelance.marketplace.repository.ClientProfileRepository;
import com.freelance.marketplace.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceTest {
    @Mock
    private JobPostRepository jobPostRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientProfileRepository clientProfileRepository;
    @InjectMocks
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobService = new JobService(jobPostRepository, userRepository, clientProfileRepository);
    }

    @Test
    void getAllJobs_returnsList() {
        when(jobPostRepository.findAll()).thenReturn(Collections.emptyList());
        List<JobPostResponse> result = jobService.getAllJobs();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getJob_returnsJobResponse() {
        JobPost job = new JobPost();
        job.setId(1L);
        job.setTitle("Test Job");
        job.setDescription("desc");
        job.setBudget(BigDecimal.TEN);
        job.setDeadline(LocalDate.now());
        User client = new User();
        client.setId(2L);
        client.setName("Client");
        client.setEmail("client@email.com");
        job.setClient(client);
        when(jobPostRepository.findById(1L)).thenReturn(Optional.of(job));
        JobPostResponse resp = jobService.getJob(1L);
        assertNotNull(resp);
        assertEquals("Test Job", resp.getTitle());
    }

    @Test
    void getJob_throwsIfNotFound() {
        when(jobPostRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> jobService.getJob(1L));
    }

    @Test
    void createJob_savesAndReturnsResponse() {
        JobPostRequest req = new JobPostRequest();
        req.setTitle("New Job");
        req.setDescription("desc");
        req.setBudget(BigDecimal.ONE);
        req.setDeadline(LocalDate.now().toString());
        User client = new User();
        client.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(client));
        when(jobPostRepository.save(any(JobPost.class))).thenAnswer(inv -> inv.getArgument(0));
        JobPostResponse resp = jobService.createJob(req, 2L);
        assertNotNull(resp);
        assertEquals("New Job", resp.getTitle());
    }

    @Test
    void updateJob_throwsIfNotFound() {
        when(jobPostRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> jobService.updateJob(1L, new JobPostRequest(), 2L));
    }

    @Test
    void deleteJob_throwsIfNotFound() {
        when(jobPostRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> jobService.deleteJob(1L, 2L));
    }
} 