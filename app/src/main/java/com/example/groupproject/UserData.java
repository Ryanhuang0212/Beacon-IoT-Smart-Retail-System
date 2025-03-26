package com.example.groupproject;

public class UserData {
    String UserID;
    String Username;
    String Email;
    String Sex;
    String Give;

    public UserData(String UserID, String Username, String Email, String Sex){
        UserID = UserID;
        Username = Username;
        Email = Email;
        Sex = Sex;
        Give = Give;

    }

    public UserData(){

    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getGive(){
        return Give;
    }

    public  void setGive(String give){
        Give = give;
    }
}
