package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.thevoids.oncologic.dto.PatientDTO;
import org.thevoids.oncologic.entity.Patient;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "user.userId", target = "userId")
    PatientDTO toPatientDTO(Patient patient);

    @Mapping(target = "user.userId", source = "userId")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "labs", ignore = true)
    @Mapping(target = "medicalHistories", ignore = true)
    Patient toPatient(PatientDTO patientDTO);

    List<PatientDTO> toPatientDTOs(List<Patient> patients);
}