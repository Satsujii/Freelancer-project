import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { User, UserRole } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  hidePassword = true;
  returnUrl!: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
    
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  ngOnInit(): void {
    this.createForm();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
  }

  private createForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (response) => {
        if (response && response.token && response.id && response.name && response.email && response.role) {
          this.toastr.success(`Welcome, ${response.name}!`, 'Login successful!');
          console.log('Login successful:', response);
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
          this.toastr.error('Unexpected response from server.', 'Login Error');
          console.error('Unexpected response:', response);
          this.loading = false;
        }
      },
      error: (error) => {
        this.loading = false;
        const errorMessage = error.error?.message || 'Login failed. Please try again.';
        this.toastr.error(errorMessage, 'Login Error');
        console.error('Login error:', error);
      }
    });
  }

  private redirectUser(user: User): void {
    // Redirect based on user role
    switch (user.role) {
      case UserRole.ADMIN:
        this.router.navigate(['/admin/dashboard']);
        break;
      case UserRole.FREELANCER:
        this.router.navigate(['/freelancer/profile']);
        break;
      case UserRole.CLIENT:
        this.router.navigate(['/client/profile']);
        break;
      default:
        this.router.navigate(['/dashboard']);
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      this.loginForm.get(key)?.markAsTouched();
    });
  }

  navigateToRegister(): void {
    this.router.navigate(['/auth/register']);
  }

  navigateToForgotPassword(): void {
    this.router.navigate(['/auth/forgot-password']);
  }
}