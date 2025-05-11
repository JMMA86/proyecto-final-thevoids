package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.ClinicDTO;
import org.thevoids.oncologic.entity.Clinic;

class ClinicMapperUnitTest {

    private final ClinicMapper mapper = ClinicMapper.INSTANCE;

    @Test
    void toClinicDTO_and_toClinic() {
        Clinic clinic = new Clinic();
        clinic.setId(1L);
        clinic.setName("City Clinic");
        clinic.setAddress("123 Main St");
        clinic.setPhone("555-1234");
        clinic.setSpecialty("Oncology");
        clinic.setCapacity(50);

        ClinicDTO dto = mapper.toClinicDTO(clinic);

        assertEquals(1L, dto.getId());
        assertEquals("City Clinic", dto.getName());
        assertEquals("123 Main St", dto.getAddress());
        assertEquals("555-1234", dto.getPhone());
        assertEquals("Oncology", dto.getSpecialty());
        assertEquals(50, dto.getCapacity());

        // Reverse mapping
        Clinic mappedBack = mapper.toClinic(dto);
        assertEquals(dto.getId(), mappedBack.getId());
        assertEquals(dto.getName(), mappedBack.getName());
        assertEquals(dto.getAddress(), mappedBack.getAddress());
        assertEquals(dto.getPhone(), mappedBack.getPhone());
        assertEquals(dto.getSpecialty(), mappedBack.getSpecialty());
        assertEquals(dto.getCapacity(), mappedBack.getCapacity());
    }
}
