package com.Aadi.PP.Matches;


public class MatchesObject {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String userState;
    public MatchesObject (String userId, String name, String profileImageUrl, String userState){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.userState = userState;
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

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
