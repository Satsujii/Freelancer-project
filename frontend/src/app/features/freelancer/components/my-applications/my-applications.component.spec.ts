import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MyApplicationsComponent } from './my-applications.component';
import { JobApplicationService } from 'src/app/core/services/job-application.service';
import { JobPostService } from 'src/app/core/services/job-post.service';

describe('MyApplicationsComponent', () => {
  let component: MyApplicationsComponent;
  let fixture: ComponentFixture<MyApplicationsComponent>;
  let jobApplicationService: jasmine.SpyObj<JobApplicationService>;
  let jobPostService: jasmine.SpyObj<JobPostService>;

  beforeEach(() => {
    const jobApplicationSpy = jasmine.createSpyObj('JobApplicationService', ['getApplicationsForFreelancer']);
    const jobPostSpy = jasmine.createSpyObj('JobPostService', ['getAllJobs', 'markJobAsCompleted']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
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
    jobPostService.markJobAsCompleted.and.returnValue(of({}));

    fixture = TestBed.createComponent(MyApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
