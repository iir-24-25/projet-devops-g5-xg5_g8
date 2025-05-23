package com.example.sahti.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.databinding.ActivityInscrirePatientBinding;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.example.sahti.models.RegisterPatient;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Inscrire_patient extends AppCompatActivity {

    private ActivityInscrirePatientBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInscrirePatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btInscrire.setOnClickListener(v -> {
            String nomPrenom = binding.etNomPrenom.getText().toString();
            String cin = binding.etCin.getText().toString();
            String naissance = binding.etNaissance.getText().toString();
            String telephone = binding.etTelephone.getText().toString();
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            boolean conditionsAccepted = binding.cbConditions.isChecked();

            if (nomPrenom.isEmpty() || cin.isEmpty() || naissance.isEmpty() || telephone.isEmpty() ||
                    email.isEmpty() || password.isEmpty() || !conditionsAccepted) {
                Toast.makeText(this,
                        "Veuillez remplir tous les champs et accepter les conditions",
                        Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Call your backend API to register patient
                registerPatient(nomPrenom, cin, naissance, telephone, email, password);
            }
        });

        binding.etNaissance.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    binding.etNaissance.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void registerPatient(String nomPrenom, String cin, String naissance, String telephone, String email, String password) {
        RegisterPatient request = new RegisterPatient(nomPrenom, cin, naissance, telephone, email, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.registerPatient(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Inscrire_patient.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Inscrire_patient.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Inscrire_patient.this, "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Inscrire_patient.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
