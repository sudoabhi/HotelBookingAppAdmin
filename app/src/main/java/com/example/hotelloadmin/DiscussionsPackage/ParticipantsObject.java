package com.example.hotelloadmin.DiscussionsPackage;

/**
 * Created by Asus on 7/21/2019.
 */

public class ParticipantsObject {

    String UserName;
    String UserUID;
    String UserClubLocation;
    String ProfileImage;
    String Activity;

    public ParticipantsObject(String UserName,String UserUID, String UserClubLocation, String ProfileImage, String Activity){
        this.UserName= UserName;
        this.UserUID= UserUID;
        this.UserClubLocation=UserClubLocation;
        this.ProfileImage=ProfileImage;
        this.Activity=Activity;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getUserClubLocation() {
        return UserClubLocation;
    }

    public void setUserClubLocation(String userClubLocation) {
        UserClubLocation = userClubLocation;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }
}
