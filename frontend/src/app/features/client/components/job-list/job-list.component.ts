import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { JobDetailDialogComponent } from '../../../freelancer/components/job-detail-dialog.component';
import { UserRole } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.scss']
})
export class JobListComponent implements OnInit {
  jobs: JobPostResponse[] = [];
  loading = false;
  error: string | null = null;
  @ViewChild('jobsSection') jobsSection!: ElementRef;

  constructor(
    private jobService: JobPostService,
    private jobApplicationService: JobApplicationService,
    public authService: AuthService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  public get UserRole() {
    return UserRole;
  }

  ngOnInit(): void {
    this.fetchJobs();
  }

  openJobDetailDialog(job: JobPostResponse): void {
    console.log('Opening dialog for job:', job);
    this.dialog.open(JobDetailDialogComponent, {
      width: '600px',
      data: job
    });
  }

  fetchJobs(): void {
    this.loading = true;
    this.jobService.getAllJobs().subscribe({
      next: (jobs) => {
        this.jobs = jobs;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load jobs';
        this.loading = false;
      }
    });
  }

  applyForJob(jobId: number): void {
    this.jobApplicationService.applyForJob(jobId).subscribe({
      next: () => {
        this.snackBar.open('Application submitted successfully!', 'Close', { duration: 3000 });
      },
      error: (err) => {
        this.snackBar.open(err.error.message || 'Failed to submit application', 'Close', { duration: 3000 });
      }
    });
  }

  scrollToJobs() {
    if (this.jobsSection) {
      this.jobsSection.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }
  }
} 