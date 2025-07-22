import { Component, OnInit } from '@angular/core';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { JobApplication } from 'src/app/core/models/job-application.model';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

@Component({
  selector: 'app-my-applications',
  templateUrl: './my-applications.component.html',
  styleUrls: ['./my-applications.component.scss']
})
export class MyApplicationsComponent implements OnInit {
  applications: JobApplication[] = [];
  acceptedJobs: JobPostResponse[] = [];
  loading = false;
  error: string | null = null;

  constructor(private jobApplicationService: JobApplicationService, private jobPostService: JobPostService) { }

  ngOnInit(): void {
    this.fetchApplications();
    this.fetchAcceptedJobs();
  }

  fetchApplications(): void {
    this.loading = true;
    this.jobApplicationService.getApplicationsForFreelancer().subscribe({
      next: (apps) => {
        this.applications = apps;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load applications';
        this.loading = false;
      }
    });
  }

  fetchAcceptedJobs(): void {
    this.jobPostService.getAllJobs().subscribe(jobs => {
      this.acceptedJobs = jobs.filter(job =>
        (job.status === 'IN_PROGRESS' || job.status === 'ASSIGNED') &&
        job.freelancerId // only jobs assigned to this freelancer
      );
    });
  }

  markAsCompleted(jobId: number): void {
    this.jobPostService.markJobAsCompleted(jobId).subscribe(() => {
      this.fetchAcceptedJobs();
    });
  }
}
