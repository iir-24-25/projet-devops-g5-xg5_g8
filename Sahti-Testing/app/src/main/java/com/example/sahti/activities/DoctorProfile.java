package com.example.sahti.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.R;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorProfile extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView nomText;
    private TextView inptText;
    private TextView specialitesText;
    private TextView adresseText;
    private TextView emailText;
    private TextView telephoneText;
    private TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        try {
            initializeViews();

            // TODO: Check if user is logged in via your own backend/session management
            // If not logged in, redirect to login activity:
            // Intent intent = new Intent(this, LoginActivity.class);
            // startActivity(intent);
            // finish();
            // return;

            loadDoctorData();

            setupButtons();
            setupBottomNavigation();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'initialisation: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() throws Exception {
        profileImageView = findViewById(R.id.profileImageView);
        nomText = findViewById(R.id.nomPrenomText);
        inptText = findViewById(R.id.inptText);
        specialitesText = findViewById(R.id.specialitesText);
        adresseText = findViewById(R.id.adresseText);
        emailText = findViewById(R.id.emailText);
        telephoneText = findViewById(R.id.telephoneText);
        aboutText = findViewById(R.id.aboutText);

        if (profileImageView == null || nomText == null || inptText == null ||
                specialitesText == null || adresseText == null || emailText == null ||
                telephoneText == null || aboutText == null) {
            throw new Exception("Une ou plusieurs vues sont manquantes dans le layout");
        }
    }

    private void setupButtons() {
        MaterialButton modifierButton = findViewById(R.id.modifierButton);
        MaterialButton supprimerButton = findViewById(R.id.supprimerButton);
        MaterialButton logoutButton = findViewById(R.id.logoutButton);

        modifierButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModifierDoctorProfile.class);
            startActivity(intent);
        });

        supprimerButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        logoutButton.setOnClickListener(v -> {
            // TODO: Implement logout logic (clear session/token)
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void loadDoctorData() {
        ApiService doctorApi = ApiClient.getClient().create(ApiService.class);

        // You should pass the real doctor ID here (e.g., from SharedPreferences or session)
        Long doctorId = 1L;

        doctorApi.getDoctorById(doctorId).enqueue(new Callback<DoctorModel>() {
            @Override
            public void onResponse(Call<DoctorModel> call, Response<DoctorModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DoctorModel doctor = response.body();

                    nomText.setText(doctor.getFullName());
                    inptText.setText(doctor.getInpe());
                    specialitesText.setText(doctor.getSpecialty());
                    adresseText.setText(doctor.getAddress());
                    emailText.setText(doctor.getEmail());
                    telephoneText.setText(doctor.getPhone());
                    aboutText.setText(doctor.getAbout());

                    if (doctor.getPhoto() != null && !doctor.getPhoto().isEmpty()) {
                        loadProfileImage(doctor.getPhoto());
                    }
                } else {
                    Toast.makeText(DoctorProfile.this, "Échec du chargement des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorModel> call, Throwable t) {
                Toast.makeText(DoctorProfile.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfileImage(String photoUrl) {
        try {
            if (photoUrl.startsWith("data:image")) {
                String base64Image = photoUrl.substring(photoUrl.indexOf("base64,") + 7);
                byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                profileImageView.setImageBitmap(bitmap);
            } else {
                // Load image from URL (network call on separate thread)
                new Thread(() -> {
                    try {
                        URL url = new URL(photoUrl);
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        new Handler(Looper.getMainLooper()).post(() -> profileImageView.setImageBitmap(bitmap));
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() ->
                                Toast.makeText(this, "Erreur de chargement de l'image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors du chargement de l'image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le compte")
                .setMessage("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.")
                .setPositiveButton("Supprimer", (dialog, which) -> deleteAccount())
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void deleteAccount() {
        // TODO: Implement backend call to delete doctor account

        // For demo just show success and redirect to login:
        Toast.makeText(this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctorData();
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
