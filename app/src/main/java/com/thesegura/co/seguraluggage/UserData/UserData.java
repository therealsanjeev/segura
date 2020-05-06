package com.thesegura.co.seguraluggage.UserData;

public class UserData {
    private String userName;
    private String userPhone;
    private String userLuggage;

    public UserData(){

    }
    public UserData(String userName, String userPhone, String userLuggage) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userLuggage = userLuggage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserLuggage(String userLuggage) {
        this.userLuggage = userLuggage;
    }

    public String getUserName() { return userName; }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserLuggage() {
        return userLuggage;
    }
}
