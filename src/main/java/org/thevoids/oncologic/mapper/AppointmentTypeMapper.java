package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.AppointmentTypeDTO;
import org.thevoids.oncologic.entity.AppointmentType;

@Mapper(componentModel = "spring")
public interface AppointmentTypeMapper {

    AppointmentTypeMapper INSTANCE = Mappers.getMapper(AppointmentTypeMapper.class);

    AppointmentTypeDTO toAppointmentTypeDTO(AppointmentType appointmentType);

    AppointmentType toAppointmentType(AppointmentTypeDTO appointmentTypeDTO);

    Iterable<AppointmentTypeDTO> toAppointmentTypeDTOs(Iterable<AppointmentType> appointmentTypes);
}
