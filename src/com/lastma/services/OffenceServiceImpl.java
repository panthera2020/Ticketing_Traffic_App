package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.OffenceRepository;
import com.lastma.data.repositories.OffenceRepositoryImpl;
import com.lastma.dtos.responses.OffenceServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class OffenceServiceImpl implements  OffenceService {
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();

    @Override
    public OffenceServiceResponse createOffenceWith(ViolationType violationType, String code) {
        validate(violationType);
        validate(code);
        Offence offence = new Offence(violationType);
        offence.setCode(code);
        offence.setDescription();
        offence.setFirstOffenceFine();
        offence.setSubsequentOffenceFine();
        offence.setPenaltyPoints();
        offence.setAdditionalPenaltyDescription();
        offence.activate();
        Offence savedOffence = offenceRepository.save(offence);
        OffenceServiceResponse response = new OffenceServiceResponse();
        response.setId(savedOffence.getId());
        response.setCode(savedOffence.getCode());
        response.setDescription(savedOffence.getDescription());
        response.setActive(savedOffence.isActive());
        response.setViolationType(savedOffence.getViolationType());
        response.setAdditionalPenaltyDescription(savedOffence.getAdditionalPenaltyDescription());
        response.setFirstOffenceFine(savedOffence.getFirstOffenceFine());
        response.setSubsequentOffenceFine(savedOffence.getSubsequentOffenceFine());
        response.setPenaltyPoints(savedOffence.getPenaltyPoints());
        return response;
    }

    @Override
    public OffenceServiceResponse findBy(int offenceId) {
        Offence foundOffence = offenceRepository.findById(offenceId);
        OffenceServiceResponse response = new OffenceServiceResponse();
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

    @Override
    public OffenceServiceResponse findBy(String code) {
        if(offenceRepository.count() > 0){
            List<Offence> offences = offenceRepository.findAll();
            for(Offence offence : offences){
                if(offence.getCode().equals(code)){
                    OffenceServiceResponse response = new OffenceServiceResponse();
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
            }
        }
        throw new IllegalArgumentException("Offence with code " + code + " not found");
    }

    @Override
    public void deactivate(int offenceId) {
        offenceRepository.findById(offenceId).deactivate();
    }

    @Override
    public List<OffenceServiceResponse> findAllOffences() {
        List<Offence> allOffences = offenceRepository.findAll();
        List<OffenceServiceResponse> responseList = new ArrayList<>();
        for(Offence offence : allOffences){
            OffenceServiceResponse response = new OffenceServiceResponse();
            response.setId(offence.getId());
            response.setCode(offence.getCode());
            response.setDescription(offence.getDescription());
            response.setActive(offence.isActive());
            response.setViolationType(offence.getViolationType());
            response.setAdditionalPenaltyDescription(offence.getAdditionalPenaltyDescription());
            response.setFirstOffenceFine(offence.getFirstOffenceFine());
            response.setSubsequentOffenceFine(offence.getSubsequentOffenceFine());
            response.setPenaltyPoints(offence.getPenaltyPoints());
            responseList.add(response);
        }
        return responseList;
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
