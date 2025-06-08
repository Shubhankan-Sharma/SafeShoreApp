package com.shubhankan.safeshore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, UserID, editTextEmail, password, confirmPassword, mobileNumber;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference; // Reference to the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.password);
        UserID = findViewById(R.id.userid);
        confirmPassword = findViewById(R.id.confirm_password);
        mobileNumber = findViewById(R.id.mobile_number);
        registerButton = findViewById(R.id.register_button);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users"); // Initialize database reference

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String confirmUserPassword = confirmPassword.getText().toString().trim();
                String mobile = mobileNumber.getText().toString().trim();

                // Check if any of the fields are empty
                if (userName.isEmpty() || email.isEmpty() || userPassword.isEmpty() || confirmUserPassword.isEmpty() || mobile.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if passwords match
                if (!userPassword.equals(confirmUserPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check is password is less than 6 characters
                if (userPassword.length()<6) {
                    Toast.makeText(RegisterActivity.this,"Password should be of minimum 6 characters", Toast.LENGTH_SHORT).show();
                }

                // Show progress dialog
                ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Registering...");
                progressDialog.setCancelable(false); // Prevent dismissal on back press
                progressDialog.show();

                // Create user with Firebase Authentication
                Log.d("Register", "Attempting to register user...");
                mAuth.createUserWithEmailAndPassword(email, userPassword)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Dismiss the progress dialog
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // User registration successful
                            String userId = mAuth.getCurrentUser().getUid(); // Get the user ID
                            User user = new User(userName, email, userId, mobile); // Create a User object
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Close the RegisterActivity

                            // Save user data to Firebase Realtime Database
                            /*databaseReference.child(userId).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish(); // Close the RegisterActivity
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });*/
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        /*String userName = name.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String userID = UserID.getText().toString().trim();
        String mobile = mobileNumber.getText().toString().trim();

        User user = new User(userName,email,userID,mobile);
        Log.e("User details: ",user.toString());

        // Create an instance of UserManager
        UserManager userManager = new UserManager();

        // Upload user data
        userManager.uploadUserData(user);*/
    }

    // User class to hold user data
    public static class User {
        public String name;
        public String email;
        public String userId;
        public String mobile;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String email, String userId, String mobile) {
            this.name = name;
            this.email = email;
            this.userId = userId;
            this.mobile = mobile;
        }
    }
    public class UserManager {

        private DatabaseReference databaseReference;

        public UserManager() {
            // Get a reference to the Firebase Realtime Database
            databaseReference = FirebaseDatabase.getInstance().getReference("users"); // "users" is the node where user data will be stored
        }

        public void uploadUserData(User user) {
            // Generate a unique user ID (you can also use userId if you have it)
            String userId = databaseReference.push().getKey(); // Generate a unique key for the user

            // Set the user data in the database
            if (userId != null) {
                databaseReference.child(userId).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            // Data uploaded successfully
                            Log.d("User Manager", "User  data uploaded successfully.");
                        })
                        .addOnFailureListener(e -> {
                            // Failed to upload data
                            Log.e("User Manager", "Failed to upload user data: " + e.getMessage());
                        });
            }
        }
    }
}