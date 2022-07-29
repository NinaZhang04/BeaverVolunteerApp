package com.example.beavervolunteerappjava;

import static android.content.ContentValues.TAG;

import static com.google.android.gms.common.util.ArrayUtils.newArrayList;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Random;


public class InfoEditPage extends AppCompatActivity {

    //Declare variables of the various inputs
    EditText inputEmailAddress;
    EditText inputPassword;
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

    //Declare an instance of FrebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit_page);

        //initialize variables of the various inputs
        inputEmailAddress = (EditText) findViewById(R.id.inputEmailAddress);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
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

        //Clear out whoever is logged in currently
        AuthUI.getInstance()
                .signOut(InfoEditPage.this)
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


        infoSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String emailAddress = inputEmailAddress.getText().toString();
                String password = inputPassword.getText().toString();
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
                stringInputs[1] = password;
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
                    createUserAccountWithEmail(stringInputs[0], stringInputs[1]);
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
                startActivity(new Intent(InfoEditPage.this, LoginPage.class));
            }
        });
    }

    public userAccount infoEdit(String pageComingFrom){
        return new userAccount();
    }


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
        Toast.makeText(InfoEditPage.this, text, Toast.LENGTH_LONG).show();
    }


    public String randomIdGenerator(){
        String randomID = "";
        String randomLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for(int i = 0; i< 8; i++){
            Random rand = new Random();
            int randomLetterNum = rand.nextInt(36);
            randomID += randomLetters.charAt(randomLetterNum);
        }
        return randomID;
    }


    public void createUserAccountWithEmail(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "==================================createUserWithEmail:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            updateDataBase(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "==================================createUserWithEmail:failure", task.getException());
                            showToastInfoEdit("Account has already signed up or user cannot be created");
                            updateDataBase(null);
                        }
                    }
                });
    }

    private void updateDataBase(FirebaseUser user){
        if(user != null){
            Log.d(TAG,"LOGGGGIGNNNNNNNN :)))))))))))))))))))))))))))))))))))))))");
            //get the Uid of newly created user
            String uid = user.getUid();
            //get reference from database
            mDatabase = FirebaseDatabase.getInstance().getReference();
            //create newUser object for upload
            List<String> registeredListEmpty = newArrayList();
            registeredListEmpty.add("A000000");
            userAccount newUser = new userAccount(
                    stringInputs[0], stringInputs[1],
                    stringInputs[2], stringInputs[3],
                    stringInputs[4], stringInputs[5],
                    stringInputs[6], stringInputs[7],
                    stringInputs[8], stringInputs[9],
                    stringInputs[10],uid, registeredListEmpty
                    );
            //Load the information of the new user to the database
            mDatabase.child("users").child(uid).setValue(newUser);

            showToastInfoEdit("Account created successfully. Returning to sign in page");
            startActivity(new Intent(InfoEditPage.this, LoginPage.class));
        }
        else{
            Log.d(TAG, "login failed. >:( >:( >:( >:( >:( >:O >:((((((((((");
        }
    }

}