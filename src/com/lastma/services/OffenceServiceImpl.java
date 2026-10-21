package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.OffenceRepository;
import com.lastma.data.repositories.OffenceRepositoryImpl;
import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.requests.FindOffenceByCodeRequest;
import com.lastma.dtos.requests.FindOffenceByIdRequest;
import com.lastma.dtos.responses.CreateOffenceResponse;
import com.lastma.dtos.responses.FindOffenceByCodeResponse;
import com.lastma.dtos.responses.FindOffenceByIdResponse;

import java.util.List;

import static com.lastma.utils.OffenceServiceMapper.*;

public class OffenceServiceImpl implements  OffenceService {
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();

    @Override
    public CreateOffenceResponse createOffenceWith(CreateOffenceRequest request) {
        validate(request.getViolationType());
        validate(request.getCode());
        Offence newOffence = map(request);
        Offence offence = offenceRepository.save(newOffence);

        return mapCreated(offence);
    }

    @Override
    public FindOffenceByIdResponse findBy(FindOffenceByIdRequest idRequest) {
        Offence foundOffence = offenceRepository.findById(idRequest.getOffenceId());

        return mapId(foundOffence);
    }

    @Override
    public FindOffenceByCodeResponse findBy(FindOffenceByCodeRequest codeRequest) {
        if(offenceRepository.count() > 0){
            List<Offence> offences = offenceRepository.findAll();
            for(Offence foundOffence : offences){
                if(foundOffence.getCode().equals(codeRequest.getCode())){
                    return mapCode(foundOffence);
                }
            }
        }
        throw new IllegalArgumentException("Offence with code " + codeRequest.getCode() + " not found");
    }

    @Override
    public void deactivate(int offenceId) {
        offenceRepository.findById(offenceId).deactivate();
    }

    private void validate(ViolationType violationType) {
        if(offenceRepository.count() > 0){
            List<Offence> offences = offenceRepository.findAll();
            for (Offence offence : offences) {
                if(offence.getViolationType().equals(violationType)) {
                    throw new IllegalArgumentException("Offence with code " + offence.getCode() + " already exists");
                }
            }
        }
    }
    private void validate(String code){
        if(offenceRepository.count() > 0){
            List<Offence> offences = offenceRepository.findAll();
            for (Offence offence : offences) {
                if(offence.getCode().equals(code)){
                    throw new IllegalArgumentException("Offence with code " + offence.getCode() + " already exists");
                }
            }
        }
    }
}
