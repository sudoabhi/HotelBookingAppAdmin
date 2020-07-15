package com.example.hotelloadmin.DiscussionsPackage;

/**
 * Created by Asus on 2/4/2020.
 */

public class DiscussionsObject {

    String ImageUrl;
    String UserName;
    String Time;
    String Comment;
    String Key;
    String liked;
    int LikeCount;
    String UserUID;
    String RepliersText;
    String RepliersName;
    String Date;

    public DiscussionsObject(String ImageUrl,String UserName,String Time,String Comment,String Key,String liked,int LikeCount,String UserUID,String RepliersText,String RepliersName,String Date){
        this.ImageUrl=ImageUrl;
        this.Date=Date;
        this.UserName=UserName;
        this.Time=Time;
        this.Key=Key;
        this.Comment=Comment;
        this.liked=liked;
        this.LikeCount=LikeCount;
        this.UserUID=UserUID;
        this.RepliersText=RepliersText;
        this.RepliersName=RepliersName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getRepliersText() {
        return RepliersText;
    }

    public void setRepliersText(String repliersText) {
        RepliersText = repliersText;
    }

    public String getRepliersName() {
        return RepliersName;
    }

    public void setRepliersName(String repliersName) {
        RepliersName = repliersName;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
