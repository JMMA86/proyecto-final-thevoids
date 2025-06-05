package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.entity.Lab;

@Mapper(componentModel = "spring")
public interface LabMapper {

    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "labTechnician.userId", target = "labTechnicianId")
    LabDTO toLabDTO(Lab lab);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "labTechnician", ignore = true)
    Lab toLab(LabDTO labDTO);

    // Convert a list of Lab entities to a list of LabDTOs
    Iterable<LabDTO> toLabDTOs(Iterable<Lab> labs);
}