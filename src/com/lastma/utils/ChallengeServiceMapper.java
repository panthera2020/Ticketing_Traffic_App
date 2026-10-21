package com.lastma.utils;

import com.lastma.data.models.Challenge;
import com.lastma.data.models.ChallengeStatus;
import com.lastma.dtos.requests.SubmitChallengeRequest;
import com.lastma.dtos.responses.FindAllChallengesForTicketResponse;
import com.lastma.dtos.responses.FindChallengeByIdResponse;
import com.lastma.dtos.responses.ReviewChallengeResponse;
import com.lastma.dtos.responses.SubmitChallengeResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChallengeServiceMapper {
    public static Challenge map(SubmitChallengeRequest request) {
        Challenge challenge = new Challenge();
        challenge.setTicketId(request.getTicketId());
        challenge.setReason(request.getReason());
        challenge.setStatus(ChallengeStatus.PENDING.toString());
        challenge.setSubmittedAt(LocalDate.now());
        return challenge;
    }

    public static SubmitChallengeResponse mapSubmitted(Challenge challenge) {
        SubmitChallengeResponse response = new SubmitChallengeResponse();
        response.setId(challenge.getId());
        response.setTicketId(challenge.getTicketId());
        response.setReason(challenge.getReason());
        response.setStatus(challenge.getStatus());
        response.setDecisionNotes(challenge.getDecisionNotes());
        response.setReviewedByOfficerId(challenge.getReviewedByOfficerId());
        response.setSubmittedAt(challenge.getSubmittedAt());
        response.setRevisedAt(challenge.getRevisedAt());
        return response;
    }

    public static ReviewChallengeResponse mapReviewed(Challenge challenge) {
        ReviewChallengeResponse response = new ReviewChallengeResponse();
        response.setId(challenge.getId());
        response.setTicketId(challenge.getTicketId());
        response.setReason(challenge.getReason());
        response.setStatus(challenge.getStatus());
        response.setDecisionNotes(challenge.getDecisionNotes());
        response.setReviewedByOfficerId(challenge.getReviewedByOfficerId());
        response.setSubmittedAt(challenge.getSubmittedAt());
        response.setRevisedAt(challenge.getRevisedAt());
        return response;
    }

    public static FindChallengeByIdResponse mapId(Challenge foundChallenge) {
        FindChallengeByIdResponse response = new FindChallengeByIdResponse();
        response.setId(foundChallenge.getId());
        response.setTicketId(foundChallenge.getTicketId());
        response.setReason(foundChallenge.getReason());
        response.setStatus(foundChallenge.getStatus());
        response.setDecisionNotes(foundChallenge.getDecisionNotes());
        response.setReviewedByOfficerId(foundChallenge.getReviewedByOfficerId());
        response.setSubmittedAt(foundChallenge.getSubmittedAt());
        response.setRevisedAt(foundChallenge.getRevisedAt());
        return response;
    }

    public static List<FindAllChallengesForTicketResponse> mapListOf(List<Challenge> submittedChallenges, int ticketId) {
        List<FindAllChallengesForTicketResponse> ticketChallenges = new ArrayList<>();
        for (Challenge challenge : submittedChallenges) {
            if (challenge.getTicketId() == ticketId) {
                FindAllChallengesForTicketResponse response = new FindAllChallengesForTicketResponse();
                response.setId(challenge.getId());
                response.setTicketId(challenge.getTicketId());
                response.setReason(challenge.getReason());
                response.setStatus(challenge.getStatus());
                response.setDecisionNotes(challenge.getDecisionNotes());
                response.setReviewedByOfficerId(challenge.getReviewedByOfficerId());
                response.setSubmittedAt(challenge.getSubmittedAt());
                response.setRevisedAt(challenge.getRevisedAt());
                ticketChallenges.add(response);
            }
        }
        return ticketChallenges;
    }
}
