import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FreelancerDashboardComponent } from './freelancer-dashboard.component';
import { RecommendationService } from 'src/app/core/services/recommendation.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserRole } from 'src/app/core/models/user.model';

describe('FreelancerDashboardComponent', () => {
  let component: FreelancerDashboardComponent;
  let fixture: ComponentFixture<FreelancerDashboardComponent>;
  let recommendationService: jasmine.SpyObj<RecommendationService>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    const recommendationSpy = jasmine.createSpyObj('RecommendationService', ['getRecommendations']);
    const authSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FreelancerDashboardComponent],
      providers: [
        { provide: RecommendationService, useValue: recommendationSpy },
        { provide: AuthService, useValue: authSpy }
      ]
    });

    recommendationService = TestBed.inject(RecommendationService) as jasmine.SpyObj<RecommendationService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;

    // Setup default return values
    recommendationService.getRecommendations.and.returnValue(of([]));
    authService.getCurrentUser.and.returnValue({ 
      id: 1, 
      name: 'Test User',
      email: 'test@example.com', 
      role: UserRole.FREELANCER,
      enabled: true,
      createdAt: new Date(),
      updatedAt: new Date()
    });

    fixture = TestBed.createComponent(FreelancerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
