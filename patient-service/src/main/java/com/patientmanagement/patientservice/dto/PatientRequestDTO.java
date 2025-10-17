package com.patientmanagement.patientservice.dto;

import com.patientmanagement.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatientRequestDTO(
        @NotBlank(message = "name is required") @Size(max = 100, message = "name cannot exceed 100 characters") String name,
        @NotBlank(message = "email should be valid") @Email(message = "email should be valid") String email,
        @NotBlank(message = "address is required") String address,
        @NotBlank(message = "date of birth is required") String dateOfBirth,
        @NotBlank(groups= CreatePatientValidationGroup.class, message = "registered date is required") String registeredDate
        ) {
}

