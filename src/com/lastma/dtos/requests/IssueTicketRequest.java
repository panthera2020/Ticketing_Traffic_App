package com.lastma.dtos.requests;

import com.lastma.data.models.OffenderStatus;

public class IssueTicketRequest {
    private String ticketNumber;
    private int vehicleId;
    private int offenceId;
    private int officerId;
    private String location;
    private OffenderStatus offenderStatus;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getOffenceId() {
        return offenceId;
    }

    public void setOffenceId(int offenceId) {
        this.offenceId = offenceId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public OffenderStatus getOffenderStatus() {
        return offenderStatus;
    }

    public void setOffenderStatus(OffenderStatus offenderStatus) {
        this.offenderStatus = offenderStatus;
    }
}
