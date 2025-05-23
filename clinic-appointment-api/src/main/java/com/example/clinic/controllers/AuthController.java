package com.example.clinic.controllers;

import com.example.clinic.dto.LoginRequest;
import com.example.clinic.dto.LoginResponse;
import com.example.clinic.entities.Patient;
import com.example.clinic.entities.Doctor;
import com.example.clinic.repositories.PatientRepository;
import com.example.clinic.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Doctor login
    @PostMapping("/doctors/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest request) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(request.getEmail());
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            if (passwordEncoder.matches(request.getPassword(), doctor.getPassword())) {
                return ResponseEntity.ok(new LoginResponse("token_doctor", doctor.getId().toString()));
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    // Patient login
    @PostMapping("/patients/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest request) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(request.getEmail());
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
                return ResponseEntity.ok(new LoginResponse("token_patient", patient.getId().toString()));
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

}
