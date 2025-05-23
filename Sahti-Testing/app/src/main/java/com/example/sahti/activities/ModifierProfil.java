package com.example.sahti.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sahti.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ModifierProfil extends AppCompatActivity {

    private TextInputEditText editNomPrenom, editCin, editEmail, editTelephone, editNaissance, editPassword, editCurrentPassword;
    private MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);

        // Initialisation des champs
        editNomPrenom = findViewById(R.id.editNomPrenom);
        editCin = findViewById(R.id.editCin);
        editEmail = findViewById(R.id.editEmail);
        editTelephone = findViewById(R.id.editTelephone);
        editNaissance = findViewById(R.id.editNaissance);
        editPassword = findViewById(R.id.editPassword);
        editCurrentPassword = findViewById(R.id.editCurrentPassword);
        saveButton = findViewById(R.id.saveButton);

        // Charger les données utilisateur (remplacer par votre source de données)
        loadUserData();

        saveButton.setOnClickListener(v -> {
            String currentPassword = editCurrentPassword.getText() != null ? editCurrentPassword.getText().toString() : "";

            if (currentPassword.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer votre mot de passe actuel", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier le mot de passe actuel (ici juste un exemple local, à adapter)
            if (!checkCurrentPassword(currentPassword)) {
                Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si mot de passe correct, mettre à jour les données
            processUpdates();
        });
    }

    private void loadUserData() {
        // TODO: Remplacer cette méthode par chargement réel depuis backend ou base locale

        // Exemple de données par défaut
        editNomPrenom.setText("Jean Dupont");
        editCin.setText("AB123456");
        editEmail.setText("jean.dupont@example.com");
        editTelephone.setText("0612345678");
        editNaissance.setText("01/01/1980");
    }

    private boolean checkCurrentPassword(String password) {
        // TODO: Remplacer par vérification réelle (backend, base locale chiffrée, etc.)
        // Ici, exemple statique : mot de passe actuel = "password123"
        return "password123".equals(password);
    }

    private void processUpdates() {
        String newEmail = editEmail.getText() != null ? editEmail.getText().toString() : "";
        String newPassword = editPassword.getText() != null ? editPassword.getText().toString() : "";

        boolean emailChanged = !newEmail.equals("jean.dupont@example.com");  // à adapter dynamiquement
        boolean passwordChanged = !newPassword.isEmpty();

        if (emailChanged && passwordChanged) {
            updateEmailAndPassword(newEmail, newPassword);
        } else if (emailChanged) {
            updateEmail(newEmail);
        } else if (passwordChanged) {
            updatePassword(newPassword);
        } else {
            updateUserData();
        }
    }

    private void updateUserData() {
        // TODO: Sauvegarder les données dans votre backend ou base locale
        Toast.makeText(this, "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateEmail(String newEmail) {
        // TODO: Mettre à jour email dans backend, ici juste exemple
        Toast.makeText(this, "Email mis à jour avec succès", Toast.LENGTH_SHORT).show();
        updateUserData();
    }

    private void updatePassword(String newPassword) {
        // TODO: Mettre à jour mot de passe dans backend, ici juste exemple
        Toast.makeText(this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
        updateUserData();
    }

    private void updateEmailAndPassword(String newEmail, String newPassword) {
        // TODO: Mettre à jour email et mot de passe dans backend, ici juste exemple
        Toast.makeText(this, "Email et mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
        updateUserData();
    }
}
