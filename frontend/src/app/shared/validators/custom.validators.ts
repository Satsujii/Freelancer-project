import { FormGroup } from '@angular/forms';

export class CustomValidators {
  static passwordMatch(passwordKey: string, confirmPasswordKey: string) {
    return (formGroup: FormGroup) => {
      const password = formGroup.get(passwordKey);
      const confirmPassword = formGroup.get(confirmPasswordKey);

      if (!password || !confirmPassword) return null;

      if (password.value !== confirmPassword.value) {
        confirmPassword.setErrors({ passwordMismatch: true });
      } else {
        const errors = confirmPassword.errors;
        if (errors) {
          delete errors['passwordMismatch'];
          if (Object.keys(errors).length === 0) {
            confirmPassword.setErrors(null);
          } else {
            confirmPassword.setErrors(errors);
          }
        }
      }

      return null;
    };
  }
}
