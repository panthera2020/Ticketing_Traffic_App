package com.lastma.services;

import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.requests.FindOffenceByCodeRequest;
import com.lastma.dtos.requests.FindOffenceByIdRequest;
import com.lastma.dtos.responses.CreateOffenceResponse;
import com.lastma.dtos.responses.FindOffenceByCodeResponse;
import com.lastma.dtos.responses.FindOffenceByIdResponse;

public interface OffenceService {
    CreateOffenceResponse createOffenceWith(CreateOffenceRequest request);

    FindOffenceByIdResponse findBy(FindOffenceByIdRequest idRequest);

    FindOffenceByCodeResponse findBy(FindOffenceByCodeRequest codeRequest);

    //Implement the deactive request response and add to controller
    void deactivate(int offenceId);
}
