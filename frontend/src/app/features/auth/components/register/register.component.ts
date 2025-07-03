import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { CustomValidators } from 'src/app/shared/validators/custom.validators';
import { User, UserRole } from 'src/app/core/models/user.model';

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  role: UserRole;
  companyName?: string; // For clients
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  loading = false;
  hidePassword = true;
  hideConfirmPassword = true;
  selectedRole: UserRole = UserRole.FREELANCER;
  userRoles = UserRole;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {
    // Check if user is already logged in
    // if (this.authService.isAuthenticated()) {
    //   this.router.navigate(['/dashboard']);
    // }
  }

  ngOnInit(): void {
    this.createForm();
  }

  private createForm(): void {
    this.registerForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      role: [UserRole.FREELANCER, [Validators.required]],
      companyName: [''], // Optional for clients
      terms: [false, [Validators.requiredTrue]]
    }, {
      validators: CustomValidators.passwordMatch('password', 'confirmPassword')
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onRoleChange(role: UserRole): void {
    this.selectedRole = role;
    this.registerForm.patchValue({ role });
    
    // Update company name validation based on role
    const companyNameControl = this.registerForm.get('companyName');
    if (role === UserRole.CLIENT) {
      companyNameControl?.setValidators([Validators.required]);
    } else {
      companyNameControl?.clearValidators();
    }
    companyNameControl?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    const formValue = this.registerForm.value;
    
    const registerData: RegisterRequest = {
      name: formValue.name,
      email: formValue.email,
      password: formValue.password,
      role: formValue.role,
      ...(formValue.role === UserRole.CLIENT && { companyName: formValue.companyName })
    };

    this.authService.register(registerData).subscribe({
      next: (response) => {
        this.toastr.success('Registration successful!', 'Welcome');
        if (response && response.token && response.id && response.name && response.email && response.role) {
          console.log('Registration successful:', response);
          const user = {
            id: response.id,
            name: response.name,
            email: response.email,
            role: response.role as UserRole,
            enabled: true,
            createdAt: new Date(),
            updatedAt: new Date()
          };
          this.redirectUser(user);
        } else {
          this.toastr.error('Unexpected response from server.', 'Registration Error');
          console.error('Unexpected registration response:', response);
          this.loading = false;
        }
      },
      error: (error) => {
        this.loading = false;
        const errorMessage = error.error?.message || 'Registration failed. Please try again.';
        this.toastr.error(errorMessage, 'Registration Error');
        console.error('Registration error:', error);
      }
    });
  }

  private redirectUser(user: User): void {
    // Redirect based on user role
    switch (user.role) {
      case UserRole.FREELANCER:
        this.router.navigate(['/freelancer/profile/setup']);
        break;
      case UserRole.CLIENT:
        this.router.navigate(['/client/profile/setup']);
        break;
      default:
        this.router.navigate(['/dashboard']);
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.registerForm.controls).forEach(key => {
      this.registerForm.get(key)?.markAsTouched();
    });
  }

  navigateToLogin(): void {
    this.router.navigate(['/auth/login']);
  }
}