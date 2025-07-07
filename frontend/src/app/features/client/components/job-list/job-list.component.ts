import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

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

  constructor(private jobService: JobPostService) {}

  ngOnInit(): void {
    this.fetchJobs();
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

  scrollToJobs() {
    if (this.jobsSection) {
      this.jobsSection.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }
  }
} 