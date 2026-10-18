package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;

import java.util.List;

public interface OffenceService {
    int createOffenceWith(ViolationType violationType, String code);

    Offence findBy(int offenceId);

    Offence findBy(String code);

    void deactivate(int offenceId);

    List<Offence> findAllOffences();
}
