package com.Aadi.PP.Chat;


public class ChatObject {
    private String message;
    private String timestamp;

    private Boolean currentUser;
    public ChatObject(String message, String timestamp, Boolean currentUser){
        this.message = message;
        this.timestamp = timestamp;
        this.currentUser = currentUser;

    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public String getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(String userID){
        this.timestamp = timestamp;
    }



    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }


}
