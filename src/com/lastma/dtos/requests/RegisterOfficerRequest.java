package com.lastma.dtos.requests;

public class RegisterOfficerRequest {
    private String fullName;
    private String officerNumber;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
