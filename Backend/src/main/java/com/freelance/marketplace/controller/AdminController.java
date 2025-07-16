package com.freelance.marketplace.controller;

import com.freelance.marketplace.entity.JobPost;
import com.freelance.marketplace.entity.JobStatus;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobPost>> getAllJobs() {
        return ResponseEntity.ok(adminService.getAllJobs());
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<JobStatus, Long>> getJobStatistics() {
        return ResponseEntity.ok(adminService.getJobStatistics());
    }

    @GetMapping("/statistics/total-jobs")
    public ResponseEntity<Long> getTotalJobs() {
        return ResponseEntity.ok(adminService.getTotalJobs());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = adminService.updateUser(id, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<JobPost> updateJob(@PathVariable Long id, @RequestBody JobPost job) {
        JobPost updated = adminService.updateJob(id, job);
        return ResponseEntity.ok(updated);
    }
} 