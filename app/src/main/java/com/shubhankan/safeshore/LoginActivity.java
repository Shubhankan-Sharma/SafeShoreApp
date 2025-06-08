package com.shubhankan.safeshore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private EditText userid, password;
    private Button loginButton;
    private TextView registerPrompt;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "SafeShorePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // If already logged in, skip login screen
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            navigateToMain();
            finish();
            return;
        }

        userid = findViewById(R.id.login_id);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerPrompt = findViewById(R.id.register_prompt);
        mAuth = FirebaseAuth.getInstance();

        // Set up the register prompt click listener
        registerPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userid.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                Log.d("Login","Loging in...");

                // Authenticate user with Firebase
                mAuth.signInWithEmailAndPassword(userId, userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply();
                                    Log.e("Logged in??:", String.valueOf(sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)));

                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    // Navigate to the main activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    private void navigateToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}