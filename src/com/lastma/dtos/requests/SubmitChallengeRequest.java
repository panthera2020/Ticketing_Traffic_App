package com.lastma.dtos.requests;

public class SubmitChallengeRequest {
    private int ticketId;
    private String reason;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
