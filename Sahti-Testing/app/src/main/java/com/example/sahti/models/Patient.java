package com.example.sahti.models;

import java.util.Date;

public class Patient {
    private String id;
    private String nomPrenom;
    private String cin;
    private Date dateNaissance;
    private String telephone;
    private String email;
    private String password;
    private boolean conditionsAcceptees;

    // Constructeur vide nécessaire pour plusieurs bibliothèques
    public Patient() {
    }

    // Constructeur avec tous les champs
    public Patient(String id, String nomPrenom, String cin, Date dateNaissance,
                   String telephone, String email, String password, boolean conditionsAcceptees) {
        this.id = id;
        this.nomPrenom = nomPrenom;
        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.conditionsAcceptees = conditionsAcceptees;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public boolean isConditionsAcceptees() {
        return conditionsAcceptees;
    }

    public void setConditionsAcceptees(boolean conditionsAcceptees) {
        this.conditionsAcceptees = conditionsAcceptees;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", nomPrenom='" + nomPrenom + '\'' +
                ", cin='" + cin + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", conditionsAcceptees=" + conditionsAcceptees +
                '}';
    }
}