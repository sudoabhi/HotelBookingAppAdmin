package com.example.hotelloadmin.DiscussionsPackage;

/**
 * Created by Asus on 2/27/2019.
 */

public class BlogsObject {
    String UserName;
    String Skill;
    String ImageUrl;
    String BlogText;
    String UploadedBy;
    String UpdatedTime;
    String UpdatedDay;
    String likeS;
    String dislikeS;
    String Key;
    int NumberOfLikes;
    int NumberOfDislikes;
    String BlogImage;
    String ClubLocation;
    int comments_count;
    String Edited;


    public BlogsObject(){}; //for firebase

    public BlogsObject(String UserName,String ClubLocation,String BlogImage,String ImageUrl, String BlogText, String UploadedBy, String UpdatedTime, String UpdatedDay, String likeS, String dislikeS, String Key, int NumberOfLikes, int NumberOfDislikes,int comments_count,String Edited){
        this.UserName=UserName;
        this.Skill=Skill;
        this.ImageUrl=ImageUrl;
        this.BlogText=BlogText;
        this.UploadedBy=UploadedBy;
        this.UpdatedTime=UpdatedTime;
        this.UpdatedDay=UpdatedDay;
        this.likeS=likeS;
        this.dislikeS=dislikeS;
        this.Key=Key;
        this.NumberOfLikes=NumberOfLikes;
        this.NumberOfDislikes=NumberOfDislikes;
        this.BlogImage=BlogImage;
        this.ClubLocation=ClubLocation;
        this.comments_count=comments_count;
        this.Edited=Edited;

    }

    public String getEdited() {
        return Edited;
    }

    public void setEdited(String edited) {
        Edited = edited;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getClubLocation() {
        return ClubLocation;
    }

    public void setClubLocation(String clubLocation) {
        ClubLocation = clubLocation;
    }

    public String getBlogImage() {
        return BlogImage;
    }

    public void setBlogImage(String blogImage) {
        BlogImage = blogImage;
    }

    public int getNumberOfDislikes() {
        return NumberOfDislikes;
    }

    public void setNumberOfDislikes(int numberOfDislikes) {
        NumberOfDislikes = numberOfDislikes;
    }

    public int getNumberOfLikes() {
        return NumberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        NumberOfLikes = numberOfLikes;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getLikeS() {
        return likeS;
    }

    public void setLikeS(String likeS) {
        this.likeS = likeS;
    }

    public String getDislikeS() {
        return dislikeS;
    }

    public void setDislikeS(String dislikeS) {
        this.dislikeS = dislikeS;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getBlogText() {
        return BlogText;
    }

    public void setBlogText(String blogText) {
        BlogText = blogText;
    }

    public String getUploadedBy() {
        return UploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        UploadedBy = uploadedBy;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        UpdatedTime = updatedTime;
    }

    public String getUpdatedDay() {
        return UpdatedDay;
    }

    public void setUpdatedDay(String updatedDay) {
        UpdatedDay = updatedDay;
    }
}
