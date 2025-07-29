import { Component } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-freelancer-layout',
  templateUrl: './freelancer-layout.component.html',
  styleUrls: ['./freelancer-layout.component.scss']
})
export class FreelancerLayoutComponent {
  sidebarOpen = false;
  currentRoute = '';

  constructor(private authService: AuthService, private router: Router) {
    // Listen to route changes to update page title
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event) => {
      this.currentRoute = (event as NavigationEnd).url;
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  getPageTitle(): string {
    const routeTitleMap: { [key: string]: string } = {
      '/freelancer/dashboard': 'Dashboard',
      '/freelancer/jobs': 'Browse Jobs',
      '/freelancer/applications': 'My Applications',
      '/freelancer/profile': 'Profile',
      '/freelancer/earnings': 'Earnings'
    };
    
    return routeTitleMap[this.currentRoute] || 'Freelancer Hub';
  }
}
