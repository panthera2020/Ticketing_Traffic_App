package com.lastma.services;

import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.VehicleType;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceImplTest {
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private VehicleService vehicleService = new VehicleServiceImpl();
    private DriverService driverService = new DriverServiceImpl();
    private OffenceService offenceService = new OffenceServiceImpl();
    private OfficerService officerService = new OfficerServiceImpl();
    private TicketService ticketService;

    @BeforeEach
    public void setup() {
        ticketService = new TicketServiceImpl();
        ticketRepository.deleteAll();
    }

    @Test
    public void testThatThatWhenIssueTicket_countIsOne(){
        assertEquals(0, ticketRepository.count());
        int driverId = driverService.register("Full Name", "email@example.com","PhoneNumber","Password").getId();
        int offenceId = offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION,"001");
        int officerId = officerService.registerOfficer("Full Name","001","Passw0rd");
        int vehicleId = vehicleService.register("Toyota","Camry","2025", VehicleType.CAR,driverId,"Plate Number");
        ticketService.issueTicket("Ticket Number", vehicleId,offenceId,officerId,"Location", OffenderStatus.FIRST_TIME_OFFENDER);
        assertEquals(1, ticketRepository.count());
    }

    @Test
    public void testThatWhen_iIssueTicketWithInvalidVehicleId_errorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket("Ticket Number", -2,1,1,"Location", OffenderStatus.FIRST_TIME_OFFENDER));
    }

    @Test
    public void testThatWhen_iIssueTicketWithInvalidOfficerId_errorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket("Ticket Number", -2,1,-3,"Location", OffenderStatus.FIRST_TIME_OFFENDER));
    }

    @Test
    public void testThatWhen_iIssueTicketWithInvalidOffenceId_errorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket("Ticket Number", -2,-5,-3,"Location", OffenderStatus.FIRST_TIME_OFFENDER));
    }
}