package com.lastma.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TicketServiceResponse {
    private int id;
    private int offenceId;
    private int vehicleId;
    private int issuedByOfficerId;
    private String ticketNumber;
    private String Location;
    private String description;
    private String status;
    private BigDecimal violationFee;
    private LocalDate issuedDate;
    private LocalDate dueDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffenceId() {
        return offenceId;
    }

    public void setOffenceId(int offenceId) {
        this.offenceId = offenceId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getIssuedByOfficerId() {
        return issuedByOfficerId;
    }

    public void setIssuedByOfficerId(int issuedByOfficerId) {
        this.issuedByOfficerId = issuedByOfficerId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getViolationFee() {
        return violationFee;
    }

    public void setViolationFee(BigDecimal violationFee) {
        this.violationFee = violationFee;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


}
