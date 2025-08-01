import { Component } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.scss']
})
export class AdminLayoutComponent {
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
      '/admin/dashboard': 'Dashboard',
      '/admin/users': 'User Management',
      '/admin/jobs': 'Job Management',
      '/admin/applications': 'Applications',
      '/admin/reports': 'Reports'
    };
    
    return routeTitleMap[this.currentRoute] || 'Admin Panel';
  }
} 