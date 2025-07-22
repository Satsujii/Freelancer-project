import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from './core/guards/role.guard';
import { UserRole } from './core/models/user.model';
import { ClientProfileComponent } from './features/client/components/client-profile/client-profile.component';
import { JobListComponent } from './features/client/components/job-list/job-list.component';
import { ClientDashboardComponent } from './features/client/components/client-dashboard/client-dashboard.component';
import { ClientJobsComponent } from './features/client/components/client-jobs/client-jobs.component';
import { FreelancerProfileComponent } from './features/freelancer/components/freelancer-profile/freelancer-profile.component';
import { MyApplicationsComponent } from './features/freelancer/components/my-applications/my-applications.component';
import { ViewApplicationsComponent } from './features/client/components/view-applications/view-applications.component';

const routes: Routes = [
  { path: '', redirectTo: '/auth/login', pathMatch: 'full' },
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'client',
    component: ClientDashboardComponent,
    canActivate: [RoleGuard],
    data: { roles: [UserRole.CLIENT] },
    children: [
      { path: '', redirectTo: 'jobs', pathMatch: 'full' },
      { path: 'jobs', component: ClientJobsComponent },
      { path: 'jobs/:jobId/applications', component: ViewApplicationsComponent },
      { path: 'profile', component: ClientProfileComponent }
    ]
  },
  {
    path: 'freelancer',
    canActivate: [RoleGuard],
    data: { roles: [UserRole.FREELANCER] },
    children: [
      { path: 'profile', component: FreelancerProfileComponent },
      { path: 'applications', component: MyApplicationsComponent }
    ]
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin.module').then(m => m.AdminModule),
    canActivate: [RoleGuard],
    data: { roles: [UserRole.ADMIN] }
  },
  {
    path: 'jobs',
    component: JobListComponent
  },
  { path: '**', redirectTo: 'auth/login' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}