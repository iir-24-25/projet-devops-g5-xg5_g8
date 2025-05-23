package com.example.sahti.models;

public class Doctor {

    private Long id;            // même type que backend (Long)
    private String fullName;    // fullName au lieu de name
    private String inpe;
    private String specialty;
    private String address;
    private String email;
    private String password;
    private String phone;
    private String about;
    private String photo;

    public Doctor() {
    }

    // Constructeur complet (optionnel)
    public Doctor(Long id, String fullName, String inpe, String specialty, String address,
                  String email, String password, String phone, String about, String photo) {
        this.id = id;
        this.fullName = fullName;
        this.inpe = inpe;
        this.specialty = specialty;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.about = about;
        this.photo = photo;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInpe() {
        return inpe;
    }

    public void setInpe(String inpe) {
        this.inpe = inpe;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
