package com.example.sahti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.adapters.DoctorsAdapter;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorsAdapter adapter;
    private EditText searchEditText;
    private List<DoctorModel> allDoctors = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docteurs);

        String speciality = getIntent().getStringExtra("SPECIALITY");
        if (speciality == null) {
            Toast.makeText(this, "Erreur: spécialité non spécifiée", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView titleText = findViewById(R.id.titleText);
        titleText.setText(speciality);

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterDoctors(s.toString());
            }
        });

        recyclerView = findViewById(R.id.doctorsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorsAdapter();
        recyclerView.setAdapter(adapter);

        // Appel réel au backend
        loadDoctorsFromServer(speciality);

        setupBottomNavigation();
    }

    private void filterDoctors(String query) {
        if (query.isEmpty()) {
            adapter.updateDoctors(allDoctors);
            return;
        }

        List<DoctorModel> filteredDoctors = new ArrayList<>();
        for (DoctorModel doctor : allDoctors) {
            if (doctor.getFullName().toLowerCase().contains(query.toLowerCase())
                    || doctor.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredDoctors.add(doctor);
            }
        }
        adapter.updateDoctors(filteredDoctors);
    }

    private void loadDoctorsFromServer(String speciality) {
        ApiService apiService = ApiClient.getApiService();
        Call<List<DoctorModel>> call = apiService.getDoctorsBySpeciality(speciality);

        call.enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(Call<List<DoctorModel>> call, Response<List<DoctorModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allDoctors = response.body();
                    if (allDoctors.isEmpty()) {
                        Toast.makeText(DoctorsActivity.this, "Aucun médecin trouvé pour cette spécialité", Toast.LENGTH_SHORT).show();
                    }
                    adapter.updateDoctors(allDoctors);
                } else {
                    Toast.makeText(DoctorsActivity.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
                Toast.makeText(DoctorsActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                return true; // déjà sur cette page
            } else {
                return false;
            }
        });

        bottomNav.setSelectedItemId(R.id.nav_profile);
    }
}
