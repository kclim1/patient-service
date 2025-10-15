package com.patientmanagement.patientservice.mapper;

import com.patientmanagement.patientservice.dto.PatientRequestDTO;
import com.patientmanagement.patientservice.dto.PatientResponseDTO;
import com.patientmanagement.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth().toString()
        );
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.name());
        patient.setEmail(patientRequestDTO.email());
        patient.setAddress(patientRequestDTO.address());
        patient.setDateOfBirth( LocalDate.parse(patientRequestDTO.dateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.registeredDate()));
        return patient;
    }

}

//1.11.54
