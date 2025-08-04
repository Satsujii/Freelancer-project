import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';

import { ViewApplicationsComponent } from './view-applications.component';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { JobPostService } from 'src/app/core/services/job-post.service';
import { JobApplication } from 'src/app/core/models/job-application.model';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

describe('ViewApplicationsComponent', () => {
  let component: ViewApplicationsComponent;
  let fixture: ComponentFixture<ViewApplicationsComponent>;
  let jobApplicationService: jasmine.SpyObj<JobApplicationService>;
  let jobPostService: jasmine.SpyObj<JobPostService>;

  beforeEach(() => {
    const jobApplicationSpy = jasmine.createSpyObj('JobApplicationService', ['getApplicationsForJob', 'acceptApplication', 'rejectApplication']);
    const jobPostSpy = jasmine.createSpyObj('JobPostService', ['getJob']);

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatChipsModule
      ],
      declarations: [ViewApplicationsComponent],
      providers: [
        { provide: JobApplicationService, useValue: jobApplicationSpy },
        { provide: JobPostService, useValue: jobPostSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => '1'
              }
            }
          }
        }
      ]
    });

    jobApplicationService = TestBed.inject(JobApplicationService) as jasmine.SpyObj<JobApplicationService>;
    jobPostService = TestBed.inject(JobPostService) as jasmine.SpyObj<JobPostService>;

    // Setup default return values
    jobApplicationService.getApplicationsForJob.and.returnValue(of([]));
    jobApplicationService.acceptApplication.and.returnValue(of({
      id: 1,
      jobId: 1,
      freelancerId: 1,
      freelancerName: 'Test Freelancer',
      freelancerEmail: 'freelancer@test.com',
      status: 'PENDING' as any,
      appliedAt: new Date()
    } as JobApplication));
    jobApplicationService.rejectApplication.and.returnValue(of({
      id: 1,
      jobId: 1,
      freelancerId: 1,
      freelancerName: 'Test Freelancer',
      freelancerEmail: 'freelancer@test.com',
      status: 'REJECTED' as any,
      appliedAt: new Date()
    } as JobApplication));
    jobPostService.getJob.and.returnValue(of({
      id: 1,
      title: 'Test Job',
      description: 'Test Description',
      budget: 1000,
      deadline: '2024-12-31',
      status: 'OPEN',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
      clientId: 1,
      clientName: 'Test Client',
      clientEmail: 'client@test.com',
      clientCompanyName: 'Test Company'
    } as JobPostResponse));

    fixture = TestBed.createComponent(ViewApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
