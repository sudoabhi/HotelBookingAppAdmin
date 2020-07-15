package com.example.hotelloadmin.EventsPackage;

/**
 * Created by Asus on 8/25/2019.
 */

public class EventDetailsFAQsData {

    String Ques;
    String Ans;

    public EventDetailsFAQsData(String Ques, String Ans){
        this.Ques=Ques;
        this.Ans=Ans;


    }

    public String getQues() {
        return Ques;
    }

    public void setQues(String ques) {
        Ques = ques;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }
}
