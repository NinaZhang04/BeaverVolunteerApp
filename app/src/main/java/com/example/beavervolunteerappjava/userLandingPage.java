package com.example.beavervolunteerappjava;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userLandingPage extends AppCompatActivity {
    // Declare variables for display username and display useremail
    TextView userName;
    TextView userEmail;
    Button userLandingSignOut;
    Button volunteerPageButton;
    Button signedUpPageButton;
    Button reeditInfoPageButton;
    Button aboutTheAppButton;



    //set up firebase reference
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landingpage);

        volunteerPageButton = (Button) findViewById(R.id.volunteerPageButton);
        signedUpPageButton = (Button) findViewById(R.id.reeditInfoPageButton);
        reeditInfoPageButton = (Button) findViewById(R.id.reeditInfoPageButton);
        aboutTheAppButton = (Button) findViewById(R.id.aboutTheAppButton);
        userLandingSignOut = (Button) findViewById(R.id.userLandingSignOut);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            userName = (TextView) findViewById(R.id.userName);
            userEmail = (TextView) findViewById(R.id.userEmail);
            mDatabase = FirebaseDatabase.getInstance().getReference();

            //.child(user.getUid())

            ValueEventListener userListener = new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get the name of the user and update it
                    String tempFirstName = dataSnapshot.child("users").child(user.getUid()).getValue(userAccount.class).getUserFirstName();
                    String tempLastName = dataSnapshot.child("users").child(user.getUid()).getValue(userAccount.class).getUserLastName();
                    String userFullName = tempFirstName + " " + tempLastName;
                    Log.d(TAG, userFullName + "***********************************");
                    userName.setText(userFullName);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            mDatabase.addValueEventListener(userListener);
            //String tempFirstName = mDatabase.child("users").child(user.getUid()).child("userFirstName").toString();
            //String tempLastName = mDatabase.child("users").child(user.getUid()).child("userLastName").toString();
            //userFullName = tempFirstName + " " + tempLastName;

            userEmail.setText(user.getEmail());

        }
        else{
            Toast.makeText(userLandingPage.this, "Login failed. Please try again",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(userLandingPage.this, LoginPage.class));
        }

        volunteerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signedUpPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        reeditInfoPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        aboutTheAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        userLandingSignOut.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(userLandingPage.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                showToast("Signed out");
                            }
                        });
                startActivity(new Intent(userLandingPage.this, LoginPage.class));
            }

        }));

    }

    private void showToast(String text){
        Toast.makeText(userLandingPage.this, text, Toast.LENGTH_LONG).show();
    }


}