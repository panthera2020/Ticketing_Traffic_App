package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;
import com.lastma.dtos.responses.OffenceServiceResponse;

import java.util.List;

public interface OffenceService {
    OffenceServiceResponse createOffenceWith(ViolationType violationType, String code);

    OffenceServiceResponse findBy(int offenceId);

    OffenceServiceResponse findBy(String code);

    void deactivate(int offenceId);

    List<OffenceServiceResponse> findAllOffences();
}
