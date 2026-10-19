package com.lastma.data.models;

public class Officer {
    private String fullName;
    private String officerNumber;
    private int id;
    private boolean isActive;
    private String password;

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public boolean isActive() {return isActive;}

    public void activate() {isActive = true;}

    public void deactivate() {isActive = false;}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOfficerNumber() {
        return officerNumber;
    }

    public void setOfficerNumber(String officerNumber) {
        this.officerNumber = officerNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
