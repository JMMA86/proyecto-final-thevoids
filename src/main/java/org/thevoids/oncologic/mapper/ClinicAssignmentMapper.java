package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.ClinicAssignmentDTO;
import org.thevoids.oncologic.entity.ClinicAssignment;

@Mapper(componentModel = "spring")
public interface ClinicAssignmentMapper {

    ClinicAssignmentMapper INSTANCE = Mappers.getMapper(ClinicAssignmentMapper.class);

    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "user.userId", target = "userId")
    ClinicAssignmentDTO toClinicAssignmentDTO(ClinicAssignment clinicAssignment);

    @Mapping(source = "clinicId", target = "clinic.id")
    @Mapping(source = "userId", target = "user.userId")
    ClinicAssignment toClinicAssignment(ClinicAssignmentDTO clinicAssignmentDTO);

    Iterable<ClinicAssignmentDTO> toClinicAssignmentDTOs(Iterable<ClinicAssignment> assignments);
}
