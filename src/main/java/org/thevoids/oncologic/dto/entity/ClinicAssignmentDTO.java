package org.thevoids.oncologic.dto.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicAssignmentDTO {
    private Long id;
    private Date startTime;
    private Date endTime;
    private Long clinicId;
    private Long userId;
}
