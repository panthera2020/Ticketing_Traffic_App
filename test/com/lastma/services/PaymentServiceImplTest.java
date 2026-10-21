package com.lastma.services;

import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.Payment;
import com.lastma.data.models.VehicleType;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.*;
import com.lastma.dtos.requests.*;
import com.lastma.dtos.responses.FindAllPaymentsResponse;
import com.lastma.dtos.responses.FindPaymentByIdResponse;
import com.lastma.dtos.responses.FindPaymentByReferenceResponse;
import com.lastma.dtos.responses.MakePaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {
    private PaymentService paymentService;
    private TicketService ticketService;
    private ChallengeService challengeService;
    private DriverService driverService;
    private VehicleService vehicleService;
    private OfficerService officerService;
    private OffenceService offenceService;
    private PaymentRepository paymentRepository = new PaymentRepositoryImpl();
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private ChallengeRepository challengeRepository = new ChallengeRepositoryImpl();
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();
    private int officerId;
    private int ticketId;
    private BigDecimal ticketFee;
    private MakePaymentRequest paymentRequest;
    private IssueTicketRequest issueRequest;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl();
        ticketService = new TicketServiceImpl();
        challengeService = new ChallengeServiceImpl();
        driverService = new DriverServiceImpl();
        vehicleService = new VehicleServiceImpl();
        officerService = new OfficerServiceImpl();
        offenceService = new OffenceServiceImpl();
        paymentRepository.deleteAll();
        challengeRepository.deleteAll();
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
        int driverId = driverService.registerDriver(driverRequest).getId();
        RegisterVehicleRequest vehicleRequest = new RegisterVehicleRequest();
        vehicleRequest.setVehicleType(VehicleType.CAR);
        vehicleRequest.setMake("Toyota");
        vehicleRequest.setModel("Camry");
        vehicleRequest.setYear("2025");
        vehicleRequest.setPlateNumber("Plate Number");
        vehicleRequest.setDriverId(driverId);
        int vehicleId = vehicleService.registerVehicle(vehicleRequest).getId();
        RegisterOfficerRequest officerRequest = new RegisterOfficerRequest();
        officerRequest.setFullName("Full Name");
        officerRequest.setOfficerNumber("001");
        officerRequest.setPassword("password");
        officerId = officerService.registerOfficer(officerRequest).getId();
        CreateOffenceRequest offenceRequest = new CreateOffenceRequest();
        offenceRequest.setViolationType(ViolationType.BRT_LANE_VIOLATION);
        offenceRequest.setCode("001");
        int offenceId = offenceService.createOffenceWith(offenceRequest).getId();
        issueRequest = new IssueTicketRequest();
        issueRequest.setTicketNumber("Ticket Number");
        issueRequest.setVehicleId(vehicleId);
        issueRequest.setOffenceId(offenceId);
        issueRequest.setOfficerId(officerId);
        issueRequest.setLocation("Location");
        issueRequest.setOffenderStatus(OffenderStatus.FIRST_TIME_OFFENDER);
        ticketId = ticketService.issueTicket(issueRequest).getId();
        ticketFee = BigDecimal.valueOf(ViolationType.BRT_LANE_VIOLATION.getFirstOffenceFine());
        paymentRequest = new MakePaymentRequest();
        paymentRequest.setTicketId(ticketId);
        paymentRequest.setPaymentReference("PAY-001");
        paymentRequest.setAmount(ticketFee);
    }

    private String statusOfTicket(int id) {
        FindTicketByIdRequest findRequest = new FindTicketByIdRequest();
        findRequest.setTicketId(id);
        return ticketService.findTicketBy(findRequest).getStatus();
    }

    private int issueAnotherTicket(String ticketNumber) {
        issueRequest.setTicketNumber(ticketNumber);
        return ticketService.issueTicket(issueRequest).getId();
    }

    @Test
    public void testThatWhenPaymentIsMade_countIsOne() {
        assertEquals(0, paymentRepository.count());
        paymentService.makePayment(paymentRequest);
        assertEquals(1, paymentRepository.count());
    }

    @Test
    public void testThatWhenPaymentIsMade_idIsGenerated() {
        assertNotEquals(0, paymentService.makePayment(paymentRequest).getId());
    }

    @Test
    public void testThatWhenPaymentIsMade_responseCarriesPaymentDetails() {
        MakePaymentResponse response = paymentService.makePayment(paymentRequest);
        assertEquals(ticketId, response.getTicketId());
        assertEquals("PAY-001", response.getPaymentReference());
        assertEquals(ticketFee, response.getAmount());
    }

    @Test
    public void testThatWhenPaymentIsMade_paymentIsSavedInRepository() {
        int paymentId = paymentService.makePayment(paymentRequest).getId();
        Payment savedPayment = paymentRepository.findById(paymentId);
        assertNotNull(savedPayment);
        assertEquals(ticketId, savedPayment.getTicketId());
        assertEquals("PAY-001", savedPayment.getPaymentReference());
    }

    @Test
    public void testThatWhenPaymentIsMade_paymentDateIsToday() {
        assertEquals(LocalDate.now(), paymentService.makePayment(paymentRequest).getPaymentDate());
    }

    @Test
    public void testThatWhenPaymentIsMade_ticketIsMarkedAsPaid() {
        assertEquals("UNPAID", statusOfTicket(ticketId));
        paymentService.makePayment(paymentRequest);
        assertEquals("PAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatWhenPaymentIsMadeForOneTicket_otherTicketsRemainUnpaid() {
        int otherTicketId = issueAnotherTicket("Ticket Number 2");
        paymentService.makePayment(paymentRequest);
        assertEquals("UNPAID", statusOfTicket(otherTicketId));
    }

    @Test
    public void testThatWhenIPayForInvalidTicketId_errorIsThrown() {
        paymentRequest.setTicketId(-1);
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatPaidTicketCannotBePaidForAgain() {
        paymentService.makePayment(paymentRequest);
        paymentRequest.setPaymentReference("PAY-002");
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
        assertEquals(1, paymentRepository.count());
    }

    @Test
    public void testThatWhenIPayLessThanTheTicketFee_errorIsThrown() {
        paymentRequest.setAmount(ticketFee.subtract(BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatWhenIPayMoreThanTheTicketFee_errorIsThrown() {
        paymentRequest.setAmount(ticketFee.add(BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatWhenIPayWithoutAmount_errorIsThrown() {
        paymentRequest.setAmount(null);
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatWhenPaymentFails_ticketRemainsUnpaidAndNoPaymentIsSaved() {
        paymentRequest.setAmount(BigDecimal.ONE);
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
        assertEquals("UNPAID", statusOfTicket(ticketId));
        assertEquals(0, paymentRepository.count());
    }

    @Test
    public void testThatWhenIPayWithBlankReference_errorIsThrown() {
        paymentRequest.setPaymentReference(" ");
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatWhenIPayWithNullReference_errorIsThrown() {
        paymentRequest.setPaymentReference(null);
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatTwoPaymentsCannotShareTheSamePaymentReference() {
        paymentService.makePayment(paymentRequest);
        paymentRequest.setTicketId(issueAnotherTicket("Ticket Number 2"));
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
        assertEquals(1, paymentRepository.count());
    }

    @Test
    public void testThatWaivedTicketCannotBePaidFor() {
        SubmitChallengeRequest submitRequest = new SubmitChallengeRequest();
        submitRequest.setTicketId(ticketId);
        submitRequest.setReason("I was not driving");
        int challengeId = challengeService.submitChallenge(submitRequest).getId();
        ReviewChallengeRequest reviewRequest = new ReviewChallengeRequest();
        reviewRequest.setChallengeId(challengeId);
        reviewRequest.setOfficerId(officerId);
        reviewRequest.setDecisionNotes("Evidence accepted");
        challengeService.approveChallenge(reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
    }

    @Test
    public void testThatTicketWithPendingChallengeCannotBePaidFor() {
        SubmitChallengeRequest submitRequest = new SubmitChallengeRequest();
        submitRequest.setTicketId(ticketId);
        submitRequest.setReason("I was not driving");
        challengeService.submitChallenge(submitRequest);
        assertThrows(IllegalArgumentException.class, () -> paymentService.makePayment(paymentRequest));
        assertEquals("UNPAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatTicketWithRejectedChallengeCanBePaidFor() {
        SubmitChallengeRequest submitRequest = new SubmitChallengeRequest();
        submitRequest.setTicketId(ticketId);
        submitRequest.setReason("I was not driving");
        int challengeId = challengeService.submitChallenge(submitRequest).getId();
        ReviewChallengeRequest reviewRequest = new ReviewChallengeRequest();
        reviewRequest.setChallengeId(challengeId);
        reviewRequest.setOfficerId(officerId);
        reviewRequest.setDecisionNotes("No evidence provided");
        challengeService.rejectChallenge(reviewRequest);
        paymentService.makePayment(paymentRequest);
        assertEquals("PAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatWhenPaymentIsMade_paymentCanBeFoundById() {
        int paymentId = paymentService.makePayment(paymentRequest).getId();
        FindPaymentByIdRequest findRequest = new FindPaymentByIdRequest();
        findRequest.setPaymentId(paymentId);
        FindPaymentByIdResponse savedPaymentByService = paymentService.findPaymentBy(findRequest);
        Payment savedPaymentInRepo = paymentRepository.findById(paymentId);
        assertEquals(savedPaymentInRepo.getId(), savedPaymentByService.getId());
        assertEquals(savedPaymentInRepo.getAmount(), savedPaymentByService.getAmount());
        assertEquals(savedPaymentInRepo.getTicketId(), savedPaymentByService.getTicketId());
    }

    @Test
    public void testThatWhenIFindPaymentByInvalidId_errorIsThrown() {
        FindPaymentByIdRequest findRequest = new FindPaymentByIdRequest();
        findRequest.setPaymentId(-1);
        assertThrows(IllegalArgumentException.class, () -> paymentService.findPaymentBy(findRequest));
    }

    @Test
    public void testThatWhenPaymentIsMade_paymentCanBeFoundByReference() {
        int paymentId = paymentService.makePayment(paymentRequest).getId();
        paymentRequest.setTicketId(issueAnotherTicket("Ticket Number 2"));
        paymentRequest.setPaymentReference("PAY-002");
        paymentService.makePayment(paymentRequest);
        FindPaymentByReferenceRequest findRequest = new FindPaymentByReferenceRequest();
        findRequest.setPaymentReference("PAY-001");
        FindPaymentByReferenceResponse foundPayment = paymentService.findPaymentBy(findRequest);
        assertEquals(paymentId, foundPayment.getId());
        assertEquals("PAY-001", foundPayment.getPaymentReference());
    }

    @Test
    public void testThatWhenIFindPaymentByUnknownReference_errorIsThrown() {
        paymentService.makePayment(paymentRequest);
        FindPaymentByReferenceRequest findRequest = new FindPaymentByReferenceRequest();
        findRequest.setPaymentReference("PAY-999");
        assertThrows(IllegalArgumentException.class, () -> paymentService.findPaymentBy(findRequest));
    }

    @Test
    public void testThatWhenNoPaymentIsMade_findByReferenceThrowsError() {
        FindPaymentByReferenceRequest findRequest = new FindPaymentByReferenceRequest();
        findRequest.setPaymentReference("PAY-001");
        assertThrows(IllegalArgumentException.class, () -> paymentService.findPaymentBy(findRequest));
    }

    @Test
    public void testThatICanFindAllPayments() {
        paymentService.makePayment(paymentRequest);
        paymentRequest.setTicketId(issueAnotherTicket("Ticket Number 2"));
        paymentRequest.setPaymentReference("PAY-002");
        paymentService.makePayment(paymentRequest);
        List<FindAllPaymentsResponse> paymentsFromService = paymentService.findAllPayments();
        List<Payment> paymentsFromRepo = paymentRepository.findAll();
        assertEquals(2, paymentsFromService.size());
        for (int index = 0; index < paymentsFromRepo.size(); index++) {
            assertEquals(paymentsFromRepo.get(index).getId(), paymentsFromService.get(index).getId());
            assertEquals(paymentsFromRepo.get(index).getPaymentReference(), paymentsFromService.get(index).getPaymentReference());
        }
    }

    @Test
    public void testThatWhenNoPaymentIsMade_findAllPaymentsReturnsEmptyList() {
        assertTrue(paymentService.findAllPayments().isEmpty());
    }
}
