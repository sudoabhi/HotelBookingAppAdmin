package com.example.hotelloadmin.NewEventPackage;

public class EventTicketsRecyclerData {

    private String ticket_type;
    private String min_members;
    private String max_members;
    private String ticket_name;
    private String ticket_cat;
    private String ticket_desc;
    private String fees_setting;
    private String ticket_price;
    private String ticket_key;

    public EventTicketsRecyclerData(String ticket_key, String ticket_type, String min_members, String max_members, String ticket_name,
                                    String ticket_cat, String ticket_desc, String fees_setting, String ticket_price){

        this.ticket_type=ticket_type;
        this.min_members=min_members;
        this.max_members=max_members;
        this.ticket_name=ticket_name;
        this.ticket_cat=ticket_cat;
        this.ticket_desc=ticket_desc;
        this.fees_setting=fees_setting;
        this.ticket_price=ticket_price;
        this.ticket_key=ticket_key;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }

    public String getTicket_key() {
        return ticket_key;
    }

    public void setTicket_key(String ticket_key) {
        this.ticket_key = ticket_key;
    }

    public String getMin_members() {
        return min_members;
    }

    public void setMin_members(String min_members) {
        this.min_members = min_members;
    }

    public String getMax_members() {
        return max_members;
    }

    public void setMax_members(String max_members) {
        this.max_members = max_members;
    }

    public String getTicket_name() {
        return ticket_name;
    }

    public void setTicket_name(String ticket_name) {
        this.ticket_name = ticket_name;
    }

    public String getTicket_cat() {
        return ticket_cat;
    }

    public void setTicket_cat(String ticket_cat) {
        this.ticket_cat = ticket_cat;
    }

    public String getTicket_desc() {
        return ticket_desc;
    }

    public void setTicket_desc(String ticket_desc) {
        this.ticket_desc = ticket_desc;
    }

    public String getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(String ticket_price) {
        this.ticket_price = ticket_price;
    }

    public String getFees_setting(){
        return fees_setting;
    }

    public void setFees_setting(String fees_setting) {
        this.fees_setting = fees_setting;
    }
}
