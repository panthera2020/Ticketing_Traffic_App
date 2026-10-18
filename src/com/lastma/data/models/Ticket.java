package com.lastma.data.models;

import java.util.Date;

public class Ticket {
    private int id;
    private ViolationType type;
    private int violationFee;
    private Date issuedDate;
    private Date dueDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public ViolationType getType() {
        return type;
    }

    public void setType(ViolationType type) {
        this.type = type;
    }

    public int getViolationFee() {
        return violationFee;
    }

    public void setViolationFee(int violationFee) {
        this.violationFee = violationFee;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


}
