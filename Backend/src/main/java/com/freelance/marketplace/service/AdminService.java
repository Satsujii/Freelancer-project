package com.freelance.marketplace.service;

import com.freelance.marketplace.entity.AdminProfile;
import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.AdminProfileRepository;
import com.freelance.marketplace.repository.JobPostRepository;
import com.freelance.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobPostRepository jobPostRepository;
    @Autowired
    private AdminProfileRepository adminProfileRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<JobPost> getAllJobs() {
        return jobPostRepository.findAll();
    }

    public Map<JobStatus, Long> getJobStatistics() {
        List<JobPost> jobs = jobPostRepository.findAll();
        Map<JobStatus, Long> stats = new EnumMap<>(JobStatus.class);
        for (JobStatus status : JobStatus.values()) {
            stats.put(status, jobs.stream().filter(j -> j.getStatus() == status).count());
        }
        return stats;
    }

    public long getTotalJobs() {
        return jobPostRepository.count();
    }

    public AdminProfile createAdminProfileIfNotExists(User user) {
        return adminProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    AdminProfile profile = new AdminProfile();
                    profile.setUser(user);
                    return adminProfileRepository.save(profile);
                });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user) {
        User existing = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());
        existing.setEnabled(user.getEnabled());
        return userRepository.save(existing);
    }

    public void deleteJob(Long id) {
        jobPostRepository.deleteById(id);
    }

    public JobPost updateJob(Long id, JobPost job) {
        JobPost existing = jobPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        existing.setTitle(job.getTitle());
        existing.setDescription(job.getDescription());
        existing.setStatus(job.getStatus());
        existing.setBudget(job.getBudget());
        existing.setDeadline(job.getDeadline());
        return jobPostRepository.save(existing);
    }
} 