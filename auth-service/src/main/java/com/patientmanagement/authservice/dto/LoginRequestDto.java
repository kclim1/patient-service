package com.patientmanagement.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "email is required") @Email(message = "Email should be a valid email address") String email,
        @NotBlank(message = "password is required") @Size(min = 8 , message="Password must be at least 8 characters long ") String password
) {
}
