package com.example.beavervolunteerappjava;
/**
 *
 * @author  Nina Zhang
 * @version 1.0
 * @since   2022/07/
 */

import static android.content.ContentValues.TAG;
import static com.google.android.gms.common.util.ArrayUtils.newArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class InfoEditAfterRegistrationPage extends AppCompatActivity {

    //Declare variables of the various inputs
    EditText inputFirstName;
    EditText inputLastName;
    Spinner  inputSchoolStatus;
    EditText inputGrade;
    EditText inputSchoolAttended;
    EditText inputCountry;
    EditText inputProvince;
    EditText inputCity;
    EditText inputAddress;

    //Declare variables for the two buttons
    Button infoSubmit;
    Button infoCancel;

    //Declare error messages
    //TextView errorMessageForInput;

    //Actual Error Messages
    String illegalInputError = "Illegal characters detected: \"`~',/\\?$";
    String emptyInputError = "You have unfilled information";
    String otherError = "";


    //list of strings input
    int numberOfInputs = 11;
    String[] stringInputs = new String[numberOfInputs];


    //Declare database variable for Firebase Google
    private DatabaseReference mDatabase;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit_page_after_registration);

        //initialize variables of the various inputs
        inputFirstName = (EditText) findViewById(R.id.inputFirstName);
        inputLastName = (EditText) findViewById(R.id.inputLastName);
        inputSchoolStatus = (Spinner) findViewById(R.id.inputSchoolStatus);
        inputGrade = (EditText) findViewById(R.id.inputGrade);
        inputSchoolAttended = (EditText) findViewById(R.id.inputSchoolAttended);
        inputCountry = (EditText) findViewById(R.id.inputCountry);
        inputProvince = (EditText) findViewById(R.id.inputProvince);
        inputCity = (EditText) findViewById(R.id.inputCity);
        inputAddress = (EditText) findViewById(R.id.inputAddress);

        //Buttons
        infoSubmit = (Button) findViewById(R.id.infoSubmit);
        infoCancel = (Button) findViewById(R.id.infoCancel);

        //Error Messages
        //errorMessageForInput = (TextView) findViewById(R.id.errorMessageForInput);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*
        //Clear out whoever is logged in currently(this was copied from InfoEdit, not necesary for info edit after registration
        AuthUI.getInstance()
                .signOut(InfoEditAfterRegistrationPage.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if(currentUser != null){

                        }
                        else{
                            //reload(); reload the page since no one signed in
                        }
                    }
        });
        */

        infoSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // No need for getting email when editing information, but leave this line in in case in the future might need it
                String emailAddress = user.getEmail();


                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String schoolStatus = inputSchoolStatus.getItemAtPosition(inputSchoolStatus.getSelectedItemPosition()).toString();
                String grade = inputGrade.getText().toString();
                String schoolAttended = inputSchoolAttended.getText().toString();
                String country = inputCountry.getText().toString();
                String province = inputProvince.getText().toString();
                String city = inputCity.getText().toString();
                String address = inputAddress.getText().toString();

                stringInputs[0] = emailAddress;
                stringInputs[1] = "dummy password filler to prevent NullPointerException when reading the stringinputs";
                stringInputs[2] = firstName;
                stringInputs[3] = lastName;
                stringInputs[4] = schoolStatus;
                stringInputs[5] = grade;
                stringInputs[6] = schoolAttended;
                stringInputs[7] = country;
                stringInputs[8] = province;
                stringInputs[9] = city;
                stringInputs[10] = address;


                boolean accountInfoOk = true;

                /**
                 * check if the input is from the file
                 * then accountInfoOk = false
                 */

                for(int i = 0; i < stringInputs.length; i++) {
                    if(checkTextInput(stringInputs[i]) == false) {
                        accountInfoOk = false;
                        break;
                    }
                };

                if(accountInfoOk) {
                    updateDataBase(user);
                    //createUserAccountWithEmail(stringInputs[0], stringInputs[1]);
                }
                else{

                }
                Log.d(TAG,"===========" + accountInfoOk + "===========");

            }
        });



        //When cancel is clicked
        infoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoEditAfterRegistrationPage.this, userLandingPage.class));
            }
        });
    }
/*
    public userAccount infoEdit(String pageComingFrom){
        return new userAccount();
    }
*/


    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    //onSignInResult(result);
                }
            }
    );





    /**
     * Determine if the string input contains special value that is not allowed,
     * including "`~',/\\?$
     * @param input
     * @return boolean that shows if the string input contains special value that is not allowed
     */
    private boolean checkTextInput(String input){
        String invalidCharacters = "\"`~',/\\?$";

        //if any input is empty
        if(input.isEmpty()){
            showToastInfoEdit(emptyInputError);
            return false;
        }

        //illegal characters in input
        for(int i = 0; i < input.length(); i++){
            for (int j = 0; j < invalidCharacters.length(); j++){
                if(invalidCharacters.charAt(j) == input.charAt(i)){
                    showToastInfoEdit(illegalInputError);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * This method shows a floating message
     * @param text
     * used to show an error mostly
     */
    private void showToastInfoEdit(String text){
        Toast.makeText(InfoEditAfterRegistrationPage.this, text, Toast.LENGTH_LONG).show();
    }


    private void updateDataBase(FirebaseUser user){
        if(user != null){
            Log.d(TAG,"UPDATING RIGHT NOW!!!!!!!!!!!!!!!!!!!!!!");
            //get the Uid of current user
            String uid = user.getUid();
            //get reference from database
            mDatabase = FirebaseDatabase.getInstance().getReference();

            //update each item
            mDatabase.child("users").child(uid).child("userFirstName").setValue(stringInputs[2]);
            mDatabase.child("users").child(uid).child("userLastName").setValue(stringInputs[3]);
            mDatabase.child("users").child(uid).child("userSchoolStatus").setValue(stringInputs[4]);
            mDatabase.child("users").child(uid).child("userGrade").setValue(stringInputs[5]);
            mDatabase.child("users").child(uid).child("schoolAttended").setValue(stringInputs[6]);
            mDatabase.child("users").child(uid).child("countryOfLiving").setValue(stringInputs[7]);
            mDatabase.child("users").child(uid).child("provinceOrStateOfLiving").setValue(stringInputs[8]);
            mDatabase.child("users").child(uid).child("cityOfLiving").setValue(stringInputs[9]);
            mDatabase.child("users").child(uid).child("addressLine").setValue(stringInputs[10]);

            showToastInfoEdit("Information saved successfully.");
            startActivity(new Intent(InfoEditAfterRegistrationPage.this, userLandingPage.class));
        }
        else{
            Log.d(TAG, "FAILUARE NOOOOO. >:( >:( >:( >:( >:( >:O >:((((((((((");
        }
    }

}