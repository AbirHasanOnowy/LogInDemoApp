package com.example.practise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    EditText eEmail,ePassword;
    Button signInButton,signUpButton;
    String email,password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        eEmail = findViewById(R.id.emailAddress);
        ePassword = findViewById(R.id.password);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = eEmail.getText().toString().trim();
                password = ePassword.getText().toString().trim();


                if(email.isEmpty())
                {
                    eEmail.setError("Email is required");
                    eEmail.requestFocus();
                    return;
                }

                if(password.isEmpty())
                {
                    ePassword.setError("Password is required");
                    ePassword.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    eEmail.setError("Please provide valid email");
                    eEmail.requestFocus();
                    return;
                }

                if(password.length() < 6)
                {
                    ePassword.setError("Password length min 6");
                    ePassword.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()) {
                                Toast.makeText(SignIn.this, "Login Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignIn.this, MainActivity.class));
                            } else {
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(SignIn.this,"Check Email of verify account", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(SignIn.this,"Failed to send email", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        } else {
                            Toast.makeText(SignIn.this,"Invalid Email or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });



            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }

}