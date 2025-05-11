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
public class TaskDTO {
    private Long id;
    private String description;
    private Boolean completed;
    private Date startDate;
    private Date endDate;
    private Long appointmentId;
}
