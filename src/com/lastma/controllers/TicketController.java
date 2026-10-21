package com.lastma.controllers;

import com.lastma.dtos.requests.FindAllTicketsForVehicleRequest;
import com.lastma.dtos.requests.FindTicketByIdRequest;
import com.lastma.dtos.requests.FindTicketByNumberRequest;
import com.lastma.dtos.requests.IssueTicketRequest;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.services.TicketService;
import com.lastma.services.TicketServiceImpl;

public class TicketController {
    private TicketService ticketService = new TicketServiceImpl();

    public ApiResponse issueTicket(IssueTicketRequest request){
        try {
            return new ApiResponse(true, ticketService.issueTicket(request));
        }
        catch(Exception e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findTicketById(FindTicketByIdRequest idRequest){
        try {
            return new ApiResponse(true, ticketService.findTicketBy(idRequest));
        }
        catch (Exception e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findTicketByCode(FindTicketByNumberRequest codeRequest){
        try {
            return new ApiResponse(true, ticketService.findTicketBy(codeRequest));
        }
        catch (Exception e) {
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findAllTicketsFor(FindAllTicketsForVehicleRequest request){
        try {
            return new ApiResponse(true, ticketService.findAllTicketsFor(request));
        } catch (Exception e) {
            return new ApiResponse(false, e.getMessage());
        }
    }
}
