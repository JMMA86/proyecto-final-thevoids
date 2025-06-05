package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "doctor.userId", target = "doctorId")
    @Mapping(source = "appointmentType.typeId", target = "appointmentTypeId")
    @Mapping(source = "clinicAssignment.id", target = "clinicAssignmentId")
    AppointmentDTO toAppointmentDTO(Appointment appointment);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "appointmentType", ignore = true)
    @Mapping(target = "clinicAssignment", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Appointment toAppointment(AppointmentDTO appointmentDTO);

    Iterable<AppointmentDTO> toAppointmentDTOs(Iterable<Appointment> appointments);
}
