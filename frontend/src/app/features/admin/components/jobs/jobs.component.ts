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
  filteredJobs: JobPostResponse[] = [];
  loading = true;
  error: string | null = null;
  displayedColumns = ['id', 'title', 'budget', 'status', 'createdDate'];
  displayedColumnsWithActions = [...this.displayedColumns, 'actions'];

  constructor(private adminService: AdminService, private dialog: MatDialog, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.adminService.getAllJobs().subscribe({
      next: jobs => {
        this.jobs = jobs;
        this.filteredJobs = jobs;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load jobs';
        this.loading = false;
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredJobs = this.jobs.filter(job => 
      job.title.toLowerCase().includes(filterValue) ||
      job.status.toLowerCase().includes(filterValue)
    );
  }

  addJob(): void {
    const dialogRef = this.dialog.open(JobEditDialogComponent, {
      width: '500px',
      data: { title: '', description: '', budget: null, status: 'DRAFT' }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.toastr.info('Add job functionality needs to be implemented in backend');
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
            this.filteredJobs = [...this.jobs];
            this.toastr.success('Job updated successfully');
          },
          error: () => this.toastr.error('Failed to update job')
        });
      }
    });
  }

  toggleJobStatus(job: any): void {
    const newStatus = job.status === 'ACTIVE' ? 'CLOSED' : 'ACTIVE';
    this.adminService.updateJob(job.id, { ...job, status: newStatus }).subscribe({
      next: updated => {
        const idx = this.jobs.findIndex(j => j.id === updated.id);
        if (idx !== -1) this.jobs[idx] = updated;
        this.filteredJobs = [...this.jobs];
        this.toastr.success(`Job ${newStatus.toLowerCase()} successfully`);
      },
      error: () => this.toastr.error('Failed to update job status')
    });
  }

  deleteJob(job: any): void {
    if (confirm(`Are you sure you want to delete job '${job.title}'?`)) {
      this.adminService.deleteJob(job.id).subscribe({
        next: () => {
          this.jobs = this.jobs.filter(j => j.id !== job.id);
          this.filteredJobs = [...this.jobs];
          this.toastr.success('Job deleted successfully');
        },
        error: () => this.toastr.error('Failed to delete job')
      });
    }
  }

  getActiveJobsCount(): number {
    return this.filteredJobs.filter(job => job.status.toString() === 'ACTIVE').length;
  }

  getPendingJobsCount(): number {
    return this.filteredJobs.filter(job => job.status.toString() === 'PENDING').length;
  }

  getClosedJobsCount(): number {
    return this.filteredJobs.filter(job => job.status.toString() === 'CLOSED').length;
  }

  getStatusClass(status: string): string {
    return status.toLowerCase();
  }

  getStatusIcon(status: string): string {
    const icons: { [key: string]: string } = {
      'ACTIVE': 'check_circle',
      'PENDING': 'schedule',
      'CLOSED': 'cancel',
      'DRAFT': 'edit'
    };
    return icons[status] || 'work';
  }

  getToggleIcon(status: string): string {
    return status === 'ACTIVE' ? 'pause_circle' : 'play_circle';
  }

  getToggleTooltip(status: string): string {
    return status === 'ACTIVE' ? 'Close Job' : 'Activate Job';
  }
} 