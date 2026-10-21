package com.lastma.services;

import com.lastma.dtos.requests.FindAllTicketsForVehicleRequest;
import com.lastma.dtos.requests.FindTicketByIdRequest;
import com.lastma.dtos.requests.FindTicketByNumberRequest;
import com.lastma.dtos.requests.IssueTicketRequest;
import com.lastma.dtos.responses.FindAllTicketsForVehicleResponse;
import com.lastma.dtos.responses.FindTicketByIdResponse;
import com.lastma.dtos.responses.FindTicketByNumberResponse;
import com.lastma.dtos.responses.TicketServiceResponse;

import java.util.List;

public interface TicketService {
    TicketServiceResponse issueTicket(IssueTicketRequest request);

    FindTicketByIdResponse findTicketBy(FindTicketByIdRequest idRequest);

    FindTicketByNumberResponse findTicketBy(FindTicketByNumberRequest ticketNumberRequest);

    List<FindAllTicketsForVehicleResponse> findAllTicketsFor(FindAllTicketsForVehicleRequest vehicleRequest);
}
