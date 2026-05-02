package com.example.rapidguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class IncidentHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IncidentAdapter adapter;
    private View emptyState;
    private List<IncidentItem> allIncidents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_history);

        setupBackButton();
        setupData();
        setupRecycler();
        setupSearch();
        setupFilterChips();
        setupBottomNav();
    }

    private void setupBackButton() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    /** Seed sample incident data */
    private void setupData() {
        allIncidents = new ArrayList<>();
        allIncidents.add(new IncidentItem(
                "Medical Emergency", "Today, 07:14 AM",
                "Durban CBD", "Resolved", "High",
                R.drawable.ic_incident_medical));
        allIncidents.add(new IncidentItem(
                "Crime", "Yesterday, 22:30 PM",
                "Berea, Durban", "In Progress", "Critical",
                R.drawable.ic_crime));
        allIncidents.add(new IncidentItem(
                "Fire", "12 Apr 2025, 14:05",
                "Pinetown", "Resolved", "High",
                R.drawable.ic_fire));
        allIncidents.add(new IncidentItem(
                "Accident", "08 Apr 2025, 08:22",
                "N3 Highway", "Resolved", "Medium",
                R.drawable.ic_accident));
        allIncidents.add(new IncidentItem(
                "Medical Emergency", "01 Apr 2025, 11:00",
                "uMlazi", "Resolved", "Low",
                R.drawable.ic_incident_medical));
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.recyclerIncidents);
        emptyState   = findViewById(R.id.emptyState);

        adapter = new IncidentAdapter(allIncidents, incident ->
                startActivity(new Intent(this, IncidentDetailsActivity.class)));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        toggleEmptyState(allIncidents.isEmpty());
    }

    private void setupSearch() {
        TextInputEditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                filterIncidents(s.toString(), getActiveChipFilter());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilterChips() {
        ChipGroup chipGroup = findViewById(R.id.chipGroupFilter);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) ->
                filterIncidents(getSearchQuery(), getActiveChipFilter()));
    }

    private void filterIncidents(String query, String typeFilter) {
        List<IncidentItem> filtered = new ArrayList<>();
        for (IncidentItem item : allIncidents) {
            boolean matchesQuery = query.isEmpty()
                    || item.type.toLowerCase().contains(query.toLowerCase())
                    || item.location.toLowerCase().contains(query.toLowerCase());
            boolean matchesType = typeFilter.equals("All")
                    || item.type.equalsIgnoreCase(typeFilter)
                    || item.status.equalsIgnoreCase(typeFilter);
            if (matchesQuery && matchesType) filtered.add(item);
        }
        adapter.updateData(filtered);
        toggleEmptyState(filtered.isEmpty());
    }

    private String getSearchQuery() {
        TextInputEditText et = findViewById(R.id.etSearch);
        return et.getText() != null ? et.getText().toString() : "";
    }

    private String getActiveChipFilter() {
        ChipGroup group = findViewById(R.id.chipGroupFilter);
        int id = group.getCheckedChipId();
        if (id == R.id.chipFilterMedical) return "Medical";
        if (id == R.id.chipFilterFire)    return "Fire";
        if (id == R.id.chipFilterCrime)   return "Crime";
        if (id == R.id.chipFilterResolved) return "Resolved";
        return "All";
    }

    private void toggleEmptyState(boolean isEmpty) {
        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_incidents);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home)
                startActivity(new Intent(this, HomeActivity.class));
            if (id == R.id.nav_map)
                startActivity(new Intent(this, NearbyServicesActivity.class));
            if (id == R.id.nav_tips)
                startActivity(new Intent(this, SafetyTipsActivity.class));
            return true;
        });
    }
}