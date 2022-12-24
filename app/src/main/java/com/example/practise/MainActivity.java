package com.example.practise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseUser user;
    private DocumentReference fdbReference;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private String userID;


    private static final String KEY_NAME = "UserName";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";


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
        userID = user.getUid();
        fdbReference = fStore.collection("People").document(userID);

        TextView tName = (TextView) findViewById(R.id.mUsername);
        TextView tMail = (TextView) findViewById(R.id.mEmailAddress);
        TextView tPassword = (TextView) findViewById(R.id.mPassword);

        //firestore
        fdbReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tName.setText("Name : "+value.getString(KEY_NAME));
                tMail.setText("Email : "+value.getString(KEY_EMAIL));
                tPassword.setText("Password : "+value.getString(KEY_PASSWORD));
            }
        });

        //Realtime firebase
//        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                People udata = snapshot.getValue(People.class);
//
//                if(udata != null){
//                    String uName = udata.uname;
//                    String uPassword = udata.password;
//                    String uEmail = udata.email;
//
//                    tName.setText("Name : "+uName);
//                    tMail.setText("Email : "+uEmail);
//                    tPassword.setText("Password : "+uPassword);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this,"Invalid LogIn",Toast.LENGTH_LONG).show();
//            }
//        });
    }
}