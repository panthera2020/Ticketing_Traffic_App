package com.lastma.services;

import com.lastma.data.models.OffenderStatus;

public interface TicketService {
    int issueTicket(String ticketNumber, int vehicleId, int offenceId, int officerId, String location, OffenderStatus offenderStatus);
}
