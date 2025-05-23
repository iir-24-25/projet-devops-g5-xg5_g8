package com.example.sahti.models;

public class Appointment {

    private String id;
    private String doctorId;
    private String patientId;
    private long date;
    private String timeSlot;
    private String status;
    private long createdAt;

    public Appointment() {
        this.createdAt = System.currentTimeMillis();
        this.status = "pending";
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}