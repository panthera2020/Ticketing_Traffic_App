package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.Officer;
import com.lastma.data.models.Ticket;
import com.lastma.data.repositories.*;
import com.lastma.dtos.requests.FindAllTicketsForVehicleRequest;
import com.lastma.dtos.requests.FindTicketByIdRequest;
import com.lastma.dtos.requests.FindTicketByNumberRequest;
import com.lastma.dtos.requests.IssueTicketRequest;
import com.lastma.dtos.responses.FindAllTicketsForVehicleResponse;
import com.lastma.dtos.responses.FindTicketByIdResponse;
import com.lastma.dtos.responses.FindTicketByNumberResponse;
import com.lastma.dtos.responses.TicketServiceResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.lastma.utils.TicketServiceMapper.*;

public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();

    @Override
    public TicketServiceResponse issueTicket(IssueTicketRequest request) {
        validateTicketNumber(request.getTicketNumber());
        validateLocation(request.getLocation());
        validateOffenderStatus(request.getOffenderStatus());
        validateVehicle(request.getVehicleId());
        validateOfficer(request.getOfficerId());
        validateOffence(request.getOffenceId());
        Offence offence = offenceRepository.findById(request.getOffenceId());
        BigDecimal offenceFee = calculateFee(offence, request.getOffenderStatus());
        Ticket newTicket = map(request, offence, offenceFee);
        Ticket ticket = ticketRepository.save(newTicket);

        return mapIssued(ticket);
    }

    @Override
    public FindTicketByIdResponse findTicketBy(FindTicketByIdRequest request) {
        validateTicket(request.getTicketId());
        Ticket foundTicket = ticketRepository.findById(request.getTicketId());

        return mapId(foundTicket);
    }

    @Override
    public FindTicketByNumberResponse findTicketBy(FindTicketByNumberRequest request) {
        List<Ticket> issuedTickets = findAllTickets();
        for (Ticket foundTicket : issuedTickets) {
            if (foundTicket.getTicketNumber().equals(request.getTicketNumber())) return mapNumber(foundTicket);
        }
        throw new IllegalArgumentException("Ticket with number " + request.getTicketNumber() + " not found");
    }

    @Override
    public List<FindAllTicketsForVehicleResponse> findAllTicketsFor(FindAllTicketsForVehicleRequest request) {
        validateVehicle(request.getVehicleId());
        List<Ticket> issuedTickets = findAllTickets();

        return mapListOf(issuedTickets, request.getVehicleId());
    }

    private BigDecimal calculateFee(Offence offence, OffenderStatus offenderStatus) {
        int fine = offence.getFirstOffenceFine();
        if (offenderStatus == OffenderStatus.SUBSEQUENT_OFFENDER && offence.getSubsequentOffenceFine() > 0) {
            fine = offence.getSubsequentOffenceFine();
        }
        return BigDecimal.valueOf(fine);
    }

    private List<Ticket> findAllTickets() {
        List<Ticket> issuedTickets = ticketRepository.findAll();
        if (issuedTickets == null) return new ArrayList<>();
        return issuedTickets;
    }

    private void validateTicketNumber(String ticketNumber) {
        if (ticketNumber == null || ticketNumber.isBlank()) throw new IllegalArgumentException("Ticket number is required");
        for (Ticket ticket : findAllTickets()) {
            if (ticket.getTicketNumber().equals(ticketNumber)) throw new IllegalArgumentException("Ticket with number " + ticketNumber + " already exists");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.isBlank()) throw new IllegalArgumentException("Location is required");
    }

    private void validateOffenderStatus(OffenderStatus offenderStatus) {
        if (offenderStatus == null) throw new IllegalArgumentException("Offender status is required");
    }

    private void validateTicket(int ticketId) {
        if (ticketRepository.findById(ticketId) == null) throw new IllegalArgumentException("Ticket with id " + ticketId + " not found");
    }

    private void validateVehicle(int vehicleId) {
        if (vehicleRepository.findById(vehicleId) == null) throw new IllegalArgumentException("Vehicle with id " + vehicleId + " not found");
    }

    private void validateOfficer(int officerId) {
        Officer foundOfficer = officerRepository.findById(officerId);
        if (foundOfficer == null) throw new IllegalArgumentException("Officer with id " + officerId + " not found");
        if (!foundOfficer.isActive()) throw new IllegalArgumentException("Officer with id " + officerId + " is not active");
    }

    private void validateOffence(int offenceId) {
        Offence foundOffence = offenceRepository.findById(offenceId);
        if (foundOffence == null) throw new IllegalArgumentException("Offence with id " + offenceId + " not found");
        if (!foundOffence.isActive()) throw new IllegalArgumentException("Offence with id " + offenceId + " is not active");
    }
}
