package com.Aadi.PP.Matches;


import android.widget.ImageView;

public class MatchesObject {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String userState;
    private String sports;
    public MatchesObject (String userId, String name, String profileImageUrl, String userState, String sports){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.userState = userState;
        this.sports = sports;
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

    public String getUserLastSeen(){ return userState; }
    public void setUserLastSeen(String userState){
        this.userState = userState;
    }

    public String getSports(){ return sports; }
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
