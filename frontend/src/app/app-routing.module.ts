import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from './core/guards/role.guard';
import { UserRole } from './core/models/user.model';
import { ClientProfileComponent } from './features/client/components/client-profile/client-profile.component';
import { JobListComponent } from './features/client/components/job-list/job-list.component';
import { ClientDashboardComponent } from './features/client/components/client-dashboard/client-dashboard.component';
import { ClientJobsComponent } from './features/client/components/client-jobs/client-jobs.component';
import { FreelancerProfileComponent } from './features/freelancer/components/freelancer-profile/freelancer-profile.component';

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
      { path: 'profile', component: ClientProfileComponent }
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
  { path: 'freelancer/profile', component: FreelancerProfileComponent },
  { path: '**', redirectTo: 'auth/login' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}