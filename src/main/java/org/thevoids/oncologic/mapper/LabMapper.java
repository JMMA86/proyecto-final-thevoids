package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.LabDTO;
import org.thevoids.oncologic.entity.Lab;

@Mapper(componentModel = "spring")
public interface LabMapper {

    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "labTechnician.userId", target = "labTechnicianId")
    LabDTO toLabDTO(Lab lab);

    @Mapping(source = "patientId", target = "patient.patientId")
    @Mapping(source = "labTechnicianId", target = "labTechnician.userId")
    Lab toLab(LabDTO labDTO);

    // Convert a list of Lab entities to a list of LabDTOs
    Iterable<LabDTO> toLabDTOs(Iterable<Lab> labs);
}