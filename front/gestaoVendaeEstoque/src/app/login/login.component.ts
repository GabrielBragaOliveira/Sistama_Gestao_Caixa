import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { UsuarioLogin } from '../modelos/DTOs/UsuarioDTOs';
import { ToastModule } from "primeng/toast";
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    ToastModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  formLogin: FormGroup<{
    email: FormControl<string>; 
    senha: FormControl<string>
  }>;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.formLogin = this.fb.group({
      email: this.fb.control('', { validators: [Validators.required, Validators.email], nonNullable: true }),
      senha: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
    });
  }

  login(): void{
  if (this.formLogin.valid) {
    const loginData: UsuarioLogin = this.formLogin.getRawValue();
    this.authService.login(loginData);
  }
}
}
