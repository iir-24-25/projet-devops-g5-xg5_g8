package com.example.clinic.services;

import com.example.clinic.entities.AppointmentWithDoctor;
import com.example.clinic.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public List<AppointmentWithDoctor> findByDoctorIdAndStatus(String doctorId, String status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
    }



    // Create or Update appointment
    public AppointmentWithDoctor save(AppointmentWithDoctor appointment) {
        return appointmentRepository.save(appointment);
    }

    // Get appointment by id
    public Optional<AppointmentWithDoctor> findById(String id) {
        return appointmentRepository.findById(id);
    }

    // Get all appointments
    public List<AppointmentWithDoctor> findAll() {
        return appointmentRepository.findAll();
    }

    // Get appointments by doctor id
    public List<AppointmentWithDoctor> findByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Get appointments by patient id
    public List<AppointmentWithDoctor> findByPatientId(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Delete appointment by id
    public void deleteById(String id) {
        appointmentRepository.deleteById(id);
    }
}
