import { Component, OnInit } from '@angular/core';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostRequest, JobPostResponse } from 'src/app/core/models/job-post.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-client-jobs',
  templateUrl: './client-jobs.component.html',
  styleUrls: ['./client-jobs.component.scss']
})
export class ClientJobsComponent implements OnInit {
  jobs: JobPostResponse[] = [];
  loading = false;
  error: string | null = null;
  jobForm: FormGroup;
  editingJob: JobPostResponse | null = null;
  submitting = false;
  jobStatuses = ['OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

  constructor(private jobService: JobPostService, private fb: FormBuilder) {
    this.jobForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      deadline: ['', Validators.required],
      budget: [0, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.fetchJobs();
  }

  fetchJobs(): void {
    this.loading = true;
    this.jobService.getJobsByClient().subscribe({
      next: jobs => {
        this.jobs = jobs;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load jobs';
        this.loading = false;
      }
    });
  }

  startEdit(job: JobPostResponse) {
    this.editingJob = job;
    this.jobForm.patchValue({
      title: job.title,
      description: job.description,
      deadline: job.deadline,
      budget: job.budget
    });
  }

  cancelEdit() {
    this.editingJob = null;
    this.jobForm.reset();
  }

  submitJob() {
    if (this.jobForm.invalid) return;
    this.submitting = true;
    const jobData: JobPostRequest = this.jobForm.value;
    if (this.editingJob) {
      this.jobService.updateJob(this.editingJob.id, jobData).subscribe({
        next: () => {
          this.fetchJobs();
          this.cancelEdit();
          this.submitting = false;
        },
        error: () => {
          this.error = 'Failed to update job';
          this.submitting = false;
        }
      });
    } else {
      this.jobService.createJob(jobData).subscribe({
        next: () => {
          this.fetchJobs();
          this.jobForm.reset();
          this.submitting = false;
        },
        error: () => {
          this.error = 'Failed to create job';
          this.submitting = false;
        }
      });
    }
  }

  deleteJob(job: JobPostResponse) {
    if (!confirm('Are you sure you want to delete this job?')) return;
    this.jobService.deleteJob(job.id).subscribe({
      next: () => this.fetchJobs(),
      error: () => this.error = 'Failed to delete job'
    });
  }

  updateStatus(job: JobPostResponse, newStatus: string) {
    if (job.status === newStatus) return;
    this.jobService.updateJobStatus(job.id, newStatus).subscribe({
      next: updatedJob => {
        job.status = updatedJob.status;
      },
      error: () => {
        this.error = 'Failed to update job status';
      }
    });
  }
} 