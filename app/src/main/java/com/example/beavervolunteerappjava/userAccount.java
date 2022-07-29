package com.example.beavervolunteerappjava;

import java.util.List;

public class userAccount {
    //declare all instance variables belonging to the user's acocunt

    //Use user's email as a unique way of identification, this cannot be changed
    private String userEmail;

    //Password. Can only be changed using email
    private String password;

    //Name
    private String userFirstName;
    private String userLastName;

    //There are several possible userSchoolStatus:
    //Elementary/High School/CEGEP
    //University/Graduate School
    //Not In School
    private String userSchoolStatus;

    //EHC: Grade 1-12, CEGEP year 1, CEGEP year 2
    //UG: Undergrad year 1-5 + beyond year 5, Masters, PhD, other
    //NIS: Parent, other; please specify
    private String userGrade;

    //School attended
    private String schoolAttended;

    //Address of the user
    private String countryOfLiving;
    private String provinceOrStateOfLiving;
    private String cityOfLiving;
    private String addressLine;

    private List<String> registeredList;

    //Unique userID, cannot be changed afterwards, will be randomly generated and assigned to user
    private String userID;

    public userAccount(){

    }

    public userAccount(
            String userEmail,
            String password,
            String userFirstName,
            String userLastName,
            String userSchoolStatus,
            String userGrade,
            String schoolAttended,
            String countryOfLiving,
            String provinceOrStateOfLiving,
            String cityOfLiving,
            String addressLine,
            String userID,
            List<String> registeredList){
            this.userEmail = userEmail;
            this.password = password;
            this.userFirstName = userFirstName;
            this.userLastName = userLastName;
            this.userSchoolStatus = userSchoolStatus;
            this.userGrade = userGrade;
            this.schoolAttended = schoolAttended;
            this.countryOfLiving = countryOfLiving;
            this.provinceOrStateOfLiving = provinceOrStateOfLiving;
            this.cityOfLiving = cityOfLiving;
            this.addressLine = addressLine;
            this.userID = userID;
            this.registeredList = registeredList;
    }


    //Getters
    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword(){
        return password;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserSchoolStatus(){
        return userSchoolStatus;
    }

    public String getUserGrade() {
        return userGrade;
    }

    public String getSchoolAttended() {
        return schoolAttended;
    }

    public String getCountryOfLiving() {
        return countryOfLiving;
    }

    public String getProvinceOrStateOfLiving() {
        return provinceOrStateOfLiving;
    }

    public String getCityOfLiving() {
        return cityOfLiving;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getUserID(){ return userID; }

    public List<String> getRegisteredList() {return registeredList;}
}
