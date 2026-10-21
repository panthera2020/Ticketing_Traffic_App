package com.lastma.services;

import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.Ticket;
import com.lastma.data.models.VehicleType;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.*;
import com.lastma.dtos.requests.*;
import com.lastma.dtos.responses.FindAllTicketsForVehicleResponse;
import com.lastma.dtos.responses.FindTicketByIdResponse;
import com.lastma.dtos.responses.FindTicketByNumberResponse;
import com.lastma.dtos.responses.TicketServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceImplTest {
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();
    private VehicleService vehicleService;
    private DriverService driverService;
    private OffenceService offenceService;
    private OfficerService officerService;
    private TicketService ticketService;
    private int driverId;
    private int vehicleId;
    private int officerId;
    private int offenceId;
    private IssueTicketRequest issueRequest;

    @BeforeEach
    public void setup() {
        ticketService = new TicketServiceImpl();
        vehicleService = new VehicleServiceImpl();
        driverService = new DriverServiceImpl();
        offenceService = new OffenceServiceImpl();
        officerService = new OfficerServiceImpl();
        ticketRepository.deleteAll();
        vehicleRepository.deleteAll();
        driverRepository.deleteAll();
        officerRepository.deleteAll();
        offenceRepository.deleteAll();
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
        driverId = driverService.registerDriver(driverRequest).getId();
        registerRequest.setDriverId(driverId);
        offenceId = offenceService.createOffenceWith(createRequest).getId();
        officerId = officerService.registerOfficer(officerRequest).getId();
        vehicleId = vehicleService.registerVehicle(registerRequest).getId();
        issueRequest = new IssueTicketRequest();
        issueRequest.setTicketNumber("Ticket Number");
        issueRequest.setVehicleId(vehicleId);
        issueRequest.setOffenceId(offenceId);
        issueRequest.setOfficerId(officerId);
        issueRequest.setLocation("Location");
        issueRequest.setOffenderStatus(OffenderStatus.FIRST_TIME_OFFENDER);
    }

    @Test
    public void testThatThatWhenIssueTicket_countIsOne(){
        assertEquals(0, ticketRepository.count());
        ticketService.issueTicket(issueRequest);
        assertEquals(1, ticketRepository.count());
    }

    @Test
    public void testThatWhen_iIssueTicketWithInvalidVehicleId_errorIsThrown(){
        issueRequest.setVehicleId(-2);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhen_iIssueTicketWithInvalidOfficerId_errorIsThrown(){
        issueRequest.setOfficerId(-3);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhen_iIssueTicketWithInvalidOffenceId_errorIsThrown(){
        issueRequest.setOffenceId(-5);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenIssueTicket_idIsGenerated(){
        assertNotEquals(0, ticketService.issueTicket(issueRequest).getId());
    }

    @Test
    public void testThatWhenIssueTwoTickets_idsAreDifferent(){
        int ticketIdOne = ticketService.issueTicket(issueRequest).getId();
        issueRequest.setTicketNumber("Ticket Number 2");
        int ticketIdTwo = ticketService.issueTicket(issueRequest).getId();
        assertNotEquals(ticketIdOne, ticketIdTwo);
        assertEquals(2, ticketRepository.count());
    }

    @Test
    public void testThatWhenIssueTicket_responseCarriesTheRequestDetails(){
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        assertEquals("Ticket Number", response.getTicketNumber());
        assertEquals(vehicleId, response.getVehicleId());
        assertEquals(offenceId, response.getOffenceId());
        assertEquals(officerId, response.getIssuedByOfficerId());
        assertEquals("Location", response.getLocation());
    }

    @Test
    public void testThatWhenIssueTicket_ticketIsSavedInRepository(){
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        Ticket savedTicket = ticketRepository.findById(response.getId());
        assertNotNull(savedTicket);
        assertEquals(response.getTicketNumber(), savedTicket.getTicketNumber());
        assertEquals(response.getViolationFee(), savedTicket.getViolationFee());
        assertEquals(response.getStatus(), savedTicket.getStatus());
    }

    @Test
    public void testThatWhenIssueTicket_statusIsUnpaid(){
        assertEquals("UNPAID", ticketService.issueTicket(issueRequest).getStatus());
    }

    @Test
    public void testThatWhenIssueTicketToFirstTimeOffender_feeIsFirstOffenceFine(){
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        assertEquals(BigDecimal.valueOf(ViolationType.BRT_LANE_VIOLATION.getFirstOffenceFine()), response.getViolationFee());
    }

    @Test
    public void testThatWhenIssueTicketToSubsequentOffender_feeIsSubsequentOffenceFine(){
        issueRequest.setOffenderStatus(OffenderStatus.SUBSEQUENT_OFFENDER);
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        assertEquals(BigDecimal.valueOf(ViolationType.BRT_LANE_VIOLATION.getSubsequentOffenceFine()), response.getViolationFee());
    }

    @Test
    public void testThatWhenSubsequentOffenceHasNoSubsequentFine_feeIsFirstOffenceFine(){
        CreateOffenceRequest createRequest = new CreateOffenceRequest();
        createRequest.setViolationType(ViolationType.DRIVING_UNDER_INFLUENCE);
        createRequest.setCode("002");
        issueRequest.setOffenceId(offenceService.createOffenceWith(createRequest).getId());
        issueRequest.setOffenderStatus(OffenderStatus.SUBSEQUENT_OFFENDER);
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        assertEquals(BigDecimal.valueOf(ViolationType.DRIVING_UNDER_INFLUENCE.getFirstOffenceFine()), response.getViolationFee());
    }

    @Test
    public void testThatWhenIssueTicket_descriptionIsTakenFromTheOffence(){
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        assertEquals(ViolationType.BRT_LANE_VIOLATION.getDescription(), response.getDescription());
    }

    @Test
    public void testThatWhenIssueTicket_issuedDateIsToday(){
        assertEquals(LocalDate.now(), ticketService.issueTicket(issueRequest).getIssuedDate());
    }

    @Test
    public void testThatWhenIssueTicket_dueDateIsThirtyDaysAfterIssuedDate(){
        TicketServiceResponse response = ticketService.issueTicket(issueRequest);
        assertEquals(response.getIssuedDate().plusDays(30), response.getDueDate());
    }

    @Test
    public void testThatWhenIssueTicketWithDeactivatedOfficer_errorIsThrown(){
        officerService.deactivate(officerId);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenIssueTicketWithDeactivatedOffence_errorIsThrown(){
        offenceService.deactivate(offenceId);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenTicketCannotBeIssued_noTicketIsSaved(){
        issueRequest.setVehicleId(-2);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
        assertEquals(0, ticketRepository.count());
    }

    @Test
    public void testThatTwoTicketsCannotBeIssuedWithTheSameTicketNumber(){
        ticketService.issueTicket(issueRequest);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
        assertEquals(1, ticketRepository.count());
    }

    @Test
    public void testThatWhenIssueTicketWithBlankTicketNumber_errorIsThrown(){
        issueRequest.setTicketNumber(" ");
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenIssueTicketWithNullTicketNumber_errorIsThrown(){
        issueRequest.setTicketNumber(null);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenIssueTicketWithBlankLocation_errorIsThrown(){
        issueRequest.setLocation("");
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenIssueTicketWithNullLocation_errorIsThrown(){
        issueRequest.setLocation(null);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenIssueTicketWithoutOffenderStatus_errorIsThrown(){
        issueRequest.setOffenderStatus(null);
        assertThrows(IllegalArgumentException.class, () -> ticketService.issueTicket(issueRequest));
    }

    @Test
    public void testThatWhenTicketIsIssued_ticketCanBeFoundById(){
        int ticketId = ticketService.issueTicket(issueRequest).getId();
        FindTicketByIdRequest findRequest = new FindTicketByIdRequest();
        findRequest.setTicketId(ticketId);
        FindTicketByIdResponse savedTicketByService = ticketService.findTicketBy(findRequest);
        Ticket savedTicketInRepo = ticketRepository.findById(ticketId);
        assertEquals(savedTicketInRepo.getId(), savedTicketByService.getId());
        assertEquals(savedTicketInRepo.getTicketNumber(), savedTicketByService.getTicketNumber());
        assertEquals(savedTicketInRepo.getViolationFee(), savedTicketByService.getViolationFee());
    }

    @Test
    public void testThatWhenTicketIsSearchedByInvalidId_errorIsThrown(){
        FindTicketByIdRequest findRequest = new FindTicketByIdRequest();
        findRequest.setTicketId(-1);
        assertThrows(IllegalArgumentException.class, () -> ticketService.findTicketBy(findRequest));
    }

    @Test
    public void testThatWhenTicketIsIssued_ticketCanBeFoundByTicketNumber(){
        int ticketId = ticketService.issueTicket(issueRequest).getId();
        issueRequest.setTicketNumber("Ticket Number 2");
        ticketService.issueTicket(issueRequest);
        FindTicketByNumberRequest findRequest = new FindTicketByNumberRequest();
        findRequest.setTicketNumber("Ticket Number");
        FindTicketByNumberResponse foundTicket = ticketService.findTicketBy(findRequest);
        assertEquals(ticketId, foundTicket.getId());
        assertEquals("Ticket Number", foundTicket.getTicketNumber());
    }

    @Test
    public void testThatWhenTicketIsSearchedByUnknownTicketNumber_errorIsThrown(){
        ticketService.issueTicket(issueRequest);
        FindTicketByNumberRequest findRequest = new FindTicketByNumberRequest();
        findRequest.setTicketNumber("Ticket Number 001");
        assertThrows(IllegalArgumentException.class, () -> ticketService.findTicketBy(findRequest));
    }

    @Test
    public void testThatWhenNoTicketIsIssued_searchByTicketNumberThrowsError(){
        FindTicketByNumberRequest findRequest = new FindTicketByNumberRequest();
        findRequest.setTicketNumber("Ticket Number");
        assertThrows(IllegalArgumentException.class, () -> ticketService.findTicketBy(findRequest));
    }

    @Test
    public void testThatICanGetListOfTicketsIssuedToAVehicle(){
        int ticketIdOne = ticketService.issueTicket(issueRequest).getId();
        issueRequest.setTicketNumber("Ticket Number 2");
        int ticketIdTwo = ticketService.issueTicket(issueRequest).getId();
        FindAllTicketsForVehicleRequest vehicleTicketsRequest = new FindAllTicketsForVehicleRequest();
        vehicleTicketsRequest.setVehicleId(vehicleId);
        List<FindAllTicketsForVehicleResponse> vehicleTickets = ticketService.findAllTicketsFor(vehicleTicketsRequest);
        assertEquals(2, vehicleTickets.size());
        assertEquals(ticketIdOne, vehicleTickets.get(0).getId());
        assertEquals(ticketIdTwo, vehicleTickets.get(1).getId());
    }

    @Test
    public void testThatListOfTicketsForAVehicleDoesNotContainTicketsOfOtherVehicles(){
        ticketService.issueTicket(issueRequest);
        RegisterVehicleRequest otherVehicleRequest = new RegisterVehicleRequest();
        otherVehicleRequest.setVehicleType(VehicleType.SUV);
        otherVehicleRequest.setMake("Honda");
        otherVehicleRequest.setModel("CRV");
        otherVehicleRequest.setYear("2022");
        otherVehicleRequest.setPlateNumber("Plate Number 2");
        otherVehicleRequest.setDriverId(driverId);
        int otherVehicleId = vehicleService.registerVehicle(otherVehicleRequest).getId();
        issueRequest.setTicketNumber("Ticket Number 2");
        issueRequest.setVehicleId(otherVehicleId);
        int otherTicketId = ticketService.issueTicket(issueRequest).getId();
        FindAllTicketsForVehicleRequest vehicleTicketsRequest = new FindAllTicketsForVehicleRequest();
        vehicleTicketsRequest.setVehicleId(otherVehicleId);
        List<FindAllTicketsForVehicleResponse> vehicleTickets = ticketService.findAllTicketsFor(vehicleTicketsRequest);
        assertEquals(1, vehicleTickets.size());
        assertEquals(otherTicketId, vehicleTickets.get(0).getId());
    }

    @Test
    public void testThatWhenVehicleHasNoTickets_emptyListIsReturned(){
        FindAllTicketsForVehicleRequest vehicleTicketsRequest = new FindAllTicketsForVehicleRequest();
        vehicleTicketsRequest.setVehicleId(vehicleId);
        assertTrue(ticketService.findAllTicketsFor(vehicleTicketsRequest).isEmpty());
    }

    @Test
    public void testThatWhenIFindTicketsForInvalidVehicleId_errorIsThrown(){
        FindAllTicketsForVehicleRequest vehicleTicketsRequest = new FindAllTicketsForVehicleRequest();
        vehicleTicketsRequest.setVehicleId(-1);
        assertThrows(IllegalArgumentException.class, () -> ticketService.findAllTicketsFor(vehicleTicketsRequest));
    }
}
