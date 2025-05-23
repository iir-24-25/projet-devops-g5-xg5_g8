package com.example.sahti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.example.sahti.R;

public class Inscrire_type extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge content (like Kotlin's enableEdgeToEdge())
        getWindow().getDecorView().setSystemUiVisibility(
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        setContentView(R.layout.activity_inscrire_type);

        // Adjust padding for system bars using WindowInsetsCompat
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(android.view.View v, WindowInsetsCompat insets) {
                Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
                return insets;
            }
        });

        TextView bt_back3 = findViewById(R.id.bt_back3);
        bt_back3.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

        TextView bt_inscrire_patient = findViewById(R.id.bt_inscrire_patient);
        bt_inscrire_patient.setOnClickListener(v -> {
            Intent intent = new Intent(this, Inscrire_patient.class);
            startActivity(intent);
        });

        TextView bt_inscrire_doctor = findViewById(R.id.bt_inscrire_doctor);
        bt_inscrire_doctor.setOnClickListener(v -> {
            Intent intent = new Intent(this, Inscrire_doctor.class);
            startActivity(intent);
        });
    }
}
