package com.example.rapidguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

public class SosEmergencyActivity extends AppCompatActivity {

    private CardView btnSOS;
    private View pulseRing1, pulseRing2, pulseRing3;
    private TextView tvLocationStatus, tvSOSButtonText, tvHoldHint;
    private android.widget.ProgressBar holdProgressBar;
    private ChipGroup chipGroup;

    private CountDownTimer holdTimer;
    private static final long HOLD_DURATION_MS = 3000L;
    private boolean alertSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_emergency);

        bindViews();
        setupBackButton();
        setupSOSHoldButton();
        setupPulseAnimations();
        setupCallButton();
        simulateLocationAcquire();
    }

    private void bindViews() {
        btnSOS          = findViewById(R.id.btnSOSHold);
        pulseRing1      = findViewById(R.id.pulseRing1);
        pulseRing2      = findViewById(R.id.pulseRing2);
        pulseRing3      = findViewById(R.id.pulseRing3);
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        tvSOSButtonText = findViewById(R.id.tvSOSButtonText);
        tvHoldHint      = findViewById(R.id.tvHoldHint);
        holdProgressBar = findViewById(R.id.holdProgressBar);
        chipGroup       = findViewById(R.id.chipGroupEmergencyType);
    }

    private void setupBackButton() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> {
            Toast.makeText(this, R.string.sos_cancelled, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /** Hold-to-send: 3-second press triggers alert dispatch */
    private void setupSOSHoldButton() {
        btnSOS.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!alertSent) startHoldTimer();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    cancelHoldTimer();
                    break;
            }
            return true;
        });

        // Single tap fallback (accessibility)
        btnSOS.setOnClickListener(v -> {
            if (!alertSent) dispatchSOSAlert();
        });
    }

    private void startHoldTimer() {
        vibrateShort();
        holdProgressBar.setVisibility(View.VISIBLE);
        holdProgressBar.setProgress(0);

        holdTimer = new CountDownTimer(HOLD_DURATION_MS, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsed = HOLD_DURATION_MS - millisUntilFinished;
                int progress = (int) ((elapsed * 100) / HOLD_DURATION_MS);
                holdProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                holdProgressBar.setProgress(100);
                dispatchSOSAlert();
            }
        }.start();
    }

    private void cancelHoldTimer() {
        if (holdTimer != null) holdTimer.cancel();
        holdProgressBar.setVisibility(View.INVISIBLE);
        holdProgressBar.setProgress(0);
    }

    /** Fires the SOS — navigates to confirmation screen */
    private void dispatchSOSAlert() {
        alertSent = true;
        vibrateAlert();

        // Determine selected emergency type
        int chipId = chipGroup.getCheckedChipId();
        String emergencyType = getEmergencyTypeFromChip(chipId);

        // TODO: send alert via API (GPS coords + type + device/session ID)
        Toast.makeText(this, R.string.sos_sent, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, PostSosConfirmationActivity.class);
        intent.putExtra("emergency_type", emergencyType);
        startActivity(intent);
        finish();
    }

    private String getEmergencyTypeFromChip(int chipId) {
        if (chipId == R.id.chipMedical)  return "Medical";
        if (chipId == R.id.chipFire)     return "Fire";
        if (chipId == R.id.chipCrime)    return "Crime";
        if (chipId == R.id.chipAccident) return "Accident";
        return "Unknown";
    }

    /** Animate 3 concentric pulse rings */
    private void setupPulseAnimations() {
        startPulse(pulseRing1, 0,    2200);
        startPulse(pulseRing2, 600,  2200);
        startPulse(pulseRing3, 1200, 2200);
    }

    private void startPulse(View ring, long delay, long duration) {
        ScaleAnimation scale = new ScaleAnimation(
                0.88f, 1.12f, 0.88f, 1.12f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(duration);
        scale.setStartOffset(delay);
        scale.setRepeatCount(Animation.INFINITE);
        scale.setRepeatMode(Animation.REVERSE);
        ring.startAnimation(scale);
    }

    /** Simulate GPS acquiring then locking */
    private void simulateLocationAcquire() {
        new android.os.Handler().postDelayed(() -> {
            tvLocationStatus.setText(getString(R.string.sos_location_locked));
        }, 2000);
    }

    private void setupCallButton() {
        MaterialButton btnCall = findViewById(R.id.btnCallEmergency);
        btnCall.setOnClickListener(v -> {
            Intent call = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:10111"));
            startActivity(call);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("deprecation")
    private void vibrateShort() {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) {
            v.vibrate(VibrationEffect.createOneShot(80,
                    VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @SuppressWarnings("deprecation")
    private void vibrateAlert() {
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) {
            long[] pattern = {0, 200, 100, 200, 100, 400};
            v.vibrate(VibrationEffect.createWaveform(pattern, -1));
        }
    }
}
