import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/core/services/admin.service';
import { User } from 'src/app/core/models/user.model';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { UserEditDialogComponent } from './user-edit-dialog.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  loading = true;
  error: string | null = null;
  displayedColumns = ['id', 'name', 'email', 'role', 'enabled'];
  displayedColumnsWithActions = [...this.displayedColumns, 'actions'];

  constructor(private adminService: AdminService, private dialog: MatDialog, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.adminService.getAllUsers().subscribe({
      next: users => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load users';
        this.loading = false;
      }
    });
  }

  editUser(user: User): void {
    const dialogRef = this.dialog.open(UserEditDialogComponent, {
      width: '400px',
      data: { ...user }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.adminService.updateUser(user.id, result).subscribe({
          next: updated => {
            const idx = this.users.findIndex(u => u.id === updated.id);
            if (idx !== -1) this.users[idx] = updated;
            this.toastr.success('User updated successfully');
          },
          error: () => this.toastr.error('Failed to update user')
        });
      }
    });
  }

  deleteUser(user: User): void {
    if (confirm(`Are you sure you want to delete user ${user.email}?`)) {
      this.adminService.deleteUser(user.id).subscribe({
        next: () => {
          this.users = this.users.filter(u => u.id !== user.id);
          this.toastr.success('User deleted successfully');
        },
        error: () => this.toastr.error('Failed to delete user')
      });
    }
  }

  get filteredUsers(): User[] {
    return this.users.filter(u => u.role === 'FREELANCER' || u.role === 'CLIENT');
  }
} 