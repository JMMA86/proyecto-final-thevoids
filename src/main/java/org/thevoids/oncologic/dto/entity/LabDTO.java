package org.thevoids.oncologic.dto.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabDTO {
    private Long labId;
    private Long patientId;
    private Long labTechnicianId;
    private String testType;
    private Date requestDate;
    private Date completionDate;
    private String result;
    private String attachment;
}