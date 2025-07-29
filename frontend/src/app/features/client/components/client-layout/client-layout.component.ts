import { Component } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-client-layout',
  templateUrl: './client-layout.component.html',
  styleUrls: ['./client-layout.component.scss']
})
export class ClientLayoutComponent {
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
      '/client/dashboard': 'Dashboard',
      '/client/jobs': 'My Jobs',
      '/client/view-applications': 'Applications',
      '/client/profile': 'Profile',
      '/client/post-job': 'Post New Job'
    };
    
    return routeTitleMap[this.currentRoute] || 'Client Portal';
  }
}
