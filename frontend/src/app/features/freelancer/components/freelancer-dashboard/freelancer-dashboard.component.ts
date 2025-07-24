import { Component, OnInit } from '@angular/core';
import { RecommendationService } from 'src/app/core/services/recommendation.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { JobPostResponse } from 'src/app/core/models/job-post.model';

@Component({
  selector: 'app-freelancer-dashboard',
  templateUrl: './freelancer-dashboard.component.html',
  styleUrls: ['./freelancer-dashboard.component.scss']
})
export class FreelancerDashboardComponent implements OnInit {
  recommendations: JobPostResponse[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private recommendationService: RecommendationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.loading = true;
      this.recommendationService.getRecommendations(user.id).subscribe({
        next: (jobs) => {
          this.recommendations = jobs;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load recommendations.';
          this.loading = false;
        }
      });
    }
  }
}
