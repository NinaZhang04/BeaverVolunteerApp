package com.example.beavervolunteerappjava;

public class VolunteerOpportunity {


    String opportunityName;
    String shortDescription;
    String longDescription;
    String volunteerResponsibilities;
    String volunteerRequirement;
    String volunteerLocationAndHours;
    String resgitrationDetails;
    String volunteerExpireTime;


    public VolunteerOpportunity(){

    }

    /**
     * Constructor of VolunteerOpportunity with variables
     * @param opportunityName
     * @param shortDescription
     * @param longDescription
     * @param volunteerResponsibilities
     * @param volunteerRequirement
     * @param volunteerLocationAndHours
     * @param resgitrationDetails
     * @param volunteerExpireTime
     */
    public VolunteerOpportunity(String opportunityName,
                                String shortDescription,
                                String longDescription,
                                String volunteerResponsibilities,
                                String volunteerRequirement,
                                String volunteerLocationAndHours,
                                String resgitrationDetails,
                                String volunteerExpireTime) {
        this.opportunityName = opportunityName;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.volunteerResponsibilities = volunteerResponsibilities;
        this.volunteerRequirement = volunteerRequirement;
        this.volunteerLocationAndHours = volunteerLocationAndHours;
        this.resgitrationDetails = resgitrationDetails;
        this.volunteerExpireTime = volunteerExpireTime;
    }



}
