import { Component, OnInit } from '@angular/core';
import { AdminService, JobStatistics } from 'src/app/core/services/admin.service';
import { User } from 'src/app/core/models/user.model';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  users: User[] = [];
  jobs: JobPostResponse[] = [];
  statistics: JobStatistics = {};
  totalJobs: number = 0;
  loading = true;
  error: string | null = null;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.loading = true;
    this.adminService.getAllUsers().subscribe({
      next: users => this.users = users,
      error: () => this.error = 'Failed to load users'
    });
    this.adminService.getAllJobs().subscribe({
      next: jobs => this.jobs = jobs,
      error: () => this.error = 'Failed to load jobs'
    });
    this.adminService.getJobStatistics().subscribe({
      next: stats => this.statistics = stats,
      error: () => this.error = 'Failed to load statistics'
    });
    this.adminService.getTotalJobs().subscribe({
      next: total => this.totalJobs = total,
      error: () => this.error = 'Failed to load total jobs',
      complete: () => this.loading = false
    });
  }
} 