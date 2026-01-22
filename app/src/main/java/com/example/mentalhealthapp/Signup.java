package com.example.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText nm, email, mob, pass, cPass, age;
    MaterialAutoCompleteTextView gender;
    Button reg;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseApp.initializeApp(this);

        nm = findViewById(R.id.nm);
        email = findViewById(R.id.email);
        mob = findViewById(R.id.mob);
        pass = findViewById(R.id.pass);
        cPass = findViewById(R.id.cPass);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        reg = findViewById(R.id.reg);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String[] items = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, items);
        gender.setAdapter(adapter);

        reg.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String name = nm.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String mobile = mob.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String confirm = cPass.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String userGender = gender.getText().toString().trim();

        if (name.isEmpty()) { nm.setError("Enter name"); return; }
        if (mail.isEmpty()) { email.setError("Enter email"); return; }
        if (mobile.isEmpty()) { mob.setError("Enter mobile"); return; }
        if (userAge.isEmpty()) { age.setError("Enter age"); return; }
        if (userGender.isEmpty()) { gender.setError("Select gender"); return; }
        if (password.isEmpty()) { pass.setError("Enter password"); return; }
        if (confirm.isEmpty()) { cPass.setError("Confirm password"); return; }

        if (!password.equals(confirm)) {
            cPass.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        reg.setEnabled(false);
        setInputsEnabled(false);

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("email", mail);
                        user.put("mobile", mobile);
                        user.put("age", userAge);
                        user.put("gender", userGender);
                        user.put("password", password);

                        db.collection("users")
                                .document(mail)
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(Signup.this, "Account Created Successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Signup.this, screen1.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    unlockUI();
                                    Toast.makeText(Signup.this, "Database Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                    } else {
                        unlockUI();

                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            email.setError("Email already registered");
                        } else {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void unlockUI() {
        progressBar.setVisibility(View.GONE);
        reg.setEnabled(true);
        setInputsEnabled(true);
    }

    private void setInputsEnabled(boolean enabled) {
        nm.setEnabled(enabled);
        email.setEnabled(enabled);
        mob.setEnabled(enabled);
        pass.setEnabled(enabled);
        cPass.setEnabled(enabled);
        age.setEnabled(enabled);
        gender.setEnabled(enabled);
    }
}
