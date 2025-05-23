package com.example.clinic.controllers;

import com.example.clinic.dto.LoginRequest;
import com.example.clinic.dto.LoginResponse;
import com.example.clinic.dto.PatientDTO;
import com.example.clinic.entities.Patient;
import com.example.clinic.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    private String generateToken(String email) {
        return "token-for-" + email;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody PatientDTO request) {
        try {
            Patient p = patientService.registerPatient(request);
            return ResponseEntity.ok().body("Patient enregistré avec ID: " + p.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
