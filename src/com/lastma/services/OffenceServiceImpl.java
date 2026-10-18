package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.OffenceRepository;
import com.lastma.data.repositories.OffenceRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class OffenceServiceImpl implements  OffenceService {
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();

    @Override
    public int createOffenceWith(ViolationType violationType, String code) {
        validate(violationType);
        validate(code);
        Offence offence = new Offence(violationType);
        offence.setCode(code);
        offence.activate();
        return offenceRepository.save(offence).getId();
    }

    @Override
    public Offence findBy(int offenceId) {
        return offenceRepository.findById(offenceId);
    }

    @Override
    public Offence findBy(String code) {
        if(offenceRepository.count() > 0){
            List<Offence> offences = offenceRepository.findAll();
            for(Offence offence : offences){
                if(offence.getCode().equals(code)){
                    return offence;
                }
            }
        }
        throw new IllegalArgumentException("Offence with code " + code + " not found");
    }

    @Override
    public void deactivate(int offenceId) {
        findBy(offenceId).deactivate();
    }

    @Override
    public List<Offence> findAllOffences() {
        return offenceRepository.findAll();
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
