package com.example.sahti.models;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.activities.DoctorModel;
import com.example.sahti.activities.DoctorsActivity;
import com.example.sahti.activities.PatientAppointmentsActivity;
import com.example.sahti.activities.Profil;
import com.example.sahti.adapters.DoctorsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialiteActivity extends AppCompatActivity {

    private EditText searchEditText;
    private TextView welcomeText;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialite);

        welcomeText = findViewById(R.id.welcomeText);
        searchEditText = findViewById(R.id.searchEditText);
        gridLayout = findViewById(R.id.gridLayout2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            int systemBars = WindowInsetsCompat.Type.systemBars();
            v.setPadding(insets.getInsets(systemBars).left, insets.getInsets(systemBars).top,
                    insets.getInsets(systemBars).right, insets.getInsets(systemBars).bottom);
            return insets;
        });

        setupBottomNavigation();
        setupSpecialityCards();
        loadUserName();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (query.length() >= 3) {
                    searchDoctors(query);
                }
            }
        });
    }

    private void loadUserName() {

        String userName = "Icame Badr"; // par exemple
        String welcomeMessage = "<b><br><font color='#673AB7'>" + userName + "</font></b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            welcomeText.setText(Html.fromHtml(welcomeMessage, Html.FROM_HTML_MODE_COMPACT));
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_appointments) {
                startActivity(new Intent(this, PatientAppointmentsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, Profil.class));
                return true;
            }
            return false;
        });
    }

    private void setupSpecialityCards() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof CardView) {
                CardView cardView = (CardView) child;
                cardView.setOnClickListener(view -> {
                    LinearLayout layout = (LinearLayout) ((CardView) view).getChildAt(0);
                    TextView specialityTextView = layout.findViewWithTag("speciality");
                    if (specialityTextView != null) {
                        String speciality = specialityTextView.getText().toString();
                        Intent intent = new Intent(SpecialiteActivity.this, DoctorsActivity.class);
                        intent.putExtra("SPECIALITY", speciality);
                        intent.putExtra("USER_NAME", welcomeText.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void searchDoctors(String query) {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.searchDoctors(query).enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(Call<List<DoctorModel>> call, Response<List<DoctorModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DoctorModel> doctors = response.body();
                    if (!doctors.isEmpty()) {
                        showSearchResults(doctors);
                    } else {
                        Toast.makeText(SpecialiteActivity.this, "Aucun docteur trouvé", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SpecialiteActivity.this, "Erreur lors de la recherche", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
                Toast.makeText(SpecialiteActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSearchResults(List<DoctorModel> doctors) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Résultats de la recherche");

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DoctorsAdapter adapter = new DoctorsAdapter();
        adapter.updateDoctors(doctors);
        recyclerView.setAdapter(adapter);
        recyclerView.setPadding(16, 16, 16, 16);

        builder.setView(recyclerView);
        builder.setNegativeButton("Fermer", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
