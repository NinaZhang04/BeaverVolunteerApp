package com.example.beavervolunteerappjava;

import static com.example.beavervolunteerappjava.VolunteerOpportunityPage.currentlyViewingOpportunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailedOpportunityPage extends AppCompatActivity {
    TextView opportunityNameDetailPage;
    TextView longDescription;
    TextView responsibilities;
    TextView requirements;
    TextView locationAndHours;
    Button goBackButtonDetailedPage;

    //did
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

        opportunityNameDetailPage = (TextView) findViewById(R.id.opportunityNameDetailPage);
        longDescription = (TextView) findViewById(R.id.longDescription);
        responsibilities = (TextView) findViewById(R.id.responsibilities);
        requirements = (TextView) findViewById(R.id.requirements);
        locationAndHours = (TextView) findViewById(R.id.locationAndHours);
        goBackButtonDetailedPage = (Button) findViewById(R.id.goBackbuttonDetailedPage);

        opportunityNameDetailPage.setText(volunteerOpportunityName);
        longDescription.setText(volunteerLongDescription);
        responsibilities.setText(volunteerResponsibilities);
        requirements.setText(volunteerRequirements);
        locationAndHours.setText(volunteerLocationAndHours);

        goBackButtonDetailedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailedOpportunityPage.this, VolunteerOpportunityPage.class));
            }
        });
    }
    // Remember to create

}