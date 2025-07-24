package com.freelance.marketplace.service;

import com.freelance.marketplace.entity.AdminProfile;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.AdminProfileRepository;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;

class AdminServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private JobPostRepository jobPostRepository;
    @Mock private AdminProfileRepository adminProfileRepository;
    @InjectMocks private AdminService service;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new AdminService();
        // Use reflection to inject mocks into private fields
        Field userRepoField = AdminService.class.getDeclaredField("userRepository");
        userRepoField.setAccessible(true);
        userRepoField.set(service, userRepository);
        Field jobPostRepoField = AdminService.class.getDeclaredField("jobPostRepository");
        jobPostRepoField.setAccessible(true);
        jobPostRepoField.set(service, jobPostRepository);
        Field adminProfileRepoField = AdminService.class.getDeclaredField("adminProfileRepository");
        adminProfileRepoField.setAccessible(true);
        adminProfileRepoField.set(service, adminProfileRepository);
    }

    @Test
    void getAllUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<User> result = service.getAllUsers();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getAllJobs_returnsList() {
        when(jobPostRepository.findAll()).thenReturn(Collections.emptyList());
        List<JobPost> result = service.getAllJobs();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getJobStatistics_returnsMap() {
        when(jobPostRepository.findAll()).thenReturn(Collections.emptyList());
        assertNotNull(service.getJobStatistics());
    }

    @Test
    void createAdminProfileIfNotExists_createsNew() {
        User user = new User(); user.setId(1L);
        when(adminProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(adminProfileRepository.save(any(AdminProfile.class))).thenAnswer(inv -> inv.getArgument(0));
        AdminProfile result = service.createAdminProfileIfNotExists(user);
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void createAdminProfileIfNotExists_returnsExisting() {
        User user = new User(); user.setId(1L);
        AdminProfile profile = new AdminProfile();
        when(adminProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        AdminProfile result = service.createAdminProfileIfNotExists(user);
        assertEquals(profile, result);
    }
} 