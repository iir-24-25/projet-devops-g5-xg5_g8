package com.example.clinic.repositories;

import com.example.clinic.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByEmail(String email);
    Optional<Doctor> findByEmail(String email);
    boolean existsByInpe(String inpe);
}