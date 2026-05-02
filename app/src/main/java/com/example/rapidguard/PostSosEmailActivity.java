package com.example.rapidguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class PostSosEmailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sos_email);

        MaterialButton btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> finish());
    }
}