import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { JobPostResponse } from 'src/app/core/models/job-post.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserRole } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-job-detail-dialog',
  template: `
    <h2 mat-dialog-title>Job Details</h2>
    <mat-dialog-content>
      <p><strong>Title:</strong> {{ data.title }}</p>
      <p><strong>Description:</strong> {{ data.description }}</p>
      <p><strong>Budget:</strong> {{ data.budget | currency }}</p>
      <p><strong>Status:</strong> {{ data.status }}</p>
      <p><strong>Deadline:</strong> {{ data.deadline | date }}</p>
      <p><strong>Client:</strong> {{ data.clientName }} ({{ data.clientEmail }})</p>
      <p *ngIf="data.clientCompanyName"><strong>Company:</strong> {{ data.clientCompanyName }}</p>
      <p><strong>Created At:</strong> {{ data.createdAt | date:'short' }}</p>
      <p><strong>Updated At:</strong> {{ data.updatedAt | date:'short' }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="applyForJob()"
        *ngIf="authService.isAuthenticated() && authService.hasRole(UserRole.FREELANCER) && data.status === 'OPEN'"
        color="primary">Apply</button>
      <button mat-button mat-dialog-close>Close</button>
    </mat-dialog-actions>
  `
})
export class JobDetailDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<JobDetailDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: JobPostResponse,
    public authService: AuthService,
    private jobApplicationService: JobApplicationService,
    private snackBar: MatSnackBar
  ) {}

  public get UserRole() {
    return UserRole;
  }

  applyForJob(): void {
    this.jobApplicationService.applyForJob(this.data.id).subscribe({
      next: () => {
        this.snackBar.open('Application submitted successfully!', 'Close', { duration: 3000 });
        this.dialogRef.close(true); // Close dialog and indicate success
      },
      error: (err) => {
        this.snackBar.open(err.error.message || 'Failed to submit application', 'Close', { duration: 3000 });
      }
    });
  }
} 