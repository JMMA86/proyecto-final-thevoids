package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.ScheduleDTO;
import org.thevoids.oncologic.entity.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(source = "user.userId", target = "userId")
    ScheduleDTO toScheduleDTO(Schedule schedule);

    @Mapping(source = "userId", target = "user.userId")
    Schedule toSchedule(ScheduleDTO scheduleDTO);

    // Convert a list of Schedule entities to a list of ScheduleDTOs
    Iterable<ScheduleDTO> toScheduleDTOs(Iterable<Schedule> schedules);
}