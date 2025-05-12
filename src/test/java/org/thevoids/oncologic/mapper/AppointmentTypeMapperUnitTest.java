package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.entity.AppointmentTypeDTO;
import org.thevoids.oncologic.entity.AppointmentType;

class AppointmentTypeMapperUnitTest {

    private final AppointmentTypeMapper mapper = AppointmentTypeMapper.INSTANCE;

    @Test
    void toAppointmentTypeDTO_and_toAppointmentType() {
        AppointmentType type = new AppointmentType();
        type.setTypeId(1L);
        type.setTypeName("Consultation");
        type.setStandardDuration(30);

        AppointmentTypeDTO dto = mapper.toAppointmentTypeDTO(type);

        assertEquals(1L, dto.getTypeId());
        assertEquals("Consultation", dto.getTypeName());
        assertEquals(30, dto.getStandardDuration());

        // Reverse mapping
        AppointmentType mappedBack = mapper.toAppointmentType(dto);
        assertEquals(dto.getTypeId(), mappedBack.getTypeId());
        assertEquals(dto.getTypeName(), mappedBack.getTypeName());
        assertEquals(dto.getStandardDuration(), mappedBack.getStandardDuration());
    }
}
