package com.example.practise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseUser user;
    private DatabaseReference dbReference;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.logOutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,SignIn.class));
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("People");
        userID = user.getUid();

        final TextView tName = (TextView) findViewById(R.id.mUsername);
        final TextView tMail = (TextView) findViewById(R.id.mEmailAddress);
        final TextView tPassword = (TextView) findViewById(R.id.mPassword);

        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                People udata = snapshot.getValue(People.class);

                if(udata != null){
                    String uName = udata.uname;
                    String uPassword = udata.password;
                    String uEmail = udata.email;

                    tName.setText("Name : "+uName);
                    tMail.setText("Email : "+uEmail);
                    tPassword.setText("Password : "+uPassword);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Invalid LogIn",Toast.LENGTH_LONG).show();
            }
        });
    }
}