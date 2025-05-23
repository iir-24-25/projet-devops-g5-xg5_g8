package com.example.clinic.repositories;

import com.example.clinic.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCin(String cin);
}
