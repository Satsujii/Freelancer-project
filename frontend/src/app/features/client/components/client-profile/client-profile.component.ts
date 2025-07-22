import { Component, OnInit } from '@angular/core';
import { ClientProfileService } from 'src/app/core/services/client-profile.service';
import { ClientProfile } from 'src/app/core/models/client-profile.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client-profile',
  templateUrl: './client-profile.component.html',
  styleUrls: ['./client-profile.component.scss']
})
export class ClientProfileComponent implements OnInit {
  profile?: ClientProfile;
  loading = true;
  error?: string;
  editMode = false;
  profileForm!: FormGroup;
  saving = false;

  constructor(
    private clientProfileService: ClientProfileService,
    private fb: FormBuilder,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchProfile();
  }

  fetchProfile() {
    this.loading = true;
    this.clientProfileService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.loading = false;
        this.profileForm = this.fb.group({
          companyName: [profile.companyName],
          bio: [profile.bio],
          website: [profile.website]
        });
      },
      error: (err) => {
        this.error = 'Failed to load profile.';
        this.loading = false;
      }
    });
  }

  enableEdit() {
    this.editMode = true;
  }

  cancelEdit() {
    this.editMode = false;
    if (this.profile) {
      this.profileForm.patchValue({
        companyName: this.profile.companyName,
        bio: this.profile.bio,
        website: this.profile.website
      });
    }
  }

  saveProfile() {
    if (!this.profileForm.valid) return;
    this.saving = true;
    this.clientProfileService.updateProfile(this.profileForm.value).subscribe({
      next: (updated) => {
        this.profile = updated;
        this.editMode = false;
        this.saving = false;
      },
      error: () => {
        this.error = 'Failed to update profile.';
        this.saving = false;
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
