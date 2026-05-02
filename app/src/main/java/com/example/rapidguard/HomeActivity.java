package com.example.rapidguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private View pulseRing1, pulseRing2;
    private final Handler pulseHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupGreeting();
        setupSOSButton();
        setupPulseAnimation();
        setupQuickActions();
        setupBottomNav();
    }

    /** Set dynamic greeting based on time of day */
    private void setupGreeting() {
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12)       greeting = getString(R.string.greeting_morning);
        else if (hour < 17)  greeting = getString(R.string.greeting_afternoon);
        else                 greeting = getString(R.string.greeting_evening);
        tvGreeting.setText(greeting);
    }

    /** SOS main button — navigates to SOS screen */
    private void setupSOSButton() {
        CardView btnSOS = findViewById(R.id.btnSOSMain);
        btnSOS.setOnClickListener(v ->
                startActivity(new Intent(this, SosEmergencyActivity.class)));
    }

    /** Animate pulse rings on the SOS button */
    private void setupPulseAnimation() {
        pulseRing1 = findViewById(R.id.pulseRing1);
        pulseRing2 = findViewById(R.id.pulseRing2);
        startPulse(pulseRing1, 0);
        startPulse(pulseRing2, 1000);
    }

    private void startPulse(View ring, long delay) {
        ScaleAnimation scale = new ScaleAnimation(
                0.85f, 1.1f, 0.85f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(2500);
        scale.setStartOffset(delay);
        scale.setRepeatCount(Animation.INFINITE);
        scale.setRepeatMode(Animation.REVERSE);
        ring.startAnimation(scale);
    }

    private void setupQuickActions() {
        findViewById(R.id.cardReportIncident).setOnClickListener(v ->
                startActivity(new Intent(this, ReportIncidentActivity.class)));

        findViewById(R.id.cardNearbyServices).setOnClickListener(v ->
                startActivity(new Intent(this, NearbyServicesActivity.class)));

        findViewById(R.id.cardIncidentHistory).setOnClickListener(v ->
                startActivity(new Intent(this, IncidentHistoryActivity.class)));

        findViewById(R.id.cardSafetyTips).setOnClickListener(v ->
                startActivity(new Intent(this, SafetyTipsActivity.class)));

        findViewById(R.id.cardRecentIncident).setOnClickListener(v ->
                startActivity(new Intent(this, IncidentDetailsActivity.class)));

        findViewById(R.id.tvViewAll).setOnClickListener(v ->
                startActivity(new Intent(this, IncidentHistoryActivity.class)));
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home)      return true;
            if (id == R.id.nav_incidents) startActivity(new Intent(this, IncidentHistoryActivity.class));
            if (id == R.id.nav_map)       startActivity(new Intent(this, NearbyServicesActivity.class));
            if (id == R.id.nav_tips)      startActivity(new Intent(this, SafetyTipsActivity.class));
            if (id == R.id.nav_profile)   startActivity(new Intent(this, LoginActivity.class));
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pulseHandler.removeCallbacksAndMessages(null);
    }
}