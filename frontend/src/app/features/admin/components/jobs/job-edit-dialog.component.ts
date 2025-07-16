import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

@Component({
  selector: 'app-job-edit-dialog',
  templateUrl: './job-edit-dialog.component.html',
  styleUrls: ['./job-edit-dialog.component.scss']
})
export class JobEditDialogComponent {
  form: FormGroup;
  jobStatuses = ['OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

  constructor(
    public dialogRef: MatDialogRef<JobEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: JobPostResponse,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      title: [data.title, Validators.required],
      description: [data.description, Validators.required],
      status: [data.status, Validators.required],
      budget: [data.budget, [Validators.required, Validators.min(0)]],
      deadline: [data.deadline, Validators.required]
    });
  }

  onSave(): void {
    if (this.form.valid) {
      this.dialogRef.close({ ...this.data, ...this.form.value });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 