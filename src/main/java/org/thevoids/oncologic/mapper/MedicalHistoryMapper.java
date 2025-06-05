package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.MedicalHistoryDTO;
import org.thevoids.oncologic.entity.MedicalHistory;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {

    MedicalHistoryMapper INSTANCE = Mappers.getMapper(MedicalHistoryMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    MedicalHistoryDTO toMedicalHistoryDTO(MedicalHistory medicalHistory);

    @Mapping(target = "patient", ignore = true)
    MedicalHistory toMedicalHistory(MedicalHistoryDTO medicalHistoryDTO);

    // Convert a list of MedicalHistory entities to a list of MedicalHistoryDTOs
    Iterable<MedicalHistoryDTO> toMedicalHistoryDTOs(Iterable<MedicalHistory> medicalHistories);
}