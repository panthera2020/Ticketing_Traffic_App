package com.lastma.services;

import com.lastma.data.models.Challenge;
import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.VehicleType;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.*;
import com.lastma.dtos.requests.*;
import com.lastma.dtos.responses.FindAllChallengesForTicketResponse;
import com.lastma.dtos.responses.FindChallengeByIdResponse;
import com.lastma.dtos.responses.ReviewChallengeResponse;
import com.lastma.dtos.responses.SubmitChallengeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeServiceImplTest {
    private ChallengeService challengeService;
    private TicketService ticketService;
    private PaymentService paymentService;
    private DriverService driverService;
    private VehicleService vehicleService;
    private OfficerService officerService;
    private OffenceService offenceService;
    private ChallengeRepository challengeRepository = new ChallengeRepositoryImpl();
    private PaymentRepository paymentRepository = new PaymentRepositoryImpl();
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private VehicleRepository vehicleRepository = new VehicleRepositoryImpl();
    private DriverRepository driverRepository = new DriverRepositoryImpl();
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();
    private int officerId;
    private int ticketId;
    private IssueTicketRequest issueRequest;
    private SubmitChallengeRequest submitRequest;
    private ReviewChallengeRequest reviewRequest;

    @BeforeEach
    void setUp() {
        challengeService = new ChallengeServiceImpl();
        ticketService = new TicketServiceImpl();
        paymentService = new PaymentServiceImpl();
        driverService = new DriverServiceImpl();
        vehicleService = new VehicleServiceImpl();
        officerService = new OfficerServiceImpl();
        offenceService = new OffenceServiceImpl();
        challengeRepository.deleteAll();
        paymentRepository.deleteAll();
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
        int vehicleId = vehicleService.register(vehicleRequest).getId();
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
        submitRequest = new SubmitChallengeRequest();
        submitRequest.setTicketId(ticketId);
        submitRequest.setReason("I was not driving the vehicle");
        reviewRequest = new ReviewChallengeRequest();
        reviewRequest.setOfficerId(officerId);
        reviewRequest.setDecisionNotes("Reviewed the evidence");
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
    public void testThatWhenChallengeIsSubmitted_countIsOne() {
        assertEquals(0, challengeRepository.count());
        challengeService.submitChallenge(submitRequest);
        assertEquals(1, challengeRepository.count());
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_idIsGenerated() {
        assertNotEquals(0, challengeService.submitChallenge(submitRequest).getId());
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_responseCarriesChallengeDetails() {
        SubmitChallengeResponse response = challengeService.submitChallenge(submitRequest);
        assertEquals(ticketId, response.getTicketId());
        assertEquals("I was not driving the vehicle", response.getReason());
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_statusIsPending() {
        assertEquals("PENDING", challengeService.submitChallenge(submitRequest).getStatus());
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_submittedDateIsToday() {
        assertEquals(LocalDate.now(), challengeService.submitChallenge(submitRequest).getSubmittedAt());
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_challengeIsSavedInRepository() {
        int challengeId = challengeService.submitChallenge(submitRequest).getId();
        Challenge savedChallenge = challengeRepository.findById(challengeId);
        assertNotNull(savedChallenge);
        assertEquals(ticketId, savedChallenge.getTicketId());
        assertEquals("PENDING", savedChallenge.getStatus());
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_ticketRemainsUnpaid() {
        challengeService.submitChallenge(submitRequest);
        assertEquals("UNPAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatWhenIChallengeInvalidTicketId_errorIsThrown() {
        submitRequest.setTicketId(-1);
        assertThrows(IllegalArgumentException.class, () -> challengeService.submitChallenge(submitRequest));
    }

    @Test
    public void testThatWhenIChallengeWithBlankReason_errorIsThrown() {
        submitRequest.setReason(" ");
        assertThrows(IllegalArgumentException.class, () -> challengeService.submitChallenge(submitRequest));
    }

    @Test
    public void testThatWhenIChallengeWithNullReason_errorIsThrown() {
        submitRequest.setReason(null);
        assertThrows(IllegalArgumentException.class, () -> challengeService.submitChallenge(submitRequest));
    }

    @Test
    public void testThatPaidTicketCannotBeChallenged() {
        MakePaymentRequest paymentRequest = new MakePaymentRequest();
        paymentRequest.setTicketId(ticketId);
        paymentRequest.setPaymentReference("PAY-001");
        paymentRequest.setAmount(BigDecimal.valueOf(ViolationType.BRT_LANE_VIOLATION.getFirstOffenceFine()));
        paymentService.makePayment(paymentRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.submitChallenge(submitRequest));
        assertEquals(0, challengeRepository.count());
    }

    @Test
    public void testThatTicketCannotHaveTwoPendingChallenges() {
        challengeService.submitChallenge(submitRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.submitChallenge(submitRequest));
        assertEquals(1, challengeRepository.count());
    }

    @Test
    public void testThatDifferentTicketsCanEachHaveAPendingChallenge() {
        challengeService.submitChallenge(submitRequest);
        submitRequest.setTicketId(issueAnotherTicket("Ticket Number 2"));
        challengeService.submitChallenge(submitRequest);
        assertEquals(2, challengeRepository.count());
    }

    @Test
    public void testThatWhenChallengeIsApproved_statusIsApproved() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        ReviewChallengeResponse response = challengeService.approveChallenge(reviewRequest);
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    public void testThatWhenChallengeIsApproved_ticketIsWaived() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.approveChallenge(reviewRequest);
        assertEquals("WAIVED", statusOfTicket(ticketId));
    }

    @Test
    public void testThatWhenChallengeIsApproved_reviewDetailsAreSaved() {
        int challengeId = challengeService.submitChallenge(submitRequest).getId();
        reviewRequest.setChallengeId(challengeId);
        ReviewChallengeResponse response = challengeService.approveChallenge(reviewRequest);
        Challenge savedChallenge = challengeRepository.findById(challengeId);
        assertEquals(officerId, response.getReviewedByOfficerId());
        assertEquals("Reviewed the evidence", response.getDecisionNotes());
        assertEquals(LocalDate.now(), response.getRevisedAt());
        assertEquals("APPROVED", savedChallenge.getStatus());
        assertEquals(officerId, savedChallenge.getReviewedByOfficerId());
    }

    @Test
    public void testThatWhenIApproveInvalidChallengeId_errorIsThrown() {
        reviewRequest.setChallengeId(-1);
        assertThrows(IllegalArgumentException.class, () -> challengeService.approveChallenge(reviewRequest));
    }

    @Test
    public void testThatWhenIApproveChallengeWithInvalidOfficerId_errorIsThrown() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        reviewRequest.setOfficerId(-1);
        assertThrows(IllegalArgumentException.class, () -> challengeService.approveChallenge(reviewRequest));
        assertEquals("UNPAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatDeactivatedOfficerCannotApproveChallenge() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        officerService.deactivate(officerId);
        assertThrows(IllegalArgumentException.class, () -> challengeService.approveChallenge(reviewRequest));
    }

    @Test
    public void testThatApprovedChallengeCannotBeApprovedAgain() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.approveChallenge(reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.approveChallenge(reviewRequest));
    }

    @Test
    public void testThatApprovedChallengeCannotBeRejected() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.approveChallenge(reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.rejectChallenge(reviewRequest));
        assertEquals("WAIVED", statusOfTicket(ticketId));
    }

    @Test
    public void testThatWhenChallengeIsRejected_statusIsRejected() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        ReviewChallengeResponse response = challengeService.rejectChallenge(reviewRequest);
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    public void testThatWhenChallengeIsRejected_ticketRemainsUnpaid() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.rejectChallenge(reviewRequest);
        assertEquals("UNPAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatWhenChallengeIsRejected_reviewDetailsAreSaved() {
        int challengeId = challengeService.submitChallenge(submitRequest).getId();
        reviewRequest.setChallengeId(challengeId);
        ReviewChallengeResponse response = challengeService.rejectChallenge(reviewRequest);
        Challenge savedChallenge = challengeRepository.findById(challengeId);
        assertEquals(officerId, response.getReviewedByOfficerId());
        assertEquals("Reviewed the evidence", response.getDecisionNotes());
        assertEquals(LocalDate.now(), response.getRevisedAt());
        assertEquals("REJECTED", savedChallenge.getStatus());
    }

    @Test
    public void testThatWhenIRejectChallengeWithoutDecisionNotes_errorIsThrown() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        reviewRequest.setDecisionNotes(" ");
        assertThrows(IllegalArgumentException.class, () -> challengeService.rejectChallenge(reviewRequest));
        assertEquals("PENDING", challengeRepository.findById(reviewRequest.getChallengeId()).getStatus());
    }

    @Test
    public void testThatWhenIRejectInvalidChallengeId_errorIsThrown() {
        reviewRequest.setChallengeId(-1);
        assertThrows(IllegalArgumentException.class, () -> challengeService.rejectChallenge(reviewRequest));
    }

    @Test
    public void testThatDeactivatedOfficerCannotRejectChallenge() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        officerService.deactivate(officerId);
        assertThrows(IllegalArgumentException.class, () -> challengeService.rejectChallenge(reviewRequest));
    }

    @Test
    public void testThatRejectedChallengeCannotBeRejectedAgain() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.rejectChallenge(reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.rejectChallenge(reviewRequest));
    }

    @Test
    public void testThatRejectedChallengeCannotBeApproved() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.rejectChallenge(reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.approveChallenge(reviewRequest));
        assertEquals("UNPAID", statusOfTicket(ticketId));
    }

    @Test
    public void testThatTicketCanBeChallengedAgainAfterChallengeIsRejected() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.rejectChallenge(reviewRequest);
        challengeService.submitChallenge(submitRequest);
        assertEquals(2, challengeRepository.count());
    }

    @Test
    public void testThatWaivedTicketCannotBeChallengedAgain() {
        reviewRequest.setChallengeId(challengeService.submitChallenge(submitRequest).getId());
        challengeService.approveChallenge(reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> challengeService.submitChallenge(submitRequest));
    }

    @Test
    public void testThatWhenChallengeIsSubmitted_challengeCanBeFoundById() {
        int challengeId = challengeService.submitChallenge(submitRequest).getId();
        FindChallengeByIdRequest findRequest = new FindChallengeByIdRequest();
        findRequest.setChallengeId(challengeId);
        FindChallengeByIdResponse savedChallengeByService = challengeService.findChallengeBy(findRequest);
        Challenge savedChallengeInRepo = challengeRepository.findById(challengeId);
        assertEquals(savedChallengeInRepo.getId(), savedChallengeByService.getId());
        assertEquals(savedChallengeInRepo.getReason(), savedChallengeByService.getReason());
        assertEquals(savedChallengeInRepo.getStatus(), savedChallengeByService.getStatus());
    }

    @Test
    public void testThatWhenIFindChallengeByInvalidId_errorIsThrown() {
        FindChallengeByIdRequest findRequest = new FindChallengeByIdRequest();
        findRequest.setChallengeId(-1);
        assertThrows(IllegalArgumentException.class, () -> challengeService.findChallengeBy(findRequest));
    }

    @Test
    public void testThatICanGetListOfChallengesSubmittedForATicket() {
        int challengeIdOne = challengeService.submitChallenge(submitRequest).getId();
        reviewRequest.setChallengeId(challengeIdOne);
        challengeService.rejectChallenge(reviewRequest);
        int challengeIdTwo = challengeService.submitChallenge(submitRequest).getId();
        FindAllChallengesForTicketRequest ticketChallengesRequest = new FindAllChallengesForTicketRequest();
        ticketChallengesRequest.setTicketId(ticketId);
        List<FindAllChallengesForTicketResponse> ticketChallenges = challengeService.findAllChallengesFor(ticketChallengesRequest);
        assertEquals(2, ticketChallenges.size());
        assertEquals(challengeIdOne, ticketChallenges.get(0).getId());
        assertEquals(challengeIdTwo, ticketChallenges.get(1).getId());
    }

    @Test
    public void testThatListOfChallengesForATicketDoesNotContainChallengesOfOtherTickets() {
        challengeService.submitChallenge(submitRequest);
        int otherTicketId = issueAnotherTicket("Ticket Number 2");
        submitRequest.setTicketId(otherTicketId);
        int otherChallengeId = challengeService.submitChallenge(submitRequest).getId();
        FindAllChallengesForTicketRequest ticketChallengesRequest = new FindAllChallengesForTicketRequest();
        ticketChallengesRequest.setTicketId(otherTicketId);
        List<FindAllChallengesForTicketResponse> ticketChallenges = challengeService.findAllChallengesFor(ticketChallengesRequest);
        assertEquals(1, ticketChallenges.size());
        assertEquals(otherChallengeId, ticketChallenges.get(0).getId());
    }

    @Test
    public void testThatWhenTicketHasNoChallenges_emptyListIsReturned() {
        FindAllChallengesForTicketRequest ticketChallengesRequest = new FindAllChallengesForTicketRequest();
        ticketChallengesRequest.setTicketId(ticketId);
        assertTrue(challengeService.findAllChallengesFor(ticketChallengesRequest).isEmpty());
    }

    @Test
    public void testThatWhenIFindChallengesForInvalidTicketId_errorIsThrown() {
        FindAllChallengesForTicketRequest ticketChallengesRequest = new FindAllChallengesForTicketRequest();
        ticketChallengesRequest.setTicketId(-1);
        assertThrows(IllegalArgumentException.class, () -> challengeService.findAllChallengesFor(ticketChallengesRequest));
    }
}
