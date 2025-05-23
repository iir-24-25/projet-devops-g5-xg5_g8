package com.example.clinic.dto;

public class PatientDTO {

    private String nomPrenom;
    private String cin;
    private String dateNaissance;
    private String telephone;
    private String email;
    private String password;

    // Getters & Setters
    public String getNomPrenom() { return nomPrenom; }
    public void setNomPrenom(String nomPrenom) { this.nomPrenom = nomPrenom; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}