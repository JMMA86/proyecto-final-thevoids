package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.entity.TaskDTO;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.entity.Task;

class TaskMapperUnitTest {

    private final TaskMapper mapper = TaskMapper.INSTANCE;

    @Test
    void toTaskDTO_and_toTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Prepare patient report");
        task.setCompleted(true);
        task.setStartDate(new Date());
        task.setEndDate(new Date());

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(2L);
        task.setAppointment(appointment);

        TaskDTO dto = mapper.toTaskDTO(task);

        assertEquals(1L, dto.getId());
        assertEquals("Prepare patient report", dto.getDescription());
        assertTrue(dto.getCompleted());
        assertEquals(2L, dto.getAppointmentId());

        // Reverse mapping
        Task mappedBack = mapper.toTask(dto);
        assertEquals(dto.getId(), mappedBack.getId());
        assertEquals(dto.getDescription(), mappedBack.getDescription());
        assertEquals(dto.getCompleted(), mappedBack.getCompleted());
        assertEquals(dto.getAppointmentId(), mappedBack.getAppointment().getAppointmentId());
    }
}
