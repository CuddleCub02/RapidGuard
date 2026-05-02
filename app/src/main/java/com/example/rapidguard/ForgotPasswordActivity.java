package com.example.rapidguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private MaterialButton btnSendReset;
    private CardView cardSuccess;
    private TextView tvResendTimer, tvResendLink;

    private static final long RESEND_DELAY_MS = 60_000L;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        bindViews();
        setupButtons();
    }

    private void bindViews() {
        tilEmail      = findViewById(R.id.tilEmail);
        etEmail       = findViewById(R.id.etEmail);
        btnSendReset  = findViewById(R.id.btnSendReset);
        cardSuccess   = findViewById(R.id.cardSuccess);
        tvResendTimer = findViewById(R.id.tvResendTimer);
        tvResendLink  = findViewById(R.id.tvResendLink);
    }

    private void setupButtons() {
        // Back arrow
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Send reset link
        btnSendReset.setOnClickListener(v -> attemptSendReset());

        // Back to login text link
        TextView tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Resend link
        tvResendLink.setOnClickListener(v -> {
            tvResendLink.setVisibility(View.GONE);
            startResendCountdown();
            // TODO: re-trigger password reset API call
        });
    }

    private void attemptSendReset() {
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

        // TODO: call password reset API (Firebase / REST)
        showSuccessState();
    }

    /** Reveal success card and start 60-second resend countdown */
    private void showSuccessState() {
        btnSendReset.setEnabled(false);
        cardSuccess.setVisibility(View.VISIBLE);
        startResendCountdown();
    }

    private void startResendCountdown() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(RESEND_DELAY_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvResendTimer.setVisibility(View.VISIBLE);
                tvResendTimer.setText(
                        getString(R.string.forgot_resend_timer)
                                .replace("60", String.valueOf(seconds)));
            }

            @Override
            public void onFinish() {
                tvResendTimer.setVisibility(View.GONE);
                tvResendLink.setVisibility(View.VISIBLE);
                btnSendReset.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}