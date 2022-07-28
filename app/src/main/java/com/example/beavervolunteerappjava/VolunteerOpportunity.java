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

    @Override
    public String toString() {
        return "VolunteerOpportunity{" +
                "opportunityName='" + opportunityName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", volunteerResponsibilities='" + volunteerResponsibilities + '\'' +
                ", volunteerRequirement='" + volunteerRequirement + '\'' +
                ", volunteerLocationAndHours='" + volunteerLocationAndHours + '\'' +
                ", resgitrationDetails='" + resgitrationDetails + '\'' +
                ", volunteerExpireTime='" + volunteerExpireTime + '\'' +
                '}';
    }


    public String getOpportunityName(){
        return opportunityName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getVolunteerResponsibilities() {
        return volunteerResponsibilities;
    }

    public String getVolunteerRequirement() {
        return volunteerRequirement;
    }

    public String getVolunteerLocationAndHours() {
        return volunteerLocationAndHours;
    }

    public String getResgitrationDetails() {
        return resgitrationDetails;
    }

    public String getVolunteerExpireTime() {
        return volunteerExpireTime;
    }
}
