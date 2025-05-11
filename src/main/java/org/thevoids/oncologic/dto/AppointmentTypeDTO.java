package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTypeDTO {
    private Long typeId;
    private String typeName;
    private Integer standardDuration;
}
