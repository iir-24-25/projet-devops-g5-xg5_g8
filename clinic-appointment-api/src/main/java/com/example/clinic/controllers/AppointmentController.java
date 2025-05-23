package com.example.clinic.controllers;

import com.example.clinic.entities.AppointmentWithDoctor;
import com.example.clinic.entities.Patient;
import com.example.clinic.services.AppointmentService;
import com.example.clinic.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientRepository patientRepository;

    // Retourne uniquement les rendez-vous validés avec infos patient
    @GetMapping("/validated/{doctorId}")
    public ResponseEntity<List<AppointmentWithDoctor>> getValidatedAppointments(@PathVariable String doctorId) {
        List<AppointmentWithDoctor> appointments = appointmentService.findByDoctorIdAndStatus(doctorId, "validated");
        // ... éventuellement charger infos patient ici ...
        return ResponseEntity.ok(appointments);
    }

    // Les autres endpoints (save, delete, etc.) restent inchangés...
}
