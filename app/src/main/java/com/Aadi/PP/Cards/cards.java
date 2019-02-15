package com.Aadi.PP.Cards;



public class cards {
    private String userId;
    private String sports;
    private String name;
    private String profileImageUrl;
    public cards (String userId, String name, String sports, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.sports = sports;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSports(){
        return sports;
    }
    public void setSports(String sports){
        this.sports = sports;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
