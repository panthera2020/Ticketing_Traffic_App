package com.lastma.services;

import com.lastma.data.models.Challenge;
import com.lastma.data.models.ChallengeStatus;
import com.lastma.data.models.Officer;
import com.lastma.data.models.Ticket;
import com.lastma.data.models.TicketStatus;
import com.lastma.data.repositories.ChallengeRepository;
import com.lastma.data.repositories.ChallengeRepositoryImpl;
import com.lastma.data.repositories.OfficerRepository;
import com.lastma.data.repositories.OfficerRepositoryImpl;
import com.lastma.data.repositories.TicketRepository;
import com.lastma.data.repositories.TicketRepositoryImpl;
import com.lastma.dtos.requests.FindAllChallengesForTicketRequest;
import com.lastma.dtos.requests.FindChallengeByIdRequest;
import com.lastma.dtos.requests.ReviewChallengeRequest;
import com.lastma.dtos.requests.SubmitChallengeRequest;
import com.lastma.dtos.responses.FindAllChallengesForTicketResponse;
import com.lastma.dtos.responses.FindChallengeByIdResponse;
import com.lastma.dtos.responses.ReviewChallengeResponse;
import com.lastma.dtos.responses.SubmitChallengeResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lastma.utils.ChallengeServiceMapper.*;

public class ChallengeServiceImpl implements ChallengeService {
    private ChallengeRepository challengeRepository = new ChallengeRepositoryImpl();
    private TicketRepository ticketRepository = new TicketRepositoryImpl();
    private OfficerRepository officerRepository = new OfficerRepositoryImpl();

    @Override
    public SubmitChallengeResponse submitChallenge(SubmitChallengeRequest request) {
        validateTicket(request.getTicketId());
        Ticket ticket = ticketRepository.findById(request.getTicketId());
        validateTicketIsUnpaid(ticket);
        validateNoPendingChallenge(ticket.getId());
        validateReason(request.getReason());

        Challenge newChallenge = map(request);

        Challenge challenge = challengeRepository.save(newChallenge);

        return mapSubmitted(challenge);
    }

    @Override
    public ReviewChallengeResponse approveChallenge(ReviewChallengeRequest request) {
        validateChallenge(request.getChallengeId());
        validateOfficer(request.getOfficerId());
        Challenge challenge = challengeRepository.findById(request.getChallengeId());
        validateChallengeIsPending(challenge);

        review(challenge, request, ChallengeStatus.APPROVED);
        Ticket ticket = ticketRepository.findById(challenge.getTicketId());
        ticket.setStatus(TicketStatus.WAIVED.toString());
        ticketRepository.save(ticket);

        return mapReviewed(challenge);
    }

    @Override
    public ReviewChallengeResponse rejectChallenge(ReviewChallengeRequest request) {
        validateChallenge(request.getChallengeId());
        validateOfficer(request.getOfficerId());
        Challenge challenge = challengeRepository.findById(request.getChallengeId());
        validateChallengeIsPending(challenge);
        validateDecisionNotes(request.getDecisionNotes());
        review(challenge, request, ChallengeStatus.REJECTED);

        return mapReviewed(challenge);
    }

    @Override
    public FindChallengeByIdResponse findChallengeBy(FindChallengeByIdRequest request) {
        validateChallenge(request.getChallengeId());
        Challenge foundChallenge = challengeRepository.findById(request.getChallengeId());

        return mapId(foundChallenge);
    }

    @Override
    public List<FindAllChallengesForTicketResponse> findAllChallengesFor(FindAllChallengesForTicketRequest request) {
        validateTicket(request.getTicketId());
        List<Challenge> submittedChallenges = findAllChallenges();

        return mapListOf(submittedChallenges, request.getTicketId());
    }

    private void review(Challenge challenge, ReviewChallengeRequest request, ChallengeStatus decision) {
        challenge.setStatus(decision.toString());
        challenge.setDecisionNotes(request.getDecisionNotes());
        challenge.setReviewedByOfficerId(request.getOfficerId());
        challenge.setRevisedAt(LocalDate.now());
        challengeRepository.save(challenge);
    }

    private List<Challenge> findAllChallenges() {
        List<Challenge> submittedChallenges = challengeRepository.findAll();
        if (submittedChallenges == null) return new ArrayList<>();
        return submittedChallenges;
    }

    private void validateTicket(int ticketId) {
        if (ticketRepository.findById(ticketId) == null) throw new IllegalArgumentException("Ticket with id " + ticketId + " not found");
    }

    private void validateTicketIsUnpaid(Ticket ticket) {
        if (!ticket.getStatus().equals(TicketStatus.UNPAID.toString())) {
            throw new IllegalArgumentException("Ticket with id " + ticket.getId() + " is " + ticket.getStatus() + " and cannot be challenged");
        }
    }

    private void validateNoPendingChallenge(int ticketId) {
        for (Challenge challenge : findAllChallenges()) {
            if (challenge.getTicketId() == ticketId && challenge.getStatus().equals(ChallengeStatus.PENDING.toString())) {
                throw new IllegalArgumentException("Ticket with id " + ticketId + " already has a pending challenge");
            }
        }
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Reason is required");
    }

    private void validateChallenge(int challengeId) {
        if (challengeRepository.findById(challengeId) == null) throw new IllegalArgumentException("Challenge with id " + challengeId + " not found");
    }

    private void validateChallengeIsPending(Challenge challenge) {
        if (!challenge.getStatus().equals(ChallengeStatus.PENDING.toString())) {
            throw new IllegalArgumentException("Challenge with id " + challenge.getId() + " has already been " + challenge.getStatus());
        }
    }

    private void validateOfficer(int officerId) {
        Officer foundOfficer = officerRepository.findById(officerId);
        if (foundOfficer == null) throw new IllegalArgumentException("Officer with id " + officerId + " not found");
        if (!foundOfficer.isActive()) throw new IllegalArgumentException("Officer with id " + officerId + " is not active");
    }

    private void validateDecisionNotes(String decisionNotes) {
        if (decisionNotes == null || decisionNotes.isBlank()) throw new IllegalArgumentException("Decision notes are required when rejecting a challenge");
    }
}
