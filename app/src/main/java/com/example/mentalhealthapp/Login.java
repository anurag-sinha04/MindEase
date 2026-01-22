package com.example.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, pass;
    Button login;
    TextView signup;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> loginUser());

        signup.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Signup.class));
            finish();
        });
    }

    private void loginUser() {

        String mail = email.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (mail.isEmpty()) {
            email.setError("Enter email");
            return;
        }

        if (password.isEmpty()) {
            pass.setError("Enter password");
            return;
        }

        // LOCK UI
        progressBar.setVisibility(View.VISIBLE);
        login.setEnabled(false);
        signup.setEnabled(false);
        email.setEnabled(false);
        pass.setEnabled(false);

        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(task -> {

                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, screen1.class));
                        finish();
                    } else {
                        // UNLOCK UI
                        login.setEnabled(true);
                        signup.setEnabled(true);
                        email.setEnabled(true);
                        pass.setEnabled(true);

                        Toast.makeText(this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
