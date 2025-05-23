package com.example.sahti.models;

public class RegisterDoctor {

    private String fullName;      // nom complet
    private String inpe;          // numéro INPE
    private String speciality;    // spécialité
    private String about;         // description / à propos
    private String phone;         // téléphone
    private String address;       // adresse
    private String email;         // email
    private String password;      // mot de passe
    private byte[] photo;         // photo as byte array

    public RegisterDoctor() {
    }

    public RegisterDoctor(String fullName, String inpe, String speciality, String about,
                          String phone, String address, String email, String password, byte[] photo) {
        this.fullName = fullName;
        this.inpe = inpe;
        this.speciality = speciality;
        this.about = about;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    // Getters et setters

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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
