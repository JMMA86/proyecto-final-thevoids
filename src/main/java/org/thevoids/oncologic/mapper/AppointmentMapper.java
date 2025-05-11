package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "doctor.userId", target = "doctorId")
    @Mapping(source = "appointmentType.typeId", target = "appointmentTypeId")
    @Mapping(source = "clinicAssignment.id", target = "clinicAssignmentId")
    AppointmentDTO toAppointmentDTO(Appointment appointment);

    @Mapping(source = "patientId", target = "patient.patientId")
    @Mapping(source = "doctorId", target = "doctor.userId")
    @Mapping(source = "appointmentTypeId", target = "appointmentType.typeId")
    @Mapping(source = "clinicAssignmentId", target = "clinicAssignment.id")
    Appointment toAppointment(AppointmentDTO appointmentDTO);

    Iterable<AppointmentDTO> toAppointmentDTOs(Iterable<Appointment> appointments);
}
