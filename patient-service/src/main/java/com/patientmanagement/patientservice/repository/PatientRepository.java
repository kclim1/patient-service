package com.patientmanagement.patientservice.repository;

import com.patientmanagement.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient , UUID> {
}
