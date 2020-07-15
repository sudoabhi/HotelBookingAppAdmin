package com.example.hotelloadmin.HomePackage;

/**
 * Created by Asus on 7/21/2019.
 */

public class AttendeesData {

    String UserName;
    String UserUID;
    String College;
    String ProfileImage;
    String District;
    String AttendeeKey;
    String EventName;
    String DateBooked;

    public AttendeesData(String UserName, String UserUID, String College, String ProfileImage,
                         String Location, String AttendeeKey, String EventName,String DateBooked){
        this.UserName= UserName;
        this.UserUID= UserUID;
        this.District =Location;
        this.ProfileImage=ProfileImage;
        this.College=College;
        this.AttendeeKey=AttendeeKey;
        this.EventName=EventName;
        this.DateBooked=DateBooked;
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

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String Location) {
        District = Location;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getAttendeeKey() {
        return AttendeeKey;
    }

    public void setAttendeeKey(String key) {
        AttendeeKey = key;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getDateBooked() {
        return DateBooked;
    }

    public void setDateBooked(String dateBooked) {
        DateBooked = dateBooked;
    }
}
