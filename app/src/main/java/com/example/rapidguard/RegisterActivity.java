package com.example.rapidguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilPhone, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etPhone, etEmail, etPassword, etConfirmPassword;
    private CheckBox cbTerms;

    // Strength bar views
    private View strengthBar1, strengthBar2, strengthBar3, strengthBar4;
    private TextView tvStrengthLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();
        setupButtons();
        setupPasswordStrength();
    }

    private void bindViews() {
        tilFullName        = findViewById(R.id.tilFullName);
        tilPhone           = findViewById(R.id.tilPhone);
        tilEmail           = findViewById(R.id.tilEmail);
        tilPassword        = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etFullName        = findViewById(R.id.etFullName);
        etPhone           = findViewById(R.id.etPhone);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        cbTerms          = findViewById(R.id.cbTerms);
        strengthBar1     = findViewById(R.id.strengthBar1);
        strengthBar2     = findViewById(R.id.strengthBar2);
        strengthBar3     = findViewById(R.id.strengthBar3);
        strengthBar4     = findViewById(R.id.strengthBar4);
        tvStrengthLabel  = findViewById(R.id.tvStrengthLabel);
    }

    private void setupButtons() {
        // Back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Register
        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> attemptRegister());

        // Login link
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.rapidguard.LoginActivity.class));
            finish();
        });

        // SOS shortcut — no login required
        MaterialButton btnSOS = findViewById(R.id.btnSOSShortcut);
        btnSOS.setOnClickListener(v ->
                startActivity(new Intent(this, SosEmergencyActivity.class)));
    }

    /** Live password strength meter */
    private void setupPasswordStrength() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}

            @Override
            public void afterTextChanged(Editable s) {
                int score = calculateStrength(s.toString());
                updateStrengthUI(score);
            }
        });
    }

    /** Returns 0–4 based on password complexity */
    private int calculateStrength(String password) {
        int score = 0;
        if (password.length() >= 8)                          score++;
        if (password.matches(".*[A-Z].*"))                   score++;
        if (password.matches(".*[0-9].*"))                   score++;
        if (password.matches(".*[^a-zA-Z0-9].*"))            score++;
        return score;
    }

    private void updateStrengthUI(int score) {
        tvStrengthLabel.setVisibility(score > 0 ? View.VISIBLE : View.GONE);

        int activeRes;
        String label;

        switch (score) {
            case 1:  activeRes = R.drawable.bg_strength_weak;      label = getString(R.string.strength_weak);       break;
            case 2:  activeRes = R.drawable.bg_strength_medium;    label = getString(R.string.strength_medium);     break;
            case 3:  activeRes = R.drawable.bg_strength_strong;    label = getString(R.string.strength_strong);     break;
            case 4:  activeRes = R.drawable.bg_strength_strong;    label = getString(R.string.strength_very_strong);break;
            default: activeRes = R.drawable.bg_strength_inactive;  label = "";                                      break;
        }

        int inactive = R.drawable.bg_strength_inactive;
        strengthBar1.setBackgroundResource(score >= 1 ? activeRes : inactive);
        strengthBar2.setBackgroundResource(score >= 2 ? activeRes : inactive);
        strengthBar3.setBackgroundResource(score >= 3 ? activeRes : inactive);
        strengthBar4.setBackgroundResource(score >= 4 ? activeRes : inactive);
        tvStrengthLabel.setText(label);
    }

    private void attemptRegister() {
        // Clear previous errors
        tilFullName.setError(null);
        tilPhone.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        String name     = etFullName.getText()        != null ? etFullName.getText().toString().trim()        : "";
        String phone    = etPhone.getText()           != null ? etPhone.getText().toString().trim()           : "";
        String email    = etEmail.getText()           != null ? etEmail.getText().toString().trim()           : "";
        String password = etPassword.getText()        != null ? etPassword.getText().toString()               : "";
        String confirm  = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString()        : "";

        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            tilFullName.setError(getString(R.string.error_name_required));
            valid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError(getString(R.string.error_phone_required));
            valid = false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            tilPhone.setError(getString(R.string.error_phone_invalid));
            valid = false;
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_email_required));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email_invalid));
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_password_required));
            valid = false;
        } else if (password.length() < 8) {
            tilPassword.setError(getString(R.string.error_password_too_short));
            valid = false;
        }

        if (TextUtils.isEmpty(confirm)) {
            tilConfirmPassword.setError(getString(R.string.error_confirm_password_required));
            valid = false;
        } else if (!password.equals(confirm)) {
            tilConfirmPassword.setError(getString(R.string.error_passwords_no_match));
            valid = false;
        }

        if (!cbTerms.isChecked()) {
            cbTerms.setError(getString(R.string.error_terms_required));
            valid = false;
        }

        if (valid) {
            // TODO: hook up registration (Firebase / REST)
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}