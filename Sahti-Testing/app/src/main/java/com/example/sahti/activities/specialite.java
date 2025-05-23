package com.example.sahti.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.adapters.DoctorsAdapter;
import com.example.sahti.models.ApiClient;
import com.example.sahti.models.ApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class specialite extends AppCompatActivity {

    private EditText searchEditText;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_specialite);

        welcomeText = findViewById(R.id.welcomeText);

        setupBottomNavigation();
        setupSpecialityCards();

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.length() >= 3) {
                    searchDoctors(query);
                }
            }
        });

        loadUserName();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserName();
    }

    private void loadUserName() {
        String nomPrenom = "Utilisateur"; // TODO: Récupérer depuis API ou SharedPreferences
        String welcomeMessage = "<font face='sans-serif-medium' color='#673AB7'><b>" + nomPrenom + "</b></font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            welcomeText.setText(Html.fromHtml(welcomeMessage, Html.FROM_HTML_MODE_COMPACT));
        } else {
            welcomeText.setText(Html.fromHtml(welcomeMessage));
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
                return true; // déjà sur cette page
            } else {
                return false;
            }
        });

        bottomNav.setSelectedItemId(R.id.nav_profile);
    }

    private void setupSpecialityCards() {
        GridLayout gridLayout = findViewById(R.id.gridLayout2);
        String userName = welcomeText.getText().toString();

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof CardView) {
                CardView cardView = (CardView) child;
                cardView.setOnClickListener(view -> {
                    ViewGroup linearLayout = null;
                    if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
                        View firstChild = ((ViewGroup) view).getChildAt(0);
                        if (firstChild instanceof LinearLayout) {
                            linearLayout = (LinearLayout) firstChild;
                        }
                    }
                    TextView specialityTextView = null;
                    if (linearLayout != null) {
                        for (int j = 0; j < linearLayout.getChildCount(); j++) {
                            View v = linearLayout.getChildAt(j);
                            Object tag = v.getTag();
                            if (v instanceof TextView && tag != null && tag.equals("speciality")) {
                                specialityTextView = (TextView) v;
                                break;
                            }
                        }
                    }
                    String specialityText = (specialityTextView != null) ? specialityTextView.getText().toString() : "";

                    if (!specialityText.isEmpty()) {
                        Intent intent = new Intent(this, DoctorsActivity.class);
                        intent.putExtra("SPECIALITY", specialityText);
                        intent.putExtra("USER_NAME", userName);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void searchDoctors(String query) {
        ApiService apiService = ApiClient.getApiService();
        Call<List<DoctorModel>> call = apiService.searchDoctors(query);

        call.enqueue(new Callback<List<DoctorModel>>() {
            @Override
            public void onResponse(Call<List<DoctorModel>> call, Response<List<DoctorModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DoctorModel> doctors = response.body();
                    if (!doctors.isEmpty()) {
                        showSearchResults(doctors);
                    } else {
                        Toast.makeText(specialite.this, "Aucun médecin trouvé", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(specialite.this, "Erreur lors de la recherche", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DoctorModel>> call, Throwable t) {
                Toast.makeText(specialite.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSearchResults(List<DoctorModel> doctors) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Résultats de la recherche")
                .create();

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DoctorsAdapter adapter = new DoctorsAdapter();
        adapter.updateDoctors(doctors);
        recyclerView.setAdapter(adapter);

        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        recyclerView.setPadding(padding, padding, padding, padding);

        dialog.setView(recyclerView);
        dialog.show();
    }
}
