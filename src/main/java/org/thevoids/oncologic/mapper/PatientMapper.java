package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.entity.Patient;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "user.userId", target = "userId")
    PatientDTO toPatientDTO(Patient patient);

    // For creation only. For updates, always fetch the managed Patient entity in
    // the service layer.
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "labs", ignore = true)
    @Mapping(target = "medicalHistories", ignore = true)
    Patient toPatient(PatientDTO patientDTO);

    List<PatientDTO> toPatientDTOs(List<Patient> patients);
}