import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { JobEditDialogComponent } from './job-edit-dialog.component';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.scss']
})
export class JobsComponent implements OnInit {
  jobs: JobPostResponse[] = [];
  loading = true;
  error: string | null = null;
  displayedColumns = ['id', 'title', 'status'];
  displayedColumnsWithActions = [...this.displayedColumns, 'actions'];

  constructor(private adminService: AdminService, private dialog: MatDialog, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.adminService.getAllJobs().subscribe({
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

  editJob(job: any): void {
    const dialogRef = this.dialog.open(JobEditDialogComponent, {
      width: '500px',
      data: { ...job }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.adminService.updateJob(job.id, result).subscribe({
          next: updated => {
            const idx = this.jobs.findIndex(j => j.id === updated.id);
            if (idx !== -1) this.jobs[idx] = updated;
            this.toastr.success('Job updated successfully');
          },
          error: () => this.toastr.error('Failed to update job')
        });
      }
    });
  }

  deleteJob(job: any): void {
    if (confirm(`Are you sure you want to delete job '${job.title}'?`)) {
      this.adminService.deleteJob(job.id).subscribe({
        next: () => {
          this.jobs = this.jobs.filter(j => j.id !== job.id);
          this.toastr.success('Job deleted successfully');
        },
        error: () => this.toastr.error('Failed to delete job')
      });
    }
  }
} 