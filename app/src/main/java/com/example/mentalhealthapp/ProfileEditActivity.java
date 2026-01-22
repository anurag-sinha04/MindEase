package com.example.mentalhealthapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileEditActivity extends AppCompatActivity {

    EditText bioEdt;
    TextView nameTxt, emailTxt;
    ImageView profileImage;
    Button saveBtn;

    String imageUriString = "";

    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        nameTxt = findViewById(R.id.tvNameProfile);
        bioEdt = findViewById(R.id.tvBioProfile);
        emailTxt = findViewById(R.id.tvEmailProfile);
        profileImage = findViewById(R.id.iv_cp);
        saveBtn = findViewById(R.id.btnProfileSave);

        // Load Firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailTxt.setText(user.getEmail());
            emailTxt.setEnabled(false);
        }

        // Set name (read only)
        nameTxt.setText(getIntent().getStringExtra("Name"));
        nameTxt.setEnabled(false);

        // Set bio (editable)
        bioEdt.setText(getIntent().getStringExtra("Bio"));

        // Load image
        imageUriString = getIntent().getStringExtra("image");
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Glide.with(this).load(imageUriString).into(profileImage);
        }

        // Image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        imageUriString = imageUri.toString();
                        Glide.with(this).load(imageUriString).into(profileImage);
                    }
                });

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        saveBtn.setOnClickListener(v -> {
            String newBio = bioEdt.getText().toString().trim();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("Bio", newBio);
            resultIntent.putExtra("image", imageUriString);

            setResult(Activity.RESULT_OK, resultIntent);
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
