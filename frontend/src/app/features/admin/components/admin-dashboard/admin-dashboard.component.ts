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

  // Sample data for recent activities
  recentActivities = [
    {
      type: 'job',
      icon: 'work',
      description: 'New job "Senior Developer" posted',
      timestamp: new Date(Date.now() - 1000 * 60 * 30)
    },
    {
      type: 'user',
      icon: 'person_add',
      description: 'New user registered',
      timestamp: new Date(Date.now() - 1000 * 60 * 60)
    },
    {
      type: 'application',
      icon: 'assignment',
      description: '5 new applications received',
      timestamp: new Date(Date.now() - 1000 * 60 * 120)
    }
  ];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.loading = true;
    this.error = null;
    
    let completedRequests = 0;
    const totalRequests = 4;
    
    const checkComplete = () => {
      completedRequests++;
      if (completedRequests === totalRequests) {
        this.loading = false;
      }
    };
    
    this.adminService.getAllUsers().subscribe({
      next: users => this.users = users,
      error: () => this.error = 'Failed to load users',
      complete: () => checkComplete()
    });
    
    this.adminService.getAllJobs().subscribe({
      next: jobs => this.jobs = jobs,
      error: () => this.error = 'Failed to load jobs',
      complete: () => checkComplete()
    });
    
    this.adminService.getJobStatistics().subscribe({
      next: stats => this.statistics = stats,
      error: () => this.error = 'Failed to load statistics',
      complete: () => checkComplete()
    });
    
    this.adminService.getTotalJobs().subscribe({
      next: total => this.totalJobs = total,
      error: () => this.error = 'Failed to load total jobs',
      complete: () => checkComplete()
    });
  }

  getCardClass(index: number): string {
    const classes = ['secondary', 'success', 'warning'];
    return classes[index % classes.length];
  }

  getStatusIcon(status: string): string {
    const icons: { [key: string]: string } = {
      'active': 'check_circle',
      'pending': 'schedule',
      'closed': 'cancel',
      'draft': 'edit'
    };
    return icons[status.toLowerCase()] || 'work';
  }

  formatStatusKey(key: string): string {
    return key.charAt(0).toUpperCase() + key.slice(1).replace(/_/g, ' ');
  }

  getPercentage(value: number): number {
    if (this.totalJobs === 0) {
      return 0;
    }
    return Math.round((value / this.totalJobs) * 100);
  }
} 