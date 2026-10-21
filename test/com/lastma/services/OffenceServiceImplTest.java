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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OffenceServiceImplTest {
    private OffenceService offenceService;
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();
    private int offenceId;
    private CreateOffenceResponse createdOffence;
    private CreateOffenceRequest  createRequest;
    private FindOffenceByIdRequest findByIdRequest;
    private FindOffenceByCodeRequest findByCodeRequest;

    @BeforeEach
    public void setup() {
        offenceService = new OffenceServiceImpl();
        offenceRepository.deleteAll();
        createRequest = new CreateOffenceRequest();
        findByIdRequest = new FindOffenceByIdRequest();
        findByCodeRequest = new FindOffenceByCodeRequest();
        createRequest.setViolationType(ViolationType.BRT_LANE_VIOLATION);
        createRequest.setCode("004");
        createdOffence  = offenceService.createOffenceWith(createRequest);
        offenceId = createdOffence.getId();
        findByIdRequest.setOffenceId(offenceId);
    }

    @Test
    public void testThatWhenOffenceIsCreated_countIsOne() {
        offenceRepository.deleteAll();
        assertEquals(0, offenceRepository.count());
        offenceService.createOffenceWith(createRequest);
        assertEquals(1, offenceRepository.count());
    }

    @Test
    public void testThatWhenOffenceIsCreated_codeIsSet() {
        Offence foundOffenceInRepo = offenceRepository.findById(offenceId);
        assertEquals(foundOffenceInRepo.getCode(), createdOffence.getCode());
    }

    @Test
    public void testThatWhenOffenceIsCreated_descriptionIsSet() {
        Offence foundOffenceInRepo = offenceRepository.findById(offenceId);
        assertEquals(foundOffenceInRepo.getDescription(), createdOffence.getDescription());
    }

    @Test
    public void testThatWhenOffenceIsCreated_firstOffenceFineIsSet() {
        Offence foundOffenceInRepo = offenceRepository.findById(offenceId);
        assertEquals(foundOffenceInRepo.getFirstOffenceFine(), createdOffence.getFirstOffenceFine());
    }

    @Test
    public void testThatWhenOffenceIsCreated_subsequentOffenceFineIsSet() {
        Offence foundOffenceInRepo = offenceRepository.findById(offenceId);
        assertEquals(foundOffenceInRepo.getSubsequentOffenceFine(), createdOffence.getSubsequentOffenceFine());
    }

    @Test
    public void testThatWhenOffenceIsCreated_penaltyPointsIsSet() {
        Offence foundOffenceInRepo = offenceRepository.findById(offenceId);
        assertEquals(foundOffenceInRepo.getPenaltyPoints(), createdOffence.getPenaltyPoints());
    }

    @Test
    public void testThatWhenOffenceIsCreated_additionalDescriptionIsSet() {
        Offence foundOffenceInRepo = offenceRepository.findById(offenceId);
        assertEquals(foundOffenceInRepo.getAdditionalPenaltyDescription(), createdOffence.getAdditionalPenaltyDescription());
    }

    @Test
    public void testThatWhenICreateOffenceIdIsGenerated() {
        offenceRepository.deleteAll();
        assertNotEquals(0, offenceService.createOffenceWith(createRequest).getId());
    }

    @Test
    public void testThatDuplicateOffenceCannotBeCreated() {
        offenceRepository.deleteAll();
        offenceService.createOffenceWith(createRequest);
        assertThrows(IllegalArgumentException.class, () -> offenceService.createOffenceWith(createRequest));
    }

    @Test
    public void testThatOffenceCanBeFoundById(){
        FindOffenceByIdResponse savedOffenceByService = offenceService.findBy(findByIdRequest);
        Offence savedOffenceByRepo = offenceRepository.findById(offenceId);
        assertEquals(savedOffenceByService.getId(), savedOffenceByRepo.getId());
    }

    @Test
    public void testThatOffenceCanOffenceCannotCannotBeCreatedWithDuplicateCode(){
        createRequest.setViolationType(ViolationType.TRAFFIC_CONTROL_VIOLATION);
        createRequest.setCode("004");
        assertThrows(IllegalArgumentException.class, () -> offenceService.createOffenceWith(createRequest));
    }

    @Test
    public void testThatOffenceCanBeFoundByCode(){
        FindOffenceByIdResponse foundById = offenceService.findBy(findByIdRequest);
        findByCodeRequest.setCode(foundById.getCode());
        FindOffenceByCodeResponse foundByCode = offenceService.findBy(findByCodeRequest);
        assertEquals(foundById.getId(), foundByCode.getId());
        assertEquals(foundById.getCode(), foundByCode.getCode());
    }

    @Test
    public void testThatWenISearchByInvalidCode_errorIsThrown(){
        findByCodeRequest.setCode("005");
        assertThrows(IllegalArgumentException.class, () -> offenceService.findBy(findByCodeRequest));
    }

    @Test
    public void testThatWhenICreateOffence_offenceStatusIsActivated(){
        FindOffenceByIdResponse savedOffence = offenceService.findBy(findByIdRequest);
        assertTrue(savedOffence.isActive());
    }

    @Test
    public void testThatWhenOffenceIsAddedOffenceCanBeDeactivatedWithId(){
        assertTrue(offenceService.findBy(findByIdRequest).isActive());
        offenceService.deactivate(offenceId);
        assertFalse(offenceService.findBy(findByIdRequest).isActive());
    }
}