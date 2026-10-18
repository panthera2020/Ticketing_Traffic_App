package com.lastma.data.models;

public class Driver {
    private String fullName;
    private String email;
    private int id;
    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarPhoneNumber() {
        return phoneNumber;
    }

    public void setCarPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
