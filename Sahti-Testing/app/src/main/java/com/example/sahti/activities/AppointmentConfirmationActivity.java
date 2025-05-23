package com.example.sahti.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.R;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.example.sahti.models.Appointment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private ApiService apiService;
    private SharedPreferences sharedPref;

    private String doctorId;
    private long dateMillis;
    private String timeSlot;
    private String patientId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirmation);

        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE);

        doctorId = getIntent().getStringExtra("DOCTOR_ID");
        dateMillis = getIntent().getLongExtra("DATE", 0);
        timeSlot = getIntent().getStringExtra("TIME_SLOT");

        token = sharedPref.getString("TOKEN", "");
        patientId = sharedPref.getString("PATIENT_ID", "");

        if (doctorId == null || doctorId.isEmpty() || dateMillis == 0 || timeSlot == null || timeSlot.isEmpty()
                || token.isEmpty() || patientId.isEmpty()) {
            Toast.makeText(this, "Informations manquantes, veuillez vous reconnecter", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Button confirmButton = findViewById(R.id.confirmButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(v -> saveAppointment());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveAppointment() {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setDate(dateMillis);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus("pending");

        apiService.saveAppointment("Bearer " + token, appointment).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AppointmentConfirmationActivity.this, "Rendez-vous enregistré", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AppointmentConfirmationActivity.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Toast.makeText(AppointmentConfirmationActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}