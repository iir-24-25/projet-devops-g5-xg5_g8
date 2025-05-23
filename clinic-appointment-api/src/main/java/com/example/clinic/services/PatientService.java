package com.example.clinic.services;

import com.example.clinic.dto.PatientDTO;
import com.example.clinic.entities.Patient;
import com.example.clinic.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Patient registerPatient(PatientDTO dto) {
        if (patientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        if (patientRepository.existsByCin(dto.getCin())) {
            throw new RuntimeException("CIN déjà utilisé");
        }

        Patient patient = new Patient();
        patient.setNomPrenom(dto.getNomPrenom());
        patient.setCin(dto.getCin());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setTelephone(dto.getTelephone());
        patient.setEmail(dto.getEmail());
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));



        return patientRepository.save(patient);
    }

    public boolean authenticatePatient(String email, String password) {
        Optional<Patient> p = patientRepository.findByEmail(email);
        return p.isPresent() && passwordEncoder.matches(password, p.get().getPassword());
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Optional<Patient> authenticate(String email, String password) {
        Optional<Patient> patient = patientRepository.findByEmail(email);
        if (patient.isPresent() && passwordEncoder.matches(password, patient.get().getPassword())) {
            return patient;
        }
        return Optional.empty();
    }

}
