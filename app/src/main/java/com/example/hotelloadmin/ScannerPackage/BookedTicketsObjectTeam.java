package com.example.hotelloadmin.ScannerPackage;

/**
 * Created by Asus on 1/10/2020.
 */

public class BookedTicketsObjectTeam {

    String TeamName;
    String Verified;
    long TeamMembersCount;
    String AllotedKey;

   public BookedTicketsObjectTeam(String TeamName, String Verified, long TeamMembersCount,String AllotedKey){

       this.TeamName=TeamName;
       this.Verified=Verified;
       this.AllotedKey=AllotedKey;
       this.TeamMembersCount=TeamMembersCount;

   }

    public String getAllotedKey() {
        return AllotedKey;
    }

    public void setAllotedKey(String allotedKey) {
        AllotedKey = allotedKey;
    }

    public long getTeamMembersCount() {
        return TeamMembersCount;
    }

    public void setTeamMembersCount(long teamMembersCount) {
        TeamMembersCount = teamMembersCount;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }
}
