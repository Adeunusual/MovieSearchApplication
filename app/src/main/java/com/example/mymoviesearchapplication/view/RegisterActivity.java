package com.example.mymoviesearchapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviesearchapplication.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * RegisterActivity allows new users to create an account using email and password.
 * Once registration is successful, users are redirected to the Login screen.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI elements
    private EditText emailEditText, passwordEditText;
    private Button registerButton, cancelButton;

    // Firebase instance
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Bind UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Handle Register button
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Input validation
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, getString(R.string.toast_input_missing), Toast.LENGTH_SHORT).show();
                return;
            }



            // Create user in Firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(this, getString(R.string.toast_registration_success), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish(); // close Register screen
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace(); // also print error to Logcat
                    });

        });

        // Handle Cancel button
        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}