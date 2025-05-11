package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private Date dateTime;
    private String status;
    private Long patientId;
    private Long doctorId;
    private Long appointmentTypeId;
    private Long clinicAssignmentId;
}
