package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.ClinicDTO;
import org.thevoids.oncologic.entity.Clinic;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    ClinicMapper INSTANCE = Mappers.getMapper(ClinicMapper.class);

    ClinicDTO toClinicDTO(Clinic clinic);

    Clinic toClinic(ClinicDTO clinicDTO);

    Iterable<ClinicDTO> toClinicDTOs(Iterable<Clinic> clinics);
}
