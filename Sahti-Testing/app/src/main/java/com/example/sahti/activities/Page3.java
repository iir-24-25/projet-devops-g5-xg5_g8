package com.example.sahti.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sahti.R;

public class Page3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge to edge display (equivalent to enableEdgeToEdge() in Kotlin)
        getWindow().getDecorView().setSystemUiVisibility(
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_page3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            int systemBars = WindowInsetsCompat.Type.systemBars();
            android.graphics.Insets systemInsets = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                systemInsets = insets.getInsets(systemBars).toPlatformInsets();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                v.setPadding(systemInsets.left, systemInsets.top, systemInsets.right, systemInsets.bottom);
            }
            return insets;
        });

        TextView bt2 = findViewById(R.id.bt2);
        bt2.setOnClickListener(v -> {
            Intent intent = new Intent(Page3.this, Login.class);
            startActivity(intent);
        });

        TextView bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(v -> {
            Intent intent = new Intent(Page3.this, Page2.class);
            startActivity(intent);
        });

        TextView bt_sauter = findViewById(R.id.bt_sauter);
        bt_sauter.setOnClickListener(v -> {
            Intent intent = new Intent(Page3.this, Login.class);
            startActivity(intent);
        });
    }
}
