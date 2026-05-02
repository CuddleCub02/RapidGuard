package com.example.rapidguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PostSosEmailCaptureActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sos_email_capture);

        bindViews();
        populateRef();
        setupButtons();
    }

    private void bindViews() {
        tilEmail = findViewById(R.id.tilEmail);
        etEmail  = findViewById(R.id.etEmail);
    }

    /** Pass incident ref from previous screen if available */
    private void populateRef() {
        String ref = getIntent().getStringExtra("incident_ref");
        if (ref != null && !ref.isEmpty()) {
            TextView tvRef = findViewById(R.id.tvContextRef);
            tvRef.setText(ref);
        }
    }

    private void setupButtons() {
        // Back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Send report to email
        MaterialButton btnSend = findViewById(R.id.btnSendReport);
        btnSend.setOnClickListener(v -> attemptSendReport());

        // Finish creating account — pre-fill email on register screen
        MaterialButton btnFinish = findViewById(R.id.btnFinishAccount);
        btnFinish.setOnClickListener(v -> {
            String email = etEmail.getText() != null
                    ? etEmail.getText().toString().trim() : "";
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("prefill_email", email);
            startActivity(intent);
        });

        // Skip for now → home
        TextView tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }

    private void attemptSendReport() {
        tilEmail.setError(null);

        String email = etEmail.getText() != null
                ? etEmail.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_email_required));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalid));
            return;
        }

        // TODO: call API to send incident report to email
        Toast.makeText(this,
                getString(R.string.email_capture_report_sent, email),
                Toast.LENGTH_LONG).show();

        // Navigate home after sending
        new android.os.Handler().postDelayed(() -> {
            startActivity(new Intent(this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }, 1200);
    }
}