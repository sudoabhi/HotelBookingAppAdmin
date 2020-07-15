package com.example.hotelloadmin.EventsPackage;

/**
 * Created by Asus on 7/21/2019.
 */

public class EventAnalyticsRevenueData {

    String TicketName;
    String TeamCheck;
    String PriceCheck;
    int TicketSold;
    int Revenue;
    String TicketKey;

    public EventAnalyticsRevenueData(String TicketName, String TeamCheck, String PriceCheck, int TicketSold,
                                     int Revenue,  String TicketKey){
        this.TicketName= TicketName;
        this.TeamCheck= TeamCheck;
        this.Revenue =Revenue;
        this.TicketSold=TicketSold;
        this.PriceCheck=PriceCheck;
        this.TicketKey=TicketKey;
    }

    public String getTicketName() {
        return TicketName;
    }

    public void setTicketName(String ticketName) {
        TicketName = ticketName;
    }

    public String getTeamCheck() {
        return TeamCheck;
    }

    public void setTeamCheck(String teamCheck) {
        TeamCheck = teamCheck;
    }

    public int getRevenue() {
        return Revenue;
    }

    public void setRevenue(int revenue) {
        Revenue = revenue;
    }

    public int getTicketSold() {
        return TicketSold;
    }

    public void setTicketSold(int ticketSold) {
        TicketSold = ticketSold;
    }

    public String getPriceCheck() {
        return PriceCheck;
    }

    public void setPriceCheck(String priceCheck) {
        PriceCheck = priceCheck;
    }


    public String getTicketKey() {
        return TicketKey;
    }

    public void setTicketKey(String ticketKey) {
        TicketKey = ticketKey;
    }


}
