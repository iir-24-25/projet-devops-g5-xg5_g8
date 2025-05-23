package com.example.sahti.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModifierDoctorProfile extends AppCompatActivity {

    private ImageView profileImageView;

    private TextInputEditText editAdresse;
    private TextInputEditText editEmail;
    private TextInputEditText editTelephone;
    private TextInputEditText editAbout;
    private TextInputEditText editPassword;
    private TextInputEditText editCurrentPassword;
    private MaterialButton saveButton;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_doctor_profile);

        profileImageView = findViewById(R.id.editProfileImageView);

        editAdresse = findViewById(R.id.editAdresse);
        editEmail = findViewById(R.id.editEmail);
        editTelephone = findViewById(R.id.editTelephone);
        editAbout = findViewById(R.id.editAbout);
        editPassword = findViewById(R.id.editPassword);
        editCurrentPassword = findViewById(R.id.editCurrentPassword);
        saveButton = findViewById(R.id.saveButton);

        // Load current data (simulate)
        loadCurrentData();

        saveButton.setOnClickListener(v -> {
            String currentPassword = editCurrentPassword.getText() != null ? editCurrentPassword.getText().toString() : "";

            if (currentPassword.isEmpty()) {
                Toast.makeText(ModifierDoctorProfile.this, "Veuillez entrer votre mot de passe actuel", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate password verification - replace with your own logic
            if (!verifyCurrentPassword(currentPassword)) {
                Toast.makeText(ModifierDoctorProfile.this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            updateDoctorProfile(
                    editAdresse.getText() != null ? editAdresse.getText().toString() : "",
                    editEmail.getText() != null ? editEmail.getText().toString() : "",
                    editTelephone.getText() != null ? editTelephone.getText().toString() : "",
                    editAbout.getText() != null ? editAbout.getText().toString() : "",
                    editPassword.getText() != null ? editPassword.getText().toString() : ""
            );
        });
    }

    private void loadCurrentData() {
        // Simulate loading from your backend or local storage
        // Replace these values with real data fetching
        String adresse = "123 Rue Exemple";
        String email = "doctor@example.com";
        String telephone = "+212600000000";
        String about = "Médecin spécialiste en cardiologie";
        String photoUrl = ""; // You can put a base64 string or URL here

        editAdresse.setText(adresse);
        editEmail.setText(email);
        editTelephone.setText(telephone);
        editAbout.setText(about);

        if (photoUrl != null && !photoUrl.isEmpty()) {
            loadProfileImage(photoUrl);
        }
    }

    private boolean verifyCurrentPassword(String currentPassword) {
        // Simulate password check (replace with real verification logic)
        // For example, compare with stored password hash or call your backend
        // Here we assume "password123" is the correct password for demo purposes
        return "password123".equals(currentPassword);
    }

    private void loadProfileImage(String photoUrl) {
        if (photoUrl.startsWith("data:image")) {
            // Base64 image
            try {
                String base64Image = photoUrl.substring(photoUrl.indexOf("base64,") + 7);
                byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                profileImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Erreur lors du chargement de l'image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Normal URL image
            executor.execute(() -> {
                try {
                    URL url = new URL(photoUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    mainHandler.post(() -> profileImageView.setImageBitmap(bitmap));
                } catch (Exception e) {
                    mainHandler.post(() -> Toast.makeText(ModifierDoctorProfile.this,
                            "Erreur de chargement de l'image: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void updateDoctorProfile(String adresse, String email, String telephone, String about, String newPassword) {
        // Simulate saving profile info and password update (replace with real backend call)

        boolean success = true; // Simulate success or failure

        if (success) {
            if (!newPassword.isEmpty()) {
                // Simulate password update
                boolean passwordUpdated = updatePassword(newPassword);
                if (passwordUpdated) {
                    Toast.makeText(this, "Profil et mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erreur lors de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean updatePassword(String newPassword) {
        // Simulate password update logic here (e.g., call backend API)
        // For demo, return true to simulate success
        return true;
    }
}
