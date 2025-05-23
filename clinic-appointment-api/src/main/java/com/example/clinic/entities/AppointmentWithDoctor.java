package com.example.clinic.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "appointments")
public class AppointmentWithDoctor {

    @Id
    private String id;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_speciality")
    private String doctorSpeciality;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "date")
    private Long date;

    @Column(name = "time_slot")
    private String timeSlot;

    @Column(name = "status")
    private String status = "pending";

    @Column(name = "created_at")
    private Long createdAt = System.currentTimeMillis();

    // Transient field to hold patient info
    @Transient
    private Patient patient;

    public AppointmentWithDoctor() {}

    // Getters and Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorSpeciality() { return doctorSpeciality; }
    public void setDoctorSpeciality(String doctorSpeciality) { this.doctorSpeciality = doctorSpeciality; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public Long getDate() { return date; }
    public void setDate(Long date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}
