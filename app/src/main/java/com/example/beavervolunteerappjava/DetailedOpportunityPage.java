package com.example.beavervolunteerappjava;

import static android.content.ContentValues.TAG;
import static com.example.beavervolunteerappjava.VolunteerOpportunityPage.currentlyViewingOpportunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import javax.mail.Message;
import javax.mail.MessagingException;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

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



    String volunteerOpportunityName;
    String volunteerRegistrationDetails;



    String opportunityId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_opportunity_page);
        volunteerOpportunityName = currentlyViewingOpportunity.getOpportunityName();
        String volunteerShortDescription = currentlyViewingOpportunity.getShortDescription();
        String volunteerLongDescription = currentlyViewingOpportunity.getLongDescription();
        String volunteerResponsibilities = currentlyViewingOpportunity.getVolunteerResponsibilities();
        String volunteerRequirements = currentlyViewingOpportunity.getVolunteerRequirement();
        String volunteerLocationAndHours = currentlyViewingOpportunity.getVolunteerLocationAndHours();
        volunteerRegistrationDetails = currentlyViewingOpportunity.getResgitrationDetails();
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
            String receiverEmail = user.getEmail();
            mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {

                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", "What is this: " + String.valueOf(task.getResult().getValue()));
                        userAccount firebaseAccountInfo = (userAccount)(task.getResult().getValue(userAccount.class));
                        List<String> firebaseAccountRegisteredList = firebaseAccountInfo.getRegisteredList();
                        Boolean stopRepeatOpportunityFound = false;
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
                        if(stopRepeatOpportunityFound == false) {
                            firebaseAccountRegisteredList.add(currentlyViewingOpportunity.getOpportunityId());
                            // update the list to the firebase database
                            // Add the opportunity id to the user's already registered list
                            mDatabase.child("users").child(userId).child("registeredList").setValue(firebaseAccountRegisteredList);
                            Log.d(TAG, "Registration Successful");
                            showToast("Registration Successful. Check your inbox for information");
                            // Send email
                            // will be sent
                            String senderEmail = ImportantValues.senderEmail;
                            String password = ImportantValues.senderPassword;
                            String messageToSend =
                                    "Hello! Thank you for registering for" + volunteerOpportunityName +
                                    ".\n\n" +
                                    "The registration detail is as follows: \n" +
                                    volunteerRegistrationDetails + ".\n" +
                                    "Good luck applying!";
                            Log.d(TAG, messageToSend);

                            sendEmail(senderEmail, password, messageToSend, receiverEmail);


                        }
                        if(stopRepeatOpportunityFound == true){
                            showToast("Already registered.");
                        }
                    }
                }
            });


            /**
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

            }

            @Override
                public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };


             mDatabase.addValueEventListener(registeredListListener);
             **/


        } else {
            showToast("Developer is amazed about how you even got here without logging in.");
        }

    }

    private void showToast(String text){
        Toast.makeText(DetailedOpportunityPage.this, text, Toast.LENGTH_LONG).show();
    }

    public void sendEmail(String senderEmail, String password, String mailBodyText, String receiverEmail){
        /**
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto: " + receiverEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, ImportantValues.registrationFirstTimeSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mailBodyText);
        startActivity(intent);
         **/
        Log.d(TAG, "Email in mainthread ");
       new Thread(()-> {
           // https://www.youtube.com/watch?v=roruU4hVwXA
           Log.d(TAG, "Email in this trhead ");
           Properties props = new Properties();
           props.put("mail.smtp.auth", "true");
           props.put("mail.smtp.starttls.enable", "true");
           props.put("mail.smtp.host", "smtp.gmail.com");
           props.put("mail.smtp.port", "587");
           Session session = Session.getInstance(props, new javax.mail.Authenticator() {
               @Override
               protected PasswordAuthentication getPasswordAuthentication() {
                   return new PasswordAuthentication(senderEmail, password);
               }
           });
           try {

               Message message = new MimeMessage(session);
               message.setFrom(new InternetAddress(senderEmail));
               message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
               message.setSubject(ImportantValues.registrationFirstTimeSubject);
               message.setText(mailBodyText);
               Transport.send(message);
               Log.d(TAG, "Email should be sent! :::::::::::::::::::::::::::");
           } catch (MessagingException e) {
               Log.d(TAG, ":(((((((((((((((((( email was not sent");
               throw new RuntimeException(e);

           }
       }).start();

    }
}