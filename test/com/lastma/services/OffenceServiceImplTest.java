package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.OffenceRepository;
import com.lastma.data.repositories.OffenceRepositoryImpl;
import com.lastma.dtos.responses.OffenceServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OffenceServiceImplTest {
    private OffenceService offenceService;
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();
    private int offenceId;
    private OffenceServiceResponse createdOffence;

    @BeforeEach
    public void setup() {
        offenceService = new OffenceServiceImpl();
        offenceRepository.deleteAll();
        createdOffence  = offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004");
        offenceId = createdOffence.getId();
    }

    @Test
    public void testThatWhenOffenceIsCreated_countIsOne() {
        offenceRepository.deleteAll();
        assertEquals(0, offenceRepository.count());
        offenceService.createOffenceWith(ViolationType.ABANDONED_VEHICLE_ON_HIGHWAY, "001");
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
        assertNotEquals(0, offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004").getId());
    }

    @Test
    public void testThatDuplicateOffenceCannotBeCreated() {
        offenceRepository.deleteAll();
        offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004");
        assertThrows(IllegalArgumentException.class, () -> offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004"));
    }

    @Test
    public void testThatOffenceCanBeFoundById(){
        OffenceServiceResponse savedOffenceByService = offenceService.findBy(offenceId);
        Offence savedOffenceByRepo = offenceRepository.findById(offenceId);
        assertEquals(savedOffenceByService.getId(), savedOffenceByRepo.getId());
    }

    @Test
    public void testThatOffenceCanOffenceCannotCannotBeCreatedWithDuplicateCode(){
        assertThrows(IllegalArgumentException.class, () -> offenceService.createOffenceWith(ViolationType.TRAFFIC_CONTROL_VIOLATION, "004"));
    }

    @Test
    public void testThatOffenceCanBeFoundByCode(){
        OffenceServiceResponse foundById = offenceService.findBy(offenceId);
        OffenceServiceResponse foundByCode = offenceService.findBy("004");
        assertEquals(foundById.getId(), foundByCode.getId());
        assertEquals(foundById.getCode(), foundByCode.getCode());
    }

    @Test
    public void testThatWenISearchByInvalidCode_errorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> offenceService.findBy("005"));
    }

    @Test
    public void testThatWhenICreateOffence_offenceStatusIsActivated(){
        OffenceServiceResponse savedOffence = offenceService.findBy(offenceId);
        assertTrue(savedOffence.isActive());
    }

    @Test
    public void testThatWhenOffenceIsAddedOffenceCanBeDeactivatedWithId(){
        assertTrue(offenceService.findBy(offenceId).isActive());
        offenceService.deactivate(offenceId);
        assertFalse(offenceService.findBy(offenceId).isActive());
    }

    @Test
    public void testThatICanGetListOfAllCreatedOffences(){
        int offenceIdOne = offenceService.createOffenceWith(ViolationType.LINE_VIOLATION, "002").getId();
        int offenceIdTwo = offenceService.createOffenceWith(ViolationType.ROUNDABOUT_VIOLATION, "005").getId();
        int offenceIdThree = offenceService.createOffenceWith(ViolationType.SEATBELT_VIOLATION, "003").getId();
        int[] offences = {offenceId, offenceIdOne, offenceIdTwo, offenceIdThree};
        List<OffenceServiceResponse> allOffences = offenceService.findAllOffences();
        int count = 0;
        for (OffenceServiceResponse offence : allOffences) {
            assertEquals(offence.getId(), offences[count]);
            count++;
        }
    }
}