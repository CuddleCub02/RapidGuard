package com.example.rapidguard;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;

public class IncidentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_details);

        populateFromIntent();
        setupButtons();
    }

    /** Populate fields from intent extras (passed from IncidentAdapter) */
    private void populateFromIntent() {
        String type      = getIntent().getStringExtra("incident_type");
        String ref       = getIntent().getStringExtra("incident_ref");
        String status    = getIntent().getStringExtra("incident_status");
        String riskLevel = getIntent().getStringExtra("risk_level");
        String dateTime  = getIntent().getStringExtra("date_time");
        String location  = getIntent().getStringExtra("location");

        if (type != null) {
            TextView tvType = findViewById(R.id.tvIncidentType);
            tvType.setText(type);
        }
        if (ref != null) {
            TextView tvRef = findViewById(R.id.tvRefNumber);
            tvRef.setText(ref);
        }
        if (status != null) {
            TextView tvStatus = findViewById(R.id.tvStatusBadge);
            tvStatus.setText(status);
            applyStatusStyle(tvStatus, status);
        }
        if (riskLevel != null) {
            TextView tvRisk = findViewById(R.id.tvRiskBadge);
            tvRisk.setText(riskLevel);
        }
        if (dateTime != null) {
            TextView tvDt = findViewById(R.id.tvDateTime);
            tvDt.setText(dateTime);
        }
        if (location != null) {
            TextView tvLoc = findViewById(R.id.tvLocation);
            tvLoc.setText(location);
        }
    }

    private void applyStatusStyle(TextView tv, String status) {
        int color, bg;
        switch (status) {
            case "Resolved":
                color = R.color.status_success;
                bg    = R.drawable.bg_status_success;
                break;
            case "In Progress":
                color = R.color.status_warning;
                bg    = R.drawable.bg_status_warning;
                break;
            default:
                color = R.color.status_danger;
                bg    = R.drawable.bg_status_danger;
        }
        tv.setTextColor(ContextCompat.getColor(this, color));
        tv.setBackgroundResource(bg);
    }

    private void setupButtons() {
        // Back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Share
        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> shareReport());

        // Download PDF
        MaterialButton btnDownload = findViewById(R.id.btnDownloadReport);
        btnDownload.setOnClickListener(v ->
                startActivity(new Intent(this, DownloadReportActivity.class)));

        // Share report
        MaterialButton btnShareReport = findViewById(R.id.btnShareReport);
        btnShareReport.setOnClickListener(v -> shareReport());
    }

    private void shareReport() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.share_subject));
        share.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_body,
                        findViewById(R.id.tvRefNumber) instanceof TextView
                                ? ((TextView) findViewById(R.id.tvRefNumber)).getText()
                                : ""));
        startActivity(Intent.createChooser(share,
                getString(R.string.share_chooser_title)));
    }
}