package com.example.beavervolunteerappjava;

import static android.content.ContentValues.TAG;
import static com.example.beavervolunteerappjava.VolunteerOpportunityPage.currentlyViewingOpportunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

public class DetailedOpportunityPage extends AppCompatActivity {
    TextView opportunityNameDetailPage;
    TextView longDescription;
    TextView responsibilities;
    TextView requirements;
    TextView locationAndHours;
    Button goBackButtonDetailedPage;
    Button detailedResgistrationButton;
    private DatabaseReference mDatabase;
    //did




    String opportunityId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_opportunity_page);
        String volunteerOpportunityName = currentlyViewingOpportunity.getOpportunityName();
        String volunteerShortDescription = currentlyViewingOpportunity.getShortDescription();
        String volunteerLongDescription = currentlyViewingOpportunity.getLongDescription();
        String volunteerResponsibilities = currentlyViewingOpportunity.getVolunteerResponsibilities();
        String volunteerRequirements = currentlyViewingOpportunity.getVolunteerRequirement();
        String volunteerLocationAndHours = currentlyViewingOpportunity.getVolunteerLocationAndHours();
        String volunteerRegistrationDetails = currentlyViewingOpportunity.getResgitrationDetails();
        String volunteerExpireDate = currentlyViewingOpportunity.getVolunteerExpireTime();
        opportunityId = currentlyViewingOpportunity.getOpportunityId();

        opportunityNameDetailPage = (TextView) findViewById(R.id.opportunityNameDetailPage);
        longDescription = (TextView) findViewById(R.id.longDescription);
        responsibilities = (TextView) findViewById(R.id.responsibilities);
        requirements = (TextView) findViewById(R.id.requirements);
        locationAndHours = (TextView) findViewById(R.id.locationAndHours);
        goBackButtonDetailedPage = (Button) findViewById(R.id.goBackbuttonDetailedPage);
        detailedResgistrationButton = (Button) findViewById(R.id.detailedResgistrationButton);

        opportunityNameDetailPage.setText(volunteerOpportunityName);
        longDescription.setText(volunteerLongDescription);
        responsibilities.setText(volunteerResponsibilities);
        requirements.setText(volunteerRequirements);
        locationAndHours.setText(volunteerLocationAndHours);

        goBackButtonDetailedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "this button worked");
                startActivity(new Intent(DetailedOpportunityPage.this, VolunteerOpportunityPage.class));
            }
        });


        detailedResgistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if you have already registered.
                // record the time that the opportunity was registered and send reminder email 7 days later
                // save this information on
                // Title: Registration details of: + volunteerOpportunityName
                // Body: volunteerRegistrationDetails + two new lines + thank you for registering!
                //

                checkIfRegistered();
            }
        });

    }

    // Remember to create emailing
    public void checkIfRegistered(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // Get the current user's ID
            String userId = user.getUid();
            // Use the above ID to check in the realtime database about their registered list
            ValueEventListener registeredListListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get userAccount object and use the values to update the UI
                    userAccount firebaseAccountInfo = dataSnapshot.child("users").child(userId).getValue(userAccount.class);

                    // get the account's registered opportunity list
                    List<String> firebaseAccountRegisteredList = firebaseAccountInfo.getRegisteredList();
                    Boolean stopRepeatOpportunityFound = false;
                    //iterate through what the user has registered

                    for(int i = 0; i< firebaseAccountRegisteredList.size(); i++){
                        if(stopRepeatOpportunityFound == false) {
                            //if there is an opportunity that has the same Id as the one the user is trying to register
                            if (firebaseAccountRegisteredList.get(i).equals(opportunityId)) {
                                stopRepeatOpportunityFound = true;
                                break;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    // the user has not yet registered the opportunity.
                    // Send email
                    // Add the opportunity id to the user's already registered list
                    if(stopRepeatOpportunityFound == false) {
                        firebaseAccountRegisteredList.add(currentlyViewingOpportunity.getOpportunityId());
                        // update the list to the firebase database
                        mDatabase.child("users").child(userId).child("registeredList").setValue(firebaseAccountRegisteredList);
                        Log.d(TAG, "Registration Successful");
                        showToast("Registration Successful. Check your inbox for information");
                    }
                    if(stopRepeatOpportunityFound == true){
                        showToast("Already registered.");
                    }
                    mDatabase.removeEventListener(registeredListListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };


            mDatabase.addValueEventListener(registeredListListener);




        } else {
            showToast("Developer is amazed about how you even got here without logging in.");
        }

    }

    private void showToast(String text){
        Toast.makeText(DetailedOpportunityPage.this, text, Toast.LENGTH_LONG).show();
    }
}