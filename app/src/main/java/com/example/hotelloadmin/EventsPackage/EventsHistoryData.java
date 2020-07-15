package com.example.hotelloadmin.EventsPackage;

import java.util.ArrayList;

/**
 * Created by Asus on 2/21/2019.
 */

public class EventsHistoryData {
    String EventName;
    String EventBannerUrl;
    String EventKey;

    String Start_Event_Date;
    String End_Event_Date;
    String EventDescription;


    String UpdatedDay;
    String UploadedBy;

    String EventTypeX;

    String EventLocation;
    String EventType;
    String PriceSegment;


    EventsHistoryData(String EventName, String EventBannerUrl, String eventDescription, String start_Event_Date,
                      String end_Event_Date, String UpdatedDay, String UploadedBy, String EventKey, String EventTypeX,
                      String EventLocation, String EventType, String PriceSegment){

        this.EventTypeX=EventTypeX;

        this.PriceSegment=PriceSegment;
        this.EventName=EventName;
        this.EventBannerUrl=EventBannerUrl;
        this.EventLocation=EventLocation;


        this.EventType=EventType;

        this.Start_Event_Date=start_Event_Date;
        this.End_Event_Date=end_Event_Date;
        this.EventKey=EventKey;

        EventDescription=eventDescription;


        this.UpdatedDay=UpdatedDay;
        this.UploadedBy=UploadedBy;





    }


    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventBannerUrl() {
        return EventBannerUrl;
    }

    public void setEventBannerUrl(String eventBannerUrl) {
        EventBannerUrl = eventBannerUrl;
    }

    public String getKey() {
        return EventKey;
    }

    public void setKey(String key) {
        EventKey = key;
    }

    public String getPriceSegment() {
        return PriceSegment;
    }

    public void setPriceSegment(String priceSegment) {
        PriceSegment = priceSegment;
    }






    public String getEventTypeX() {
        return EventTypeX;
    }



    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public void setEventTypeX(String eventTypeX) {
        EventTypeX = eventTypeX;
    }



    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }





    public String getUploadedBy() {
        return UploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        UploadedBy = uploadedBy;
    }

    public String getUpdatedDay() {
        return UpdatedDay;
    }

    public void setUpdatedDay(String updatedDay) {
        UpdatedDay = updatedDay;
    }

    public String getStart_Event_Date() {
        return Start_Event_Date;
    }

    public void setStart_Event_Date(String event_Dates) {
        Start_Event_Date = event_Dates;
    }


    public String getEnd_Event_Date() {
        return End_Event_Date;
    }

    public void setEnd_Event_Date(String event_Dates) {
        End_Event_Date = event_Dates;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }



}
