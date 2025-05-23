package com.example.sahti.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.sahti.R;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.example.sahti.models.Doctor;
import com.example.sahti.models.RegisterDoctor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Inscrire_doctor extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri selectedImageUri;
    private ImageView imgProfile;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrire_doctor);

        apiService = ApiClient.getClient().create(ApiService.class);

        imgProfile = findViewById(R.id.imgProfile);
        MaterialButton btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        MaterialButton btnInscrire = findViewById(R.id.btnInscrire);
        TextInputEditText editNom = findViewById(R.id.editNom);
        TextInputEditText editInpt = findViewById(R.id.editInpt);
        MultiAutoCompleteTextView editSpecialites = findViewById(R.id.editSpecialites);
        TextInputEditText editAdresse = findViewById(R.id.editAdresse);
        TextInputEditText editEmail = findViewById(R.id.editEmail);
        TextInputEditText editPassword = findViewById(R.id.editPassword);
        TextInputEditText editTelephone = findViewById(R.id.telephoneLayout);
        TextInputEditText editAPropos = findViewById(R.id.aProposLayout);
        MaterialCheckBox cbConditions = findViewById(R.id.cbConditions);

        // Spécialités
        String[] specialites = {"Généraliste", "Néphrologue", "Nutrisionniste", "Gastro-entérologie",
                "Dentaire", "Neurologie", "Ophtalmologie", "Orthopédie", "Pédiatrie", "ORL", "Pneumologie", "Cardiologie"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, specialites);
        editSpecialites.setAdapter(adapter);
        editSpecialites.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // Choisir une image
        btnSelectPhoto.setOnClickListener(v -> {
            if (checkPermission()) {
                pickImageFromGallery();
            } else {
                requestPermission();
            }
        });

        // Inscription
        btnInscrire.setOnClickListener(v -> {
            String nom = editNom.getText() != null ? editNom.getText().toString() : "";
            String inpt = editInpt.getText() != null ? editInpt.getText().toString() : "";
            String sp = editSpecialites.getText() != null ? editSpecialites.getText().toString() : "";
            String adresse = editAdresse.getText() != null ? editAdresse.getText().toString() : "";
            String email = editEmail.getText() != null ? editEmail.getText().toString() : "";
            String password = editPassword.getText() != null ? editPassword.getText().toString() : "";
            String telephone = editTelephone.getText() != null ? editTelephone.getText().toString() : "";
            String aPropos = editAPropos.getText() != null ? editAPropos.getText().toString() : "";
            String imageBase64 = selectedImageUri != null ? encodeImageToBase64(selectedImageUri) : "";

            if (!cbConditions.isChecked()) {
                Toast.makeText(this, "Vous devez accepter les conditions", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterDoctor request = new RegisterDoctor(
                    nom,
                    inpt,
                    sp,
                    aPropos,
                    telephone,
                    adresse,
                    email,
                    password,
                    imageBase64.getBytes()
            );

            Doctor doctor = new Doctor(null, nom, inpt, sp, adresse, email, password, telephone, aPropos, imageBase64);

            apiService.registerDoctor(doctor).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Inscrire_doctor.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Inscrire_doctor.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Inscrire_doctor.this, "Erreur: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Inscrire_doctor.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String encodeImageToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            return Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            selectedImageUri = data.getData();
            imgProfile.setImageURI(selectedImageUri);
        }
    }
}
