package com.example.clinic.services;


import com.example.clinic.dto.DoctorDTO;
import com.example.clinic.entities.Doctor;
import com.example.clinic.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Doctor registerDoctor(DoctorDTO dto) {
        if (doctorRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        if (doctorRepository.existsByInpe(dto.getInpe())) {
            throw new RuntimeException("INPE déjà utilisé");
        }

        Doctor doctor = new Doctor();
        doctor.setFullName(dto.getFullName());
        doctor.setInpe(dto.getInpe());
        doctor.setSpecialty(dto.getSpecialty());
        doctor.setAddress(dto.getAddress());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctor.setPhone(dto.getPhone());
        doctor.setAbout(dto.getAbout());

        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            byte[] photoBytes = Base64.getDecoder().decode(dto.getPhoto());
            doctor.setPhoto(Arrays.toString(photoBytes));
        }

        return doctorRepository.save(doctor);
    }


    public boolean emailExists(String email) {
        return doctorRepository.existsByEmail(email);
    }
    public Doctor saveDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docteur non trouvé avec l'id : " + id));
    }
}
