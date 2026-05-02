package com.example.rapidguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PostSosConfirmationActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sos_confirmation);

        populateIncidentData();
        setupPulseAnimation();
        setupButtons();
    }

    /** Populate incident summary with passed data + generated ref */
    private void populateIncidentData() {
        // Incident reference — generated uniquely per alert
        String ref = "REF: #RG-" + (10000 + new Random().nextInt(89999));
        TextView tvRef     = findViewById(R.id.tvRefNumber);
        TextView tvRefBadge = findViewById(R.id.tvIncidentRef);
        tvRef.setText(ref);
        tvRefBadge.setText(ref);

        // Emergency type passed from SosEmergencyActivity
        String type = getIntent().getStringExtra("emergency_type");
        if (type == null || type.isEmpty()) type = "Unknown";
        TextView tvType = findViewById(R.id.tvEmergencyType);
        tvType.setText(type);

        // Timestamp
        String timestamp = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date());
        TextView tvTime = findViewById(R.id.tvTimestamp);
        tvTime.setText(timestamp);
    }

    /** Animate green pulse rings around hero check icon */
    private void setupPulseAnimation() {
        View heroPulse1 = findViewById(R.id.heroPulse1);
        View heroPulse2 = findViewById(R.id.heroPulse2);
        startPulse(heroPulse1, 0);
        startPulse(heroPulse2, 700);
    }

    private void startPulse(View ring, long delay) {
        ScaleAnimation scale = new ScaleAnimation(
                0.9f, 1.1f, 0.9f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(2000);
        scale.setStartOffset(delay);
        scale.setRepeatCount(Animation.INFINITE);
        scale.setRepeatMode(Animation.REVERSE);
        ring.startAnimation(scale);
    }

    private void setupButtons() {
        // Home button
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });

        // Add more details → Emergency Reporting screen
        MaterialButton btnDetails = findViewById(R.id.btnAddDetails);
        btnDetails.setOnClickListener(v ->
                startActivity(new Intent(this, PostSosConfirmationActivity.class)));

        // Enter email for report → email capture screen
        MaterialButton btnEmail = findViewById(R.id.btnEnterEmail);
        btnEmail.setOnClickListener(v ->
                startActivity(new Intent(this, PostSosEmailCaptureActivity.class)));

        // Track live → incident tracking screen
        MaterialButton btnTrack = findViewById(R.id.btnTrackLive);
        btnTrack.setOnClickListener(v ->
                startActivity(new Intent(this, PostSosEmailActivity.class)));

        // Call 10111
        MaterialButton btnCall = findViewById(R.id.btnCallNow);
        btnCall.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:10111"))));
    }
}