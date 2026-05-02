package com.example.rapidguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private View dotGreen;
    private boolean dotVisible = true;
    private final Handler blinkHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setupBrandName();
        setupPulseRings();
        setupDotBlink();
        setupButtons();
    }

    /** Color "Rapid" white and "Guard" red using SpannableString */
    private void setupBrandName() {
        TextView tvName = findViewById(R.id.tvAppName);
        SpannableString span = new SpannableString("RapidGuard");
        span.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.sos_core_red)),
                5, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvName.setText(span);
    }

    /** Animate pulse rings with scale + fade */
    private void setupPulseRings() {
        View inner = findViewById(R.id.pulseRingInner);
        View outer = findViewById(R.id.pulseRingOuter);
        startPulseAnimation(inner, 0);
        startPulseAnimation(outer, 800);
    }

    private void startPulseAnimation(View view, long delay) {
        ScaleAnimation scale = new ScaleAnimation(
                0.8f, 1.3f, 0.8f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(2800);
        scale.setStartOffset(delay);
        scale.setRepeatCount(Animation.INFINITE);
        scale.setRepeatMode(Animation.RESTART);
        view.startAnimation(scale);
    }

    /** Blink the green dot every 1.2s */
    private void setupDotBlink() {
        dotGreen = findViewById(R.id.dotGreen);
        blinkHandler.post(blinkRunnable);
    }

    private final Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            dotVisible = !dotVisible;
            dotGreen.setAlpha(dotVisible ? 1f : 0.25f);
            blinkHandler.postDelayed(this, 1200);
        }
    };

    private void setupButtons() {
        MaterialButton btnSOS = findViewById(R.id.btnSOS);
        MaterialButton btnGetStarted = findViewById(R.id.btnGetStarted);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        // SOS — no login required, goes straight to emergency
        btnSOS.setOnClickListener(v ->
                startActivity(new Intent(this, SosEmergencyActivity.class)));

        // Get Started — goes to home dashboard
        btnGetStarted.setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        // Login — goes to login screen
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        blinkHandler.removeCallbacks(blinkRunnable);
    }
}