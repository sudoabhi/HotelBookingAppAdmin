package com.example.hotelloadmin.ScannerPackage;

import android.provider.ContactsContract;

/**
 * Created by Asus on 1/10/2020.
 */

public class BookedTicketsObject {

    String UserName;
    String ClubLocation;
    String RollNum;
    String Verified;
    String ProfileImage;
    String UserUID;
    String AllotedKey;

    public BookedTicketsObject(String UserName,String ClubLocation,String RollNum, String Verified,String ProfileImage,String UserUID,String AllotedKey){

       this.UserName=UserName;
       this.UserUID=UserUID;
       this.AllotedKey=AllotedKey;
       this.ClubLocation=ClubLocation;
       this.RollNum=RollNum;
        this.Verified=Verified;
        this.ProfileImage= ProfileImage;

    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getAllotedKey() {
        return AllotedKey;
    }

    public void setAllotedKey(String allotedKey) {
        AllotedKey = allotedKey;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getClubLocation() {
        return ClubLocation;
    }

    public void setClubLocation(String clubLocation) {
        ClubLocation = clubLocation;
    }

    public String getRollNum() {
        return RollNum;
    }

    public void setRollNum(String rollNum) {
        RollNum = rollNum;
    }

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }
}

