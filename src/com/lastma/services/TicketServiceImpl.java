package com.lastma.services;

import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.Ticket;
import com.lastma.data.repositories.*;

public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();

    @Override
    public int issueTicket(String ticketNumber, int vehicleId, int offenceId, int officerId, String location, OffenderStatus offenderStatus) {
        validateVehicle(vehicleId);
        validateOfficer(officerId);
        validateOffence(offenceId);
        Ticket newTicket = new Ticket();
        newTicket.setTicketNumber(ticketNumber);
        newTicket.setVehicleId(vehicleId);
        newTicket.setOffenceId(offenceId);
        newTicket.setIssuedByOfficerId(officerId);
        newTicket.setLocation(location);
        newTicket.setStatus(offenderStatus.toString());
        return ticketRepository.save(newTicket).getId();
    }

    private void validateVehicle(int vehicleId){
        vehicleRepository.findById(vehicleId);
    }

    private void validateOfficer(int officerId){
        officerRepository.findById(officerId);
    }

    private void validateOffence(int offenceId){
        offenceRepository.findById(offenceId);
    }
}
