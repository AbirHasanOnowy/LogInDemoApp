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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStoreDb;
    private DocumentReference dDbReference;

    EditText eName,eEmail,ePassword;
    Button confirmButton;
    String name,email,password;
    private static final String KEY_NAME = "UserName";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        mAuth = FirebaseAuth.getInstance();
        fireStoreDb = FirebaseFirestore.getInstance();


        eName = findViewById(R.id.sUsername);
        eEmail = findViewById(R.id.semailAddress);
        ePassword = findViewById(R.id.spassword);
        confirmButton = findViewById(R.id.sconfirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = eName.getText().toString().trim();
                email = eEmail.getText().toString().trim();
                password = ePassword.getText().toString().trim();

                if(name.isEmpty())
                {
                    eName.setError("Name is required");
                    eName.requestFocus();
                    return;
                }

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


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            People newUser = new People(name,email,password);
                            //firestore database
                            dDbReference = fireStoreDb.collection("People")
                                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                           Map<String,Object> val = new HashMap<>();
                           val.put(KEY_NAME,name);
                           val.put(KEY_EMAIL,email);
                           val.put(KEY_PASSWORD,password);
                           dDbReference.set(val).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful())
                                            {
                                                Toast.makeText(SignUp.this,"Registration complete", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(SignUp.this, SignIn.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this,"Failed to Register", Toast.LENGTH_LONG).show();
                                            }
                               }
                           });





//                            //Realtime database
//                            FirebaseDatabase.getInstance().getReference("People")
//                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(newUser)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful())
//                                            {
//                                                Toast.makeText(SignUp.this,"Registration complete", Toast.LENGTH_LONG).show();
//                                                startActivity(new Intent(SignUp.this, SignIn.class));
//                                                finish();
//                                            } else {
//                                                Toast.makeText(SignUp.this,"Failed to Register", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(SignUp.this,"Failed to Register", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUp.this,"Failed to Register", Toast.LENGTH_LONG).show();
                        }
                   }
                });
            }
        });
    }

}