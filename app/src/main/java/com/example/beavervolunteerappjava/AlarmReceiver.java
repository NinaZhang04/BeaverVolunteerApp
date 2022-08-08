package com.example.beavervolunteerappjava;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AlarmReceiver extends BroadcastReceiver {
    private DatabaseReference mDatabase;
    @Override
    public void onReceive(Context context, Intent intent) {
        // When the timer is triggered
        // Send an reminder email to the user



/**
        // this will send a reminder notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "BeaverWithU")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Volunteer opportunity feedback")
                .setContentText("A week ago you signed up for an opportunity. Want to give us some feedback on it?")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
**/


        String mailBodyText = "A week ago you signed up for an volunteer opportunity using the BeaverWithU app.\n" +
                "Today we kindly ask you to fill out a feed-back form on how was the opportunity if you have participated, and how was your experience with our app.\n" +
                "Click on the google form link below to give us some feedback and help us to improve!" +
                "\n\n" +
                "https://forms.gle/RUm3tsQY5UGsbK9s5";

        //get database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // If the user is logged in
        if (user != null) {
            Log.d(TAG, "++++++++++++++++++++REMINDER SENT++++++++++++++++++++++++");
            // Get the current user's ID
            String userId = user.getUid();

            // Use the above ID to check in the realtime database about their registered list
            String receiverEmail = user.getEmail();

            //send the email
            sendEmail(ImportantValues.senderEmail, ImportantValues.senderPassword, mailBodyText, receiverEmail);
        }

        else{
            Log.d(TAG, "USER IS NOT LOGGED IN AHHHHHHHHHHH");
            // The user is not logged in, change the date to one day later (24 hours later) to see if the user logs in again
        }
    }



    private void sendEmail(String senderEmail, String password, String mailBodyText, String receiverEmail){
        /**
         Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto: " + receiverEmail));
         intent.putExtra(Intent.EXTRA_SUBJECT, ImportantValues.registrationFirstTimeSubject);
         intent.putExtra(Intent.EXTRA_TEXT, mailBodyText);
         startActivity(intent);
         **/
        Log.d(TAG, "Email in mainthread ");
        new Thread(()-> {
            // https://www.youtube.com/watch?v=roruU4hVwXA
            Log.d(TAG, "Email in this thread ");
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
        // (if want to remove the new thread argument, use the following line below
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
    }

}
