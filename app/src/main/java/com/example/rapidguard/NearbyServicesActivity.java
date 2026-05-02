package com.example.rapidguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import java.util.Arrays;
import java.util.List;

public class NearbyServicesActivity extends AppCompatActivity {

    /** Simple data model for a nearby service */
    static class ServiceItem {
        String name, address, hours, distance, phone;
        int iconRes;
        String type;

        ServiceItem(String name, String address, String hours,
                    String distance, String phone,
                    int iconRes, String type) {
            this.name     = name;
            this.address  = address;
            this.hours    = hours;
            this.distance = distance;
            this.phone    = phone;
            this.iconRes  = iconRes;
            this.type     = type;
        }
    }

    private List<ServiceItem> services;

    // Card view IDs for the four included cards
    private final int[] cardIds = {
            R.id.cardPolice, R.id.cardHospital,
            R.id.cardFireStation, R.id.cardEMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_services);

        buildServiceData();
        bindServiceCards();
        setupChipFilter();
        setupBackButton();
        setupBottomNav();
    }

    private void buildServiceData() {
        services = Arrays.asList(
                new ServiceItem(
                        getString(R.string.service_police_name),
                        getString(R.string.service_police_address),
                        getString(R.string.service_police_hours),
                        getString(R.string.service_police_distance),
                        getString(R.string.service_police_number),
                        R.drawable.ic_responders_chip, "Police"),

                new ServiceItem(
                        getString(R.string.service_hospital_name),
                        getString(R.string.service_hospital_address),
                        getString(R.string.service_hospital_hours),
                        getString(R.string.service_hospital_distance),
                        getString(R.string.service_hospital_number),
                        R.drawable.ic_incident_medical, "Hospital"),

                new ServiceItem(
                        getString(R.string.service_fire_name),
                        getString(R.string.service_fire_address),
                        getString(R.string.service_fire_hours),
                        getString(R.string.service_fire_distance),
                        getString(R.string.service_fire_number),
                        R.drawable.ic_fire, "Fire Station"),

                new ServiceItem(
                        getString(R.string.service_ems_name),
                        getString(R.string.service_ems_address),
                        getString(R.string.service_ems_hours),
                        getString(R.string.service_ems_distance),
                        getString(R.string.service_ems_number),
                        R.drawable.ic_responders_chip, "EMS")
        );
    }

    /** Bind each service item into the included card views */
    private void bindServiceCards() {
        for (int i = 0; i < cardIds.length; i++) {
            View card = findViewById(cardIds[i]);
            ServiceItem item = services.get(i);
            bindCard(card, item);
        }
    }

    private void bindCard(View card, ServiceItem item) {
        android.widget.ImageView ivIcon =
                card.findViewById(R.id.ivServiceIcon);
        android.widget.TextView tvName =
                card.findViewById(R.id.tvServiceName);
        android.widget.TextView tvAddress =
                card.findViewById(R.id.tvServiceAddress);
        android.widget.TextView tvHours =
                card.findViewById(R.id.tvServiceHours);
        android.widget.TextView tvDistance =
                card.findViewById(R.id.tvDistance);
        MaterialButton btnCall =
                card.findViewById(R.id.btnCall);
        MaterialButton btnDirections =
                card.findViewById(R.id.btnDirections);

        ivIcon.setImageResource(item.iconRes);
        tvName.setText(item.name);
        tvAddress.setText(item.address);
        tvHours.setText(item.hours);
        tvDistance.setText(item.distance);

        // Call button
        btnCall.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.parse(item.phone))));

        // Directions — opens Google Maps
        btnDirections.setOnClickListener(v -> {
            Uri mapsUri = Uri.parse(
                    "geo:0,0?q=" + Uri.encode(item.name + ", " + item.address));
            Intent maps = new Intent(Intent.ACTION_VIEW, mapsUri);
            maps.setPackage("com.google.android.apps.maps");
            if (maps.resolveActivity(getPackageManager()) != null) {
                startActivity(maps);
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, mapsUri));
            }
        });
    }

    /** Filter cards by service type chip selection */
    private void setupChipFilter() {
        ChipGroup chips = findViewById(R.id.chipGroupServiceType);
        chips.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String filter = "All";
            if (!checkedIds.isEmpty()) {
                int id = checkedIds.get(0);
                if (id == R.id.chipPolice)      filter = "Police";
                else if (id == R.id.chipHospital)    filter = "Hospital";
                else if (id == R.id.chipFireStation) filter = "Fire Station";
                else if (id == R.id.chipEMS)         filter = "EMS";
            }
            applyFilter(filter);
        });
    }

    private void applyFilter(String filter) {
        for (int i = 0; i < cardIds.length; i++) {
            View card = findViewById(cardIds[i]);
            boolean show = filter.equals("All")
                    || services.get(i).type.equals(filter);
            card.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void setupBackButton() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_map);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home)
                startActivity(new Intent(this, HomeActivity.class));
            if (id == R.id.nav_incidents)
                startActivity(new Intent(this, IncidentHistoryActivity.class));
            if (id == R.id.nav_tips)
                startActivity(new Intent(this, SafetyTipsActivity.class));
            return true;
        });
    }
}