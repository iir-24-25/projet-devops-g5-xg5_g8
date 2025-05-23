package com.example.sahti.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.adapters.DaysAdapter;
import com.example.sahti.adapters.TimeSlotsAdapter;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailActivity extends AppCompatActivity {

    private DaysAdapter daysAdapter;
    private TimeSlotsAdapter timeSlotsAdapter;
    private Calendar selectedDate;
    private String selectedTimeSlot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        setupViews();
        setupCalendar();
        setupTimeSlots();
        setupBookingButton();

        Long doctorId = null;
        try {
            doctorId = Long.parseLong(getIntent().getStringExtra("DOCTOR_ID"));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID docteur invalide", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (doctorId != null) {
            loadDoctorData(doctorId);
        }

        setupBottomNavigation();
    }

    private void setupViews() {
        daysAdapter = new DaysAdapter(new DaysAdapter.OnDaySelectedListener() {
            @Override
            public void onDaySelected(Calendar day) {
                selectedDate = day;
                updateAvailableTimeSlots(day);
            }
        });
        RecyclerView daysRecyclerView = findViewById(R.id.daysRecyclerView);
        daysRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        daysRecyclerView.setAdapter(daysAdapter);

        timeSlotsAdapter = new TimeSlotsAdapter(new TimeSlotsAdapter.OnTimeSlotSelectedListener() {
            @Override
            public void onTimeSlotSelected(String timeSlot) {
                selectedTimeSlot = timeSlot;
            }
        });
        RecyclerView timeSlotsRecyclerView = findViewById(R.id.timeSlotsRecyclerView);
        timeSlotsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        timeSlotsRecyclerView.setAdapter(timeSlotsAdapter);
    }

    private void loadDoctorData(Long doctorId) {
        ApiService apiService = ApiClient.getApiService();
        Call<DoctorModel> call = apiService.getDoctorById(doctorId);  // <-- DoctorModel ici
        call.enqueue(new Callback<DoctorModel>() {
            @Override
            public void onResponse(Call<DoctorModel> call, Response<DoctorModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DoctorModel doctor = response.body();

                    TextView nameTv = findViewById(R.id.doctorName);
                    TextView specialityTv = findViewById(R.id.doctorSpeciality);
                    TextView descriptionTv = findViewById(R.id.doctorDescription);
                    TextView phoneTv = findViewById(R.id.doctorPhone);
                    TextView addressTv = findViewById(R.id.doctorAddress);

                    nameTv.setText(doctor.getFullName());
                    specialityTv.setText("Spécialité : " + doctor.getSpecialty());
                    descriptionTv.setText(doctor.getAbout() != null ? doctor.getAbout() : "Aucune description disponible");
                    phoneTv.setText("Téléphone : " + doctor.getPhone());
                    addressTv.setText("Adresse : " + doctor.getAddress());

                    loadDoctorPhoto(doctor.getPhoto());

                } else {
                    Toast.makeText(DoctorDetailActivity.this, "Erreur de récupération des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorModel> call, Throwable t) {
                Toast.makeText(DoctorDetailActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDoctorPhoto(String photoUrl) {
        ImageView imageView = findViewById(R.id.doctorImage);
        if (photoUrl != null) {
            try {
                if (photoUrl.startsWith("data:image")) {
                    String base64Image = photoUrl.substring(photoUrl.indexOf("base64,") + 7);
                    byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.default_profile);
                }
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.default_profile);
            }
        } else {
            imageView.setImageResource(R.drawable.default_profile);
        }
    }

    private void setupCalendar() {
        List<Calendar> days = new ArrayList<>();
        Calendar today = Calendar.getInstance();

        int i = 0;
        while (days.size() < 7) {
            Calendar day = (Calendar) today.clone();
            day.add(Calendar.DAY_OF_MONTH, i);

            if (day.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days.add(day);
            }
            i++;
        }

        daysAdapter.submitList(days);
    }

    private void setupTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        timeSlots.add("09:00");
        timeSlots.add("10:00");
        timeSlots.add("11:00");
        timeSlots.add("14:00");
        timeSlots.add("15:00");
        timeSlots.add("16:00");
        timeSlots.add("17:00");
        timeSlots.add("18:00");

        timeSlotsAdapter.submitList(timeSlots);
    }

    private void updateAvailableTimeSlots(Calendar date) {
        List<String> allTimeSlots = new ArrayList<>();
        allTimeSlots.add("09:00");
        allTimeSlots.add("10:00");
        allTimeSlots.add("11:00");
        allTimeSlots.add("14:00");
        allTimeSlots.add("15:00");
        allTimeSlots.add("16:00");
        allTimeSlots.add("17:00");
        allTimeSlots.add("18:00");

        timeSlotsAdapter.submitList(allTimeSlots);
    }

    private void setupBookingButton() {
        MaterialButton bookButton = findViewById(R.id.bookAppointmentButton);
        bookButton.setOnClickListener(v -> {
            if (selectedDate == null || selectedTimeSlot == null) {
                Toast.makeText(this, "Veuillez sélectionner une date et un horaire", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(DoctorDetailActivity.this, AppointmentConfirmationActivity.class);
            intent.putExtra("DOCTOR_ID", getIntent().getStringExtra("DOCTOR_ID"));
            intent.putExtra("DATE", selectedDate.getTimeInMillis());
            intent.putExtra("TIME_SLOT", selectedTimeSlot);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(DoctorDetailActivity.this, specialite.class));
                return true;
            } else if (id == R.id.nav_appointments) {
                startActivity(new Intent(DoctorDetailActivity.this, PatientAppointmentsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(DoctorDetailActivity.this, Profil.class));
                return true;
            } else {
                return false;
            }
        });
    }
}
