package com.example.clinic.controllers;

import com.example.clinic.dto.DoctorDTO;
import com.example.clinic.entities.Doctor;
import com.example.clinic.services.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {
        if (doctorService.emailExists(doctor.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        Doctor saved = doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        DoctorDTO dto = new DoctorDTO(doctor);
        return ResponseEntity.ok(dto);
    }
}

