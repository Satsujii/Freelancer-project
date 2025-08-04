import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewApplicationsComponent } from './view-applications.component';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { JobPostService } from 'src/app/core/services/job-post.service';

describe('ViewApplicationsComponent', () => {
  let component: ViewApplicationsComponent;
  let fixture: ComponentFixture<ViewApplicationsComponent>;
  let jobApplicationService: jasmine.SpyObj<JobApplicationService>;
  let jobPostService: jasmine.SpyObj<JobPostService>;

  beforeEach(() => {
    const jobApplicationSpy = jasmine.createSpyObj('JobApplicationService', ['getApplicationsForJob', 'acceptApplication', 'rejectApplication']);
    const jobPostSpy = jasmine.createSpyObj('JobPostService', ['getJob']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
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
    jobApplicationService.acceptApplication.and.returnValue(of({}));
    jobApplicationService.rejectApplication.and.returnValue(of({}));
    jobPostService.getJob.and.returnValue(of({}));

    fixture = TestBed.createComponent(ViewApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
