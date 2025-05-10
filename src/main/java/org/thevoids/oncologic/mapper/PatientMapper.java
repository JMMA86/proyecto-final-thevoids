package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.PatientDTO;
import org.thevoids.oncologic.entity.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    @Mapping(source = "user.userId", target = "userId")
    PatientDTO toPatientDTO(Patient patient);

    @Mapping(source = "userId", target = "user.userId")
    Patient toPatient(PatientDTO patientDTO);

    // Convert a list of Patient entities to a list of PatientDTOs
    Iterable<PatientDTO> toPatientDTOs(Iterable<Patient> patients);
}