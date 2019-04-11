package com.Aadi.PP.Cards;



public class cards {
    private String userId;
    private String sports;
    private String name;
    private String longitude2;
    private String latitude2;
    private String profileImageUrl;
    public cards (String userId, String name, String sports, String longitude2, String latitude2, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.sports = sports;
        this.longitude2 = longitude2;
        this.latitude2 = latitude2;
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

    public String getLongitude2(){
        return longitude2;
    }
    public void setLongitude2(String longitude2){
        this.longitude2 = longitude2;
    }

    public String getLatitude2(){
        return latitude2;
    } public void setLatitude2(String latitude2){
        this.latitude2 = latitude2;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
