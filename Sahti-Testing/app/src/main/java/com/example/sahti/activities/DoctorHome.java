package com.example.sahti.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.adapters.DoctorAppointmentsAdapter;
import com.example.sahti.models.AppointmentWithDoctor;
import com.example.sahti.models.AppointmentWithPatient;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DoctorHome extends AppCompatActivity {

    private DoctorAppointmentsAdapter appointmentsAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    // Remplacer par ta propre source de données (API REST etc.)
    // Exemple : private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        // Initialisation des vues
        recyclerView = findViewById(R.id.appointmentsRecyclerView);
        emptyView = findViewById(R.id.emptyAppointmentsView);

        setupAppointmentsRecyclerView();
        setupBottomNavigation();
        loadDoctorName();
        loadPendingAppointments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPendingAppointments();
    }

    private void setupAppointmentsRecyclerView() {
        appointmentsAdapter = new DoctorAppointmentsAdapter(
                new DoctorAppointmentsAdapter.OnValidateClickListener() {
                    @Override
                    public void onValidateClick(AppointmentWithDoctor appointment) {
                        validateAppointment(appointment);
                    }
                },
                new DoctorAppointmentsAdapter.OnRejectClickListener() {
                    @Override
                    public void onRejectClick(AppointmentWithDoctor appointment) {
                        rejectAppointment(appointment);
                    }
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appointmentsAdapter);
    }


    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // rester sur la page actuelle
                return true;
            } else if (id == R.id.nav_appointments) {
                startActivity(new Intent(this, DoctorAppointmentsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, DoctorProfile.class));
                return true;
            }
            return false;
        });
    }

    // Méthode simulant le chargement du nom du docteur (à remplacer par appel API)
    private void loadDoctorName() {
        TextView welcomeText = findViewById(R.id.welcomeDoctorText);

        // Simulation d'un nom récupéré
        String doctorName = "Dupont";

        if (!doctorName.isEmpty()) {
            welcomeText.setText("Welcome Dr. " + doctorName);
        } else {
            welcomeText.setText("Welcome Doctor");
        }
    }

    // Simulation du chargement des rendez-vous en attente (à remplacer par appel API)
    private void loadPendingAppointments() {
        List<AppointmentWithDoctor> appointmentsList = new ArrayList<>();

        // Exemple d'ajout d'un rendez-vous fictif
        // appointmentsList.add(new AppointmentWithDoctor("1", "docId", "Doctor Name", "Specialty", "patId", 1684588800000L, "10:00", "pending", System.currentTimeMillis()));

        updateUI(appointmentsList);
    }

    private void validateAppointment(AppointmentWithDoctor appointment) {
        // Ici, appeler API pour valider le rendez-vous
        Toast.makeText(this, "Rendez-vous validé (simulation)", Toast.LENGTH_SHORT).show();
        // Actualiser la liste après validation
        loadPendingAppointments();
    }

    private void rejectAppointment(AppointmentWithDoctor appointment) {
        // Ici, appeler API pour rejeter le rendez-vous
        Toast.makeText(this, "Rendez-vous refusé (simulation)", Toast.LENGTH_SHORT).show();
        // Actualiser la liste après rejet
        loadPendingAppointments();
    }

    private void updateUI(List<AppointmentWithDoctor> appointments) {
        ImageView emptyStateImage = findViewById(R.id.emptyStateImage);

        if (appointments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyStateImage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            emptyStateImage.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appointments.sort((a, b) -> Long.compare(b.getDate(), a.getDate()));
            }
            appointmentsAdapter.updateAppointments(appointments);
        }
    }
}
