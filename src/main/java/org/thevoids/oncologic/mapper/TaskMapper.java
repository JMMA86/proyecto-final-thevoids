package org.thevoids.oncologic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.thevoids.oncologic.dto.entity.TaskDTO;
import org.thevoids.oncologic.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "appointment.appointmentId", target = "appointmentId")
    TaskDTO toTaskDTO(Task task);

    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    Task toTask(TaskDTO taskDTO);

    Iterable<TaskDTO> toTaskDTOs(Iterable<Task> tasks);
}
