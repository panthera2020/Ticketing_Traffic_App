package com.lastma.services;

import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.VehicleType;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.*;
import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.requests.RegisterVehicleRequest;
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
        RegisterDriverRequest driverRequest = new RegisterDriverRequest();
        driverRequest.setFullName("Full Name");
        driverRequest.setMail("email@example.com");
        driverRequest.setPassword("password");
        driverRequest.setPhoneNumber("123456789");
        RegisterOfficerRequest officerRequest = new RegisterOfficerRequest();
        officerRequest.setFullName("Full Name");
        officerRequest.setOfficerNumber("001");
        officerRequest.setPassword("password");
        CreateOffenceRequest createRequest = new CreateOffenceRequest();
        createRequest.setViolationType(ViolationType.BRT_LANE_VIOLATION);
        createRequest.setCode("001");
        RegisterVehicleRequest registerRequest = new RegisterVehicleRequest();
        registerRequest.setVehicleType(VehicleType.CAR);
        registerRequest.setMake("Toyota");
        registerRequest.setModel("Camry");
        registerRequest.setYear("2025");
        registerRequest.setPlateNumber("Plate Number");
        int driverId = driverService.registerDriver(driverRequest).getId();
        registerRequest.setDriverId(driverId);
        int offenceId = offenceService.createOffenceWith(createRequest).getId();
        int officerId = officerService.registerOfficer(officerRequest).getId();
        int vehicleId = vehicleService.register(registerRequest).getId();
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