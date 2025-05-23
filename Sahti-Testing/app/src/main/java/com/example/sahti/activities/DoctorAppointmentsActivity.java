package com.example.sahti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.adapters.DoctorAppointmentsAdapter;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.example.sahti.models.AppointmentWithDoctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ImageView emptyStateImage;
    private ProgressBar loadingIndicator;
    private DoctorAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        setupRecyclerView();
        setupBottomNavigation();
        loadValidatedAppointments();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.validatedAppointmentsRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        emptyStateImage = findViewById(R.id.emptyStateImage);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        adapter = new DoctorAppointmentsAdapter(
                appointment -> saveAppointmentToBackend(appointment, "validated"),
                appointment -> saveAppointmentToBackend(appointment, "rejected")
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadValidatedAppointments() {
        String doctorId = getDoctorIdFromPreferences();
        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Identifiant du médecin introuvable", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingIndicator.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getValidatedAppointmentsById(doctorId).enqueue(new Callback<List<AppointmentWithDoctor>>() {
            @Override
            public void onResponse(Call<List<AppointmentWithDoctor>> call, Response<List<AppointmentWithDoctor>> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(DoctorAppointmentsActivity.this, "Erreur de réponse du serveur", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<AppointmentWithDoctor>> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(DoctorAppointmentsActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveAppointmentToBackend(AppointmentWithDoctor appointment, String newStatus) {
        String token = getAuthTokenFromPreferences();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Utilisateur non authentifié", Toast.LENGTH_SHORT).show();
            return;
        }

        appointment.setStatus(newStatus);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.saveAppointment("Bearer " + token, appointment).enqueue(new Callback<AppointmentWithDoctor>() {
            @Override
            public void onResponse(Call<AppointmentWithDoctor> call, Response<AppointmentWithDoctor> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DoctorAppointmentsActivity.this, "Rendez-vous mis à jour", Toast.LENGTH_SHORT).show();
                    loadValidatedAppointments();
                } else {
                    Toast.makeText(DoctorAppointmentsActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppointmentWithDoctor> call, Throwable t) {
                Toast.makeText(DoctorAppointmentsActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(List<AppointmentWithDoctor> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyStateImage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            emptyStateImage.setVisibility(View.GONE);
            adapter.updateAppointments(appointments);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DoctorHome.class));
                finish();
                return true;
            } else if (id == R.id.nav_appointments) {
                startActivity(new Intent(this, DoctorAppointmentsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            } else {
                return false;
            }
        });

        bottomNav.setSelectedItemId(R.id.nav_profile);
    }

    private String getDoctorIdFromPreferences() {
        return getSharedPreferences("user_prefs", MODE_PRIVATE).getString("doctor_id", "");
    }

    private String getAuthTokenFromPreferences() {
        return getSharedPreferences("user_prefs", MODE_PRIVATE).getString("auth_token", "");
    }
}