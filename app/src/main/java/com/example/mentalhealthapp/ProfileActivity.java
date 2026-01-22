package com.example.mentalhealthapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Button btnEdit, btnLogout;
    TextView tvName, tvEmail, tvBio;
    ImageView imageView;

    String imageString = "";

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ActivityResultLauncher<Intent> editLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnEdit = findViewById(R.id.edit);
        btnLogout = findViewById(R.id.log);

        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);
        tvBio = findViewById(R.id.tvBioProfile);
        imageView = findViewById(R.id.iv_cp);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());

            // Fetch name from Firestore
            db.collection("users")
                    .document(Objects.requireNonNull(user.getEmail()))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            tvName.setText(name);
                        }
                    });
        }

        // Load Bio & Image from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bio = prefs.getString("bio", "Write your bio");
        imageString = prefs.getString("image", "");

        tvBio.setText(bio);

        if (!imageString.isEmpty()) {
            Glide.with(this).load(imageString).into(imageView);
        }

        // Result from Edit Profile
        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        imageString = result.getData().getStringExtra("image");
                        String newBio = result.getData().getStringExtra("Bio");

                        tvBio.setText(newBio);
                        Glide.with(ProfileActivity.this).load(imageString).into(imageView);
                    }
                });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
            intent.putExtra("Bio", tvBio.getText().toString());
            intent.putExtra("Name", tvName.getText().toString());
            intent.putExtra("image", imageString);
            editLauncher.launch(intent);
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, Login.class));
            finish();
        });
    }

    @Override
    public void finish() {
        // Save Bio & Image locally
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("bio", tvBio.getText().toString());
        editor.putString("image", imageString);
        editor.apply();

        super.finish();
    }
}
