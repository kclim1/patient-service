package com.patientmanagement.patientservice.service;

import com.patientmanagement.patientservice.dto.PatientRequestDTO;
import com.patientmanagement.patientservice.dto.PatientResponseDTO;
import com.patientmanagement.patientservice.exception.EmailAlreadyExistsException;
import com.patientmanagement.patientservice.exception.PatientNotFoundException;
import com.patientmanagement.patientservice.grpc.BillingServiceGrpcClient;
import com.patientmanagement.patientservice.kafka.KafkaProducer;
import com.patientmanagement.patientservice.mapper.PatientMapper;
import com.patientmanagement.patientservice.model.Patient;
import com.patientmanagement.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository , BillingServiceGrpcClient billingServiceGrpcClient , KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
//        query db for all patients
        List<Patient> patients = patientRepository.findAll();
//        map out the list of patients into ResponseDTOs
        List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();
        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists :" + patientRequestDTO.email());
        }
//        creates new patient in db
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
//        sends grpc req
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),newPatient.getName(), newPatient.getEmail());
//        sends message to kafka broker
        kafkaProducer.sendEvent(newPatient);
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatientById(UUID id ,  PatientRequestDTO patientRequestDTO){
        Patient existingPatient = patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("Patient not found with id: " + id));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.email(),id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists :" + patientRequestDTO.email());
        }
        existingPatient.setName(patientRequestDTO.name());
        existingPatient.setAddress(patientRequestDTO.address());
        existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));
        existingPatient.setEmail(patientRequestDTO.email());

        Patient updatedPatient = patientRepository.save(existingPatient);
        return PatientMapper.toDTO(updatedPatient);
    }
    public void deletePatientById(UUID id){
        patientRepository.deleteById(id);
    }

}
