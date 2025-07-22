import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { FreelancerProfileService } from 'src/app/core/services/freelancer-profile.service';
import { FreelancerProfile } from 'src/app/core/models/freelancer-profile.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';
import { MatDialog } from '@angular/material/dialog';
import { JobDetailDialogComponent } from '../job-detail-dialog.component';

@Component({
  selector: 'app-freelancer-profile',
  templateUrl: './freelancer-profile.component.html',
  styleUrls: ['./freelancer-profile.component.scss']
})
export class FreelancerProfileComponent implements OnInit {
  profile?: FreelancerProfile;
  loading = true;
  error?: string;
  editMode = false;
  profileForm!: FormGroup;
  saving = false;
  jobs: JobPostResponse[] = [];
  jobsLoading = false;
  jobsError?: string;
  showSection: 'profile' | 'jobs' | 'applications' = 'profile';
  selectedJob?: JobPostResponse;

  constructor(
    private freelancerProfileService: FreelancerProfileService,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private jobPostService: JobPostService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.fetchProfile();
    this.fetchJobs();
  }

  fetchProfile() {
    this.loading = true;
    this.freelancerProfileService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.loading = false;
        this.profileForm = this.fb.group({
          bio: [profile.bio],
          skills: [profile.skills ? profile.skills.join(', ') : ''],
          website: [profile.website]
        });
      },
      error: (err) => {
        this.error = 'Failed to load profile.';
        this.loading = false;
      }
    });
  }

  fetchJobs(filters?: any) {
    this.jobsLoading = true;
    this.jobsError = undefined;
    this.jobPostService.getJobsForFreelancers(filters).subscribe({
      next: (jobs) => {
        this.jobs = jobs;
        this.jobsLoading = false;
      },
      error: (err) => {
        this.jobsError = 'Failed to load jobs.';
        this.jobsLoading = false;
      }
    });
  }

  enableEdit() {
    this.editMode = true;
  }

  cancelEdit() {
    this.editMode = false;
    if (this.profile) {
      this.profileForm.patchValue({
        bio: this.profile.bio,
        skills: this.profile.skills ? this.profile.skills.join(', ') : '',
        website: this.profile.website
      });
    }
  }

  saveProfile() {
    if (!this.profileForm.valid) return;
    this.saving = true;
    const formValue = this.profileForm.value;
    const updatePayload = {
      ...formValue,
      skills: formValue.skills.split(',').map((s: string) => s.trim())
    };
    this.freelancerProfileService.updateProfile(updatePayload).subscribe({
      next: (updated) => {
        this.profile = updated;
        this.editMode = false;
        this.saving = false;
      },
      error: () => {
        this.error = 'Failed to update profile.';
        this.saving = false;
      }
    });
  }

  logout() {
    this.authService.logout();
  }

  openJobDialog(job: JobPostResponse) {
    this.selectedJob = job;
    this.dialog.open(JobDetailDialogComponent, {
      data: job,
      width: '400px',
    });
  }
} 