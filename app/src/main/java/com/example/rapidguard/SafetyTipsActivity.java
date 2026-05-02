package com.example.rapidguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SafetyTipsActivity extends AppCompatActivity {

    // Tip data model
    static class TipItem {
        String category, title, body;
        TipItem(String cat, String title, String body) {
            this.category = cat;
            this.title    = title;
            this.body     = body;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_tips);

        setupBackButton();
        bindTipCards();
        setupBottomNav();
    }

    private void setupBackButton() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void bindTipCards() {
        TipItem[] tips = {
                new TipItem(
                        getString(R.string.tip1_category),
                        getString(R.string.tip1_title),
                        getString(R.string.tip1_body)),
                new TipItem(
                        getString(R.string.tip2_category),
                        getString(R.string.tip2_title),
                        getString(R.string.tip2_body)),
                new TipItem(
                        getString(R.string.tip3_category),
                        getString(R.string.tip3_title),
                        getString(R.string.tip3_body)),
                new TipItem(
                        getString(R.string.tip4_category),
                        getString(R.string.tip4_title),
                        getString(R.string.tip4_body))
        };

        int[] cardIds = {
                R.id.tipCard1, R.id.tipCard2,
                R.id.tipCard3, R.id.tipCard4
        };

        for (int i = 0; i < cardIds.length; i++) {
            View card = findViewById(cardIds[i]);
            TipItem tip = tips[i];
            ((TextView) card.findViewById(R.id.tvTipCategory))
                    .setText(tip.category);
            ((TextView) card.findViewById(R.id.tvTipTitle))
                    .setText(tip.title);
            ((TextView) card.findViewById(R.id.tvTipBody))
                    .setText(tip.body);
        }
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_tips);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home)
                startActivity(new Intent(this, HomeActivity.class));
            if (id == R.id.nav_incidents)
                startActivity(new Intent(this,
                        IncidentHistoryActivity.class));
            if (id == R.id.nav_map)
                startActivity(new Intent(this,
                        NearbyServicesActivity.class));
            return true;
        });
    }
}