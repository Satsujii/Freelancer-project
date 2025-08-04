import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';

import { MyApplicationsComponent } from './my-applications.component';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

describe('MyApplicationsComponent', () => {
  let component: MyApplicationsComponent;
  let fixture: ComponentFixture<MyApplicationsComponent>;
  let jobApplicationService: jasmine.SpyObj<JobApplicationService>;
  let jobPostService: jasmine.SpyObj<JobPostService>;

  beforeEach(() => {
    const jobApplicationSpy = jasmine.createSpyObj('JobApplicationService', ['getApplicationsForFreelancer']);
    const jobPostSpy = jasmine.createSpyObj('JobPostService', ['getAllJobs', 'markJobAsCompleted']);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatIconModule,
        MatCardModule,
        MatButtonModule,
        MatChipsModule
      ],
      declarations: [MyApplicationsComponent],
      providers: [
        { provide: JobApplicationService, useValue: jobApplicationSpy },
        { provide: JobPostService, useValue: jobPostSpy }
      ]
    });

    jobApplicationService = TestBed.inject(JobApplicationService) as jasmine.SpyObj<JobApplicationService>;
    jobPostService = TestBed.inject(JobPostService) as jasmine.SpyObj<JobPostService>;

    // Setup default return values
    jobApplicationService.getApplicationsForFreelancer.and.returnValue(of([]));
    jobPostService.getAllJobs.and.returnValue(of([]));
    jobPostService.markJobAsCompleted.and.returnValue(of({
      id: 1,
      title: 'Test Job',
      description: 'Test Description',
      budget: 1000,
      deadline: '2024-12-31',
      status: 'COMPLETED',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
      clientId: 1,
      clientName: 'Test Client',
      clientEmail: 'client@test.com',
      clientCompanyName: 'Test Company'
    } as JobPostResponse));

    fixture = TestBed.createComponent(MyApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
