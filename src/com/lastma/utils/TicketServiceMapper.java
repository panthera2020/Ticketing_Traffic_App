package com.lastma.utils;

import com.lastma.data.models.Offence;
import com.lastma.data.models.Ticket;
import com.lastma.data.models.TicketStatus;
import com.lastma.dtos.requests.IssueTicketRequest;
import com.lastma.dtos.responses.FindAllTicketsForVehicleResponse;
import com.lastma.dtos.responses.FindTicketByIdResponse;
import com.lastma.dtos.responses.FindTicketByNumberResponse;
import com.lastma.dtos.responses.TicketServiceResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketServiceMapper {
    public static Ticket map(IssueTicketRequest request , Offence offence, BigDecimal offenceFee ) {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(request.getTicketNumber());
        ticket.setVehicleId(request.getVehicleId());
        ticket.setOffenceId(request.getOffenceId());
        ticket.setIssuedByOfficerId(request.getOfficerId());
        ticket.setLocation(request.getLocation());
        ticket.setDescription(offence.getDescription());
        ticket.setViolationFee(offenceFee);
        ticket.setLocation(request.getLocation());
        ticket.setStatus(TicketStatus.UNPAID.toString());
        ticket.setIssuedDate(LocalDate.now());
        ticket.setDueDate(ticket.getIssuedDate().plusDays(30));
        return ticket;
    }

    public static TicketServiceResponse mapIssued(Ticket ticket) {
        TicketServiceResponse response = new TicketServiceResponse();
        response.setId(ticket.getId());
        response.setOffenceId(ticket.getOffenceId());
        response.setVehicleId(ticket.getVehicleId());
        response.setIssuedByOfficerId(ticket.getIssuedByOfficerId());
        response.setTicketNumber(ticket.getTicketNumber());
        response.setLocation(ticket.getLocation());
        response.setDescription(ticket.getDescription());
        response.setStatus(ticket.getStatus());
        response.setViolationFee(ticket.getViolationFee());
        response.setIssuedDate(ticket.getIssuedDate());
        response.setDueDate(ticket.getDueDate());
        return response;
    }

    public static FindTicketByIdResponse mapId(Ticket foundTicket) {
        FindTicketByIdResponse response = new FindTicketByIdResponse();
        response.setId(foundTicket.getId());
        response.setOffenceId(foundTicket.getOffenceId());
        response.setVehicleId(foundTicket.getVehicleId());
        response.setIssuedByOfficerId(foundTicket.getIssuedByOfficerId());
        response.setTicketNumber(foundTicket.getTicketNumber());
        response.setLocation(foundTicket.getLocation());
        response.setDescription(foundTicket.getDescription());
        response.setStatus(foundTicket.getStatus());
        response.setViolationFee(foundTicket.getViolationFee());
        response.setIssuedDate(foundTicket.getIssuedDate());
        response.setDueDate(foundTicket.getDueDate());
        return response;
    }

    public static FindTicketByNumberResponse mapNumber(Ticket foundTicket) {
        FindTicketByNumberResponse response = new FindTicketByNumberResponse();
        response.setId(foundTicket.getId());
        response.setOffenceId(foundTicket.getOffenceId());
        response.setVehicleId(foundTicket.getVehicleId());
        response.setIssuedByOfficerId(foundTicket.getIssuedByOfficerId());
        response.setTicketNumber(foundTicket.getTicketNumber());
        response.setLocation(foundTicket.getLocation());
        response.setDescription(foundTicket.getDescription());
        response.setStatus(foundTicket.getStatus());
        response.setViolationFee(foundTicket.getViolationFee());
        response.setIssuedDate(foundTicket.getIssuedDate());
        response.setDueDate(foundTicket.getDueDate());
        return response;
    }

    public static List<FindAllTicketsForVehicleResponse> mapListOf(List<Ticket> issuedTickets, int vehicleId) {
        List<FindAllTicketsForVehicleResponse> vehicleTickets = new ArrayList<>();
        for (Ticket ticket : issuedTickets) {
            if (ticket.getVehicleId() == vehicleId) {
                FindAllTicketsForVehicleResponse response = new FindAllTicketsForVehicleResponse();
                response.setId(ticket.getId());
                response.setOffenceId(ticket.getOffenceId());
                response.setVehicleId(ticket.getVehicleId());
                response.setIssuedByOfficerId(ticket.getIssuedByOfficerId());
                response.setTicketNumber(ticket.getTicketNumber());
                response.setLocation(ticket.getLocation());
                response.setDescription(ticket.getDescription());
                response.setStatus(ticket.getStatus());
                response.setViolationFee(ticket.getViolationFee());
                response.setIssuedDate(ticket.getIssuedDate());
                response.setDueDate(ticket.getDueDate());
                vehicleTickets.add(response);
            }
        }
        return vehicleTickets;
    }
}
