package com.example.sahti.models;

public class AppointmentWithPatient {
    private String id;
    private String doctorId;
    private String patientId;
    private String patientName;
    private long date;
    private String timeSlot;
    private String status;
    private long createdAt;

    public AppointmentWithPatient() {
        this.id = "";
        this.doctorId = "";
        this.patientId = "";
        this.patientName = "";
        this.date = 0L;
        this.timeSlot = "";
        this.status = "pending";
        this.createdAt = System.currentTimeMillis();
    }

    public AppointmentWithPatient(String id, String doctorId, String patientId, String patientName,
                                  long date, String timeSlot, String status, long createdAt) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters et Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
