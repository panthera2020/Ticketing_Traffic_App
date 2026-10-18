package com.lastma.services;

import com.lastma.data.models.Offence;
import com.lastma.data.models.ViolationType;
import com.lastma.data.repositories.OffenceRepository;
import com.lastma.data.repositories.OffenceRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OffenceServiceImplTest {
    private OffenceService offenceService;
    private OffenceRepository offenceRepository = new OffenceRepositoryImpl();
    private int offenceId;

    @BeforeEach
    public void setup() {
        offenceService = new OffenceServiceImpl();
        offenceRepository.deleteAll();
        offenceId = offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004");
    }

    @Test
    public void testThatWhenOffenceIsCreated_countIsOne() {
        offenceRepository.deleteAll();
        assertEquals(0, offenceRepository.count());
        offenceService.createOffenceWith(ViolationType.ABANDONED_VEHICLE_ON_HIGHWAY, "001");
        assertEquals(1, offenceRepository.count());
    }

    @Test
    public void testThatWhenICreateOffenceIdIsGenerated() {
        offenceRepository.deleteAll();
        assertNotEquals(0, offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004"));
    }

    @Test
    public void testThatDuplicateOffenceCannotBeCreated() {
        offenceRepository.deleteAll();
        offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004");
        assertThrows(IllegalArgumentException.class, () -> offenceService.createOffenceWith(ViolationType.BRT_LANE_VIOLATION, "004"));
    }

    @Test
    public void testThatOffenceCanBeFoundById(){
        Offence savedOffenceByService = offenceService.findBy(offenceId);
        Offence savedOffenceByRepo = offenceRepository.findById(offenceId);
        assertEquals(savedOffenceByService, savedOffenceByRepo);
    }

    @Test
    public void testThatOffenceCanOffenceCannotCannotBeCreatedWithDuplicateCode(){
        assertThrows(IllegalArgumentException.class, () -> offenceService.createOffenceWith(ViolationType.TRAFFIC_CONTROL_VIOLATION, "004"));
    }

    @Test
    public void testThatOffenceCanBeFoundByCode(){
        Offence savedOffenceByService = offenceService.findBy(offenceId);
        Offence savedOffenceByRepo = offenceService.findBy("004");
        assertEquals(savedOffenceByService, savedOffenceByRepo);
    }

    @Test
    public void testThatWenISearchByInvalidCode_errorIsThrown(){
        assertThrows(IllegalArgumentException.class, () -> offenceService.findBy("005"));
    }

    @Test
    public void testThatWhenICreateOffence_offenceStatusIsActivated(){
        Offence savedOffence = offenceService.findBy(offenceId);
        assertTrue(savedOffence.isActive());
    }

    @Test
    public void testThatWhenOffenceIsAddedOffenceCanBeDeactivatedWithId(){
        Offence savedOffence = offenceService.findBy(offenceId);
        offenceService.deactivate(offenceId);
        assertFalse(savedOffence.isActive());
    }

    @Test
    public void testThatICanGetListOfAllCreatedOffences(){
        int offenceIdOne = offenceService.createOffenceWith(ViolationType.LINE_VIOLATION, "002");
        int offenceIdTwo = offenceService.createOffenceWith(ViolationType.ROUNDABOUT_VIOLATION, "005");
        int offenceIdThree = offenceService.createOffenceWith(ViolationType.SEATBELT_VIOLATION, "003");
        int[] offences = {offenceId, offenceIdOne, offenceIdTwo, offenceIdThree};
        List<Offence> allOffences = offenceService.findAllOffences();
        int count = 0;
        for (Offence offence : allOffences) {
            assertEquals(offence.getId(), offences[count]);
            count++;
        }
    }
}