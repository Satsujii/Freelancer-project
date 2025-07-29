import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FreelancerLayoutComponent } from './components/freelancer-layout/freelancer-layout.component';
import { FreelancerDashboardComponent } from './components/freelancer-dashboard/freelancer-dashboard.component';
import { FreelancerProfileComponent } from './components/freelancer-profile/freelancer-profile.component';
import { MyApplicationsComponent } from './components/my-applications/my-applications.component';
import { RoleGuard } from 'src/app/core/guards/role.guard';
import { UserRole } from 'src/app/core/models/user.model';

const routes: Routes = [
  {
    path: '',
    component: FreelancerLayoutComponent,
    canActivate: [RoleGuard],
    data: { roles: [UserRole.FREELANCER] },
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: FreelancerDashboardComponent },
      { path: 'profile', component: FreelancerProfileComponent },
      { path: 'applications', component: MyApplicationsComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FreelancerRoutingModule {}
