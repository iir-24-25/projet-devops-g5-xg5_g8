package com.example.sahti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profil extends AppCompatActivity {

    // Variables vues
    private TextView nomPrenomText;
    private TextView cinText;
    private TextView emailText;
    private TextView telephoneText;
    private TextView naissanceText;

    private static final int MODIFY_PROFILE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        nomPrenomText = findViewById(R.id.nomPrenomText);
        cinText = findViewById(R.id.cinText);
        emailText = findViewById(R.id.emailText);
        telephoneText = findViewById(R.id.telephoneText);
        naissanceText = findViewById(R.id.naissanceText);

        Button modifierButton = findViewById(R.id.modifierButton);
        Button supprimerButton = findViewById(R.id.supprimerButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        loadUserData();

        modifierButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModifierProfil.class);
            startActivityForResult(intent, MODIFY_PROFILE_REQUEST);
        });

        supprimerButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        logoutButton.setOnClickListener(v -> {
            // Ici tu dois faire ta logique de déconnexion selon ton backend
            // Exemple : nettoyer les données locales, token, etc.

            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        setupBottomNavigation();
    }

    private void loadUserData() {
        // TODO: Remplacer cette méthode par ta logique pour charger les données utilisateur
        // Par exemple depuis ta base locale ou ton backend REST API

        // Exemple statique fictif :
        nomPrenomText.setText("Jean Dupont");
        cinText.setText("AB123456");
        emailText.setText("jean.dupont@example.com");
        telephoneText.setText("0601020304");
        naissanceText.setText("01/01/1980");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_PROFILE_REQUEST && resultCode == RESULT_OK) {
            loadUserData();  // Recharger les données après modification
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
        // TODO: Remplacer par ta logique de suppression du compte utilisateur
        // Par exemple via une requête API vers ton backend

        // Exemple de simulation de succès :
        Toast.makeText(this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
