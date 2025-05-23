package com.example.clinic.repositories;

import com.example.clinic.entities.AppointmentWithDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentWithDoctor, String> {

    List<AppointmentWithDoctor> findByDoctorId(String doctorId);

    List<AppointmentWithDoctor> findByPatientId(String patientId);
    List<AppointmentWithDoctor> findByDoctorIdAndStatus(String doctorId, String status);
}
