package com.lastma.services;

import com.lastma.dtos.requests.FindAllChallengesForTicketRequest;
import com.lastma.dtos.requests.FindChallengeByIdRequest;
import com.lastma.dtos.requests.ReviewChallengeRequest;
import com.lastma.dtos.requests.SubmitChallengeRequest;
import com.lastma.dtos.responses.FindAllChallengesForTicketResponse;
import com.lastma.dtos.responses.FindChallengeByIdResponse;
import com.lastma.dtos.responses.ReviewChallengeResponse;
import com.lastma.dtos.responses.SubmitChallengeResponse;

import java.util.List;

public interface ChallengeService {
    SubmitChallengeResponse submitChallenge(SubmitChallengeRequest request);

    ReviewChallengeResponse approveChallenge(ReviewChallengeRequest request);

    ReviewChallengeResponse rejectChallenge(ReviewChallengeRequest request);

    FindChallengeByIdResponse findChallengeBy(FindChallengeByIdRequest request);

    List<FindAllChallengesForTicketResponse> findAllChallengesFor(FindAllChallengesForTicketRequest request);
}
