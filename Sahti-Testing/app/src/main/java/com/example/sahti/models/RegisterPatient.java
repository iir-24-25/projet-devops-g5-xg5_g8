package com.example.sahti.models;

public class RegisterPatient {
    private String nomPrenom;
    private String cin;
    private String dateNaissance;
    private String telephone;
    private String email;
    private String password;

    public RegisterPatient(String nomPrenom, String cin, String dateNaissance, String telephone, String email, String password) {
        this.nomPrenom = nomPrenom;
        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
    }

    // Getters and setters (optionnels si tu veux les utiliser avec Gson)
    public String getNomPrenom() { return nomPrenom; }
    public String getCin() { return cin; }
    public String getDateNaissance() { return dateNaissance; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
