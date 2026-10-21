package com.lastma.services;

import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.requests.FindOffenceByCodeRequest;
import com.lastma.dtos.requests.FindOffenceByIdRequest;
import com.lastma.dtos.responses.CreateOffenceResponse;
import com.lastma.dtos.responses.FindOffenceByCodeResponse;
import com.lastma.dtos.responses.FindOffenceByIdResponse;

import java.util.List;

public interface OffenceService {
    CreateOffenceResponse createOffenceWith(CreateOffenceRequest request);

    FindOffenceByIdResponse findBy(FindOffenceByIdRequest request);

    FindOffenceByCodeResponse findBy(FindOffenceByCodeRequest request);

    void deactivate(int offenceId);
}
