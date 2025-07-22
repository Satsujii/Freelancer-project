import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { JobApplication } from 'src/app/core/models/job-application.model';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

@Component({
  selector: 'app-view-applications',
  templateUrl: './view-applications.component.html',
  styleUrls: ['./view-applications.component.scss']
})
export class ViewApplicationsComponent implements OnInit {
  applications: JobApplication[] = [];
  loading = false;
  error: string | null = null;
  jobId!: number;
  job: JobPostResponse | null = null;

  constructor(
    private route: ActivatedRoute,
    private jobApplicationService: JobApplicationService,
    private jobPostService: JobPostService
  ) { }

  ngOnInit(): void {
    this.jobId = +this.route.snapshot.paramMap.get('jobId')!;
    this.fetchJob();
    this.fetchApplications();
  }

  fetchJob(): void {
    this.jobPostService.getJob(this.jobId).subscribe({
      next: (job) => {
        this.job = job;
      },
      error: () => {
        this.error = 'Failed to load job details';
      }
    });
  }

  fetchApplications(): void {
    this.loading = true;
    this.jobApplicationService.getApplicationsForJob(this.jobId).subscribe({
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

  accept(applicationId: number): void {
    this.jobApplicationService.acceptApplication(applicationId).subscribe(() => {
        this.fetchApplications(); // Refresh the list
        this.fetchJob(); // Refresh job status
    });
  }

  reject(applicationId: number): void {
    this.jobApplicationService.rejectApplication(applicationId).subscribe(() => {
        this.fetchApplications(); // Refresh the list
    });
  }
}
