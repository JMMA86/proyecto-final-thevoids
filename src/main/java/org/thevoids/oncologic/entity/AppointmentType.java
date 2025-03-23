package org.thevoids.oncologic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Appointment_Types")
@Getter
@Setter
@ToString
public class AppointmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(name = "type_name", length = 100, nullable = false, unique = true)
    private String typeName;

    @Column(name = "standard_duration", nullable = false)
    private Integer standardDuration;

    // Getters and setters
}
