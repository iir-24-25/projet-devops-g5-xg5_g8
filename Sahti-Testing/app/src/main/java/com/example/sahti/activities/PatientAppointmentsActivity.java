package com.example.sahti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.adapters.PatientAppointmentsAdapter;
import com.example.sahti.models.AppointmentWithDoctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientAppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private PatientAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        recyclerView = findViewById(R.id.appointmentsRecyclerView);
        emptyView = findViewById(R.id.emptyView);

        setupRecyclerView();
        loadAppointments();  // Ici on charge localement
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        adapter = new PatientAppointmentsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadAppointments() {
        List<AppointmentWithDoctor> appointmentsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            long date1 = sdf.parse("2025-06-01").getTime();
            long date2 = sdf.parse("2025-06-05").getTime();
            long createdAt1 = sdf.parse("2025-05-10").getTime();
            long createdAt2 = sdf.parse("2025-05-12").getTime();

            appointmentsList.add(new AppointmentWithDoctor(
                    "1", "doc1", "Dr. Dupont", "Cardiologie",
                    "patient123", date1, "10:00-11:00", "Confirmé", createdAt1
            ));
            appointmentsList.add(new AppointmentWithDoctor(
                    "2", "doc2", "Dr. Martin", "Dermatologie",
                    "patient123", date2, "14:00-15:00", "En attente", createdAt2
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Trier par date décroissante
        Collections.sort(appointmentsList, new Comparator<AppointmentWithDoctor>() {
            @Override
            public int compare(AppointmentWithDoctor a1, AppointmentWithDoctor a2) {
                return Long.compare(a2.getDate(), a1.getDate());  // trier du plus récent au plus ancien
            }
        });

        updateUI(appointmentsList);
    }

    private void updateUI(List<AppointmentWithDoctor> appointments) {
        if (appointments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.updateAppointments(appointments);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DoctorHome.class));
                return false;
            } else if (id == R.id.nav_appointments) {
                startActivity(new Intent(this, DoctorAppointmentsActivity.class));
                return false;
            } else if (id == R.id.nav_profile) {
                return true; // already here
            } else {
                return false;
            }
        });

        bottomNav.setSelectedItemId(R.id.nav_profile);
    }
}
