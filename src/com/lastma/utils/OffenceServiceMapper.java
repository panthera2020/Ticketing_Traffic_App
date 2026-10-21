package com.lastma.utils;

import com.lastma.data.models.Offence;
import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.responses.CreateOffenceResponse;
import com.lastma.dtos.responses.FindOffenceByCodeResponse;
import com.lastma.dtos.responses.FindOffenceByIdResponse;

public class OffenceServiceMapper {
    public static Offence map(CreateOffenceRequest request){
        Offence offence = new Offence(request.getViolationType());
        offence.setCode(request.getCode());
        offence.setDescription();
        offence.setFirstOffenceFine();
        offence.setSubsequentOffenceFine();
        offence.setPenaltyPoints();
        offence.setAdditionalPenaltyDescription();
        offence.activate();
        return offence;
    }

    public static CreateOffenceResponse mapCreated(Offence offence){
        CreateOffenceResponse response = new CreateOffenceResponse();
        response.setId(offence.getId());
        response.setCode(offence.getCode());
        response.setDescription(offence.getDescription());
        response.setActive(offence.isActive());
        response.setViolationType(offence.getViolationType());
        response.setAdditionalPenaltyDescription(offence.getAdditionalPenaltyDescription());
        response.setFirstOffenceFine(offence.getFirstOffenceFine());
        response.setSubsequentOffenceFine(offence.getSubsequentOffenceFine());
        response.setPenaltyPoints(offence.getPenaltyPoints());
        return response;
    }

    public static FindOffenceByIdResponse mapId(Offence foundOffence){
        FindOffenceByIdResponse response = new FindOffenceByIdResponse();
        response.setId(foundOffence.getId());
        response.setCode(foundOffence.getCode());
        response.setDescription(foundOffence.getDescription());
        response.setActive(foundOffence.isActive());
        response.setViolationType(foundOffence.getViolationType());
        response.setAdditionalPenaltyDescription(foundOffence.getAdditionalPenaltyDescription());
        response.setFirstOffenceFine(foundOffence.getFirstOffenceFine());
        response.setSubsequentOffenceFine(foundOffence.getSubsequentOffenceFine());
        response.setPenaltyPoints(foundOffence.getPenaltyPoints());
        return response;
    }

    public static FindOffenceByCodeResponse mapCode(Offence foundOffence){
        FindOffenceByCodeResponse response = new FindOffenceByCodeResponse();
        response.setId(foundOffence.getId());
        response.setCode(foundOffence.getCode());
        response.setDescription(foundOffence.getDescription());
        response.setActive(foundOffence.isActive());
        response.setViolationType(foundOffence.getViolationType());
        response.setAdditionalPenaltyDescription(foundOffence.getAdditionalPenaltyDescription());
        response.setFirstOffenceFine(foundOffence.getFirstOffenceFine());
        response.setSubsequentOffenceFine(foundOffence.getSubsequentOffenceFine());
        response.setPenaltyPoints(foundOffence.getPenaltyPoints());
        return response;
    }
}
