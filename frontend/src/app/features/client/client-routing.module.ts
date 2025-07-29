import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientLayoutComponent } from './components/client-layout/client-layout.component';
import { ClientDashboardComponent } from './components/client-dashboard/client-dashboard.component';
import { ClientJobsComponent } from './components/client-jobs/client-jobs.component';
import { ClientProfileComponent } from './components/client-profile/client-profile.component';
import { ViewApplicationsComponent } from './components/view-applications/view-applications.component';
import { JobListComponent } from './components/job-list/job-list.component';
import { RoleGuard } from 'src/app/core/guards/role.guard';
import { UserRole } from 'src/app/core/models/user.model';

const routes: Routes = [
  {
    path: '',
    component: ClientLayoutComponent,
    canActivate: [RoleGuard],
    data: { roles: [UserRole.CLIENT] },
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: ClientDashboardComponent },
      { path: 'jobs', component: ClientJobsComponent },
      { path: 'jobs/:jobId/applications', component: ViewApplicationsComponent },
      { path: 'job-list', component: JobListComponent },
      { path: 'profile', component: ClientProfileComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule {}
