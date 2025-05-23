package com.example.sahti.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.R;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.example.sahti.models.LoginRequest;
import com.example.sahti.models.LoginResponse;
import com.example.sahti.models.SpecialiteActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ApiService apiService;
    private SharedPreferences sharedPref;

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView inscriptionButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.btn_log);
        inscriptionButton = findViewById(R.id.bt_inscrire1);

        inscriptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Inscrire_type.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);

            // Try patient login first
            apiService.loginPatient(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Patient login successful
                        saveSession(response.body(), "patient");
                        Toast.makeText(Login.this, "Connexion réussie en tant que patient", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, SpecialiteActivity.class)); // Patient screen
                        finish();
                    } else {
                        // If patient login failed, try doctor login
                        tryDoctorLogin(loginRequest);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Network error or other failure, still try doctor login
                    tryDoctorLogin(loginRequest);
                }
            });
        });
    }

    private void tryDoctorLogin(LoginRequest loginRequest) {
        apiService.loginDoctor(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Doctor login successful
                    saveSession(response.body(), "doctor");
                    Toast.makeText(Login.this, "Connexion réussie en tant que docteur", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, DoctorHome.class)); // Doctor screen - create this activity
                    finish();
                } else {
                    Toast.makeText(Login.this, "Échec de la connexion. Vérifiez vos identifiants.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSession(LoginResponse response, String role) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TOKEN", response.getToken());
        if ("patient".equals(role)) {
            editor.putString("PATIENT_ID", response.getUserId());  // Or getPatientId if you have that method
        } else if ("doctor".equals(role)) {
            editor.putString("DOCTOR_ID", response.getUserId());   // Or getDoctorId if available
        }
        editor.putString("ROLE", role);
        editor.apply();
    }
}
