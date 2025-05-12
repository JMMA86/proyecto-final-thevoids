package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.entity.ClinicAssignmentDTO;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.User;

class ClinicAssignmentMapperUnitTest {

    private final ClinicAssignmentMapper mapper = ClinicAssignmentMapper.INSTANCE;

    @Test
    void toClinicAssignmentDTO_and_toClinicAssignment() {
        ClinicAssignment assignment = new ClinicAssignment();
        assignment.setId(1L);
        assignment.setStartTime(new Date());
        assignment.setEndTime(new Date());

        Clinic clinic = new Clinic();
        clinic.setId(2L);
        assignment.setClinic(clinic);

        User user = new User();
        user.setUserId(3L);
        assignment.setUser(user);

        ClinicAssignmentDTO dto = mapper.toClinicAssignmentDTO(assignment);

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getClinicId());
        assertEquals(3L, dto.getUserId());

        // Reverse mapping
        ClinicAssignment mappedBack = mapper.toClinicAssignment(dto);
        assertEquals(dto.getId(), mappedBack.getId());
        assertEquals(dto.getClinicId(), mappedBack.getClinic().getId());
        assertEquals(dto.getUserId(), mappedBack.getUser().getUserId());
    }
}
