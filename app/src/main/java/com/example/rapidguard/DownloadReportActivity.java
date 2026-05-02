package com.example.rapidguard;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DownloadReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_report);

        populateFromIntent();
        setupButtons();
    }

    private void populateFromIntent() {
        String ref = getIntent().getStringExtra("incident_ref");
        if (ref != null) {
            android.widget.TextView tvRef =
                    findViewById(R.id.tvReportRef);
            tvRef.setText(ref);
        }
    }

    private void setupButtons() {

        // Back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Download PDF
        MaterialButton btnDownload = findViewById(R.id.btnDownloadPDF);
        btnDownload.setOnClickListener(v -> downloadPDF());

        // Share
        MaterialButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> shareReport());

        // Send to email
        MaterialButton btnEmail = findViewById(R.id.btnSendEmail);
        btnEmail.setOnClickListener(v -> sendToEmail());
    }

    /** Trigger system DownloadManager to save PDF */
    private void downloadPDF() {
        try {
            // TODO: replace with real PDF URL from backend
            String pdfUrl = "https://rapidguard.app/reports/RG-84721.pdf";

            DownloadManager.Request request =
                    new DownloadManager.Request(Uri.parse(pdfUrl));
            request.setTitle(getString(R.string.download_report_title));
            request.setDescription(
                    getString(R.string.details_ref_number));
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "RapidGuard_Report_RG84721.pdf");

            DownloadManager dm = (DownloadManager)
                    getSystemService(Context.DOWNLOAD_SERVICE);
            if (dm != null) {
                dm.enqueue(request);
                Toast.makeText(this,
                        R.string.download_toast_started,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,
                    R.string.download_toast_error,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Android share sheet */
    private void shareReport() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.share_subject));
        share.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_body,
                        getString(R.string.details_ref_number)));
        startActivity(Intent.createChooser(share,
                getString(R.string.share_chooser_title)));
    }

    /** Open email client pre-filled with report details */
    private void sendToEmail() {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.share_subject));
        email.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_body,
                        getString(R.string.details_ref_number)));
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        } else {
            Toast.makeText(this,
                    R.string.download_no_email_app,
                    Toast.LENGTH_SHORT).show();
        }
    }
}