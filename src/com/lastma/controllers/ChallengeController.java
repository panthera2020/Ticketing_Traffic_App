package com.lastma.controllers;

import com.lastma.dtos.requests.*;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.services.ChallengeService;
import com.lastma.services.ChallengeServiceImpl;

public class ChallengeController {
    private ChallengeService challengeService =  new ChallengeServiceImpl();

    public ApiResponse submitChallenge(SubmitChallengeRequest request){
        try {
            return new ApiResponse(true, challengeService.submitChallenge(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse approveChallenge(ReviewChallengeRequest request){
        try {
            return new ApiResponse(true, challengeService.approveChallenge(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse rejectChallenge(ReviewChallengeRequest request){
        try {
            return new ApiResponse(true, challengeService.rejectChallenge(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findChallengeBy(FindChallengeByIdRequest request){
        try {
            return new ApiResponse(true, challengeService.findChallengeBy(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

    public ApiResponse findAllChallengesFor(FindAllChallengesForTicketRequest request){
        try {
            return new ApiResponse(true, challengeService.findAllChallengesFor(request));
        }
        catch(Exception e){
            return new ApiResponse(false, e.getMessage());
        }
    }

}
