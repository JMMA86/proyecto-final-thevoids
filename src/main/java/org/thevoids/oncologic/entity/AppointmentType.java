package org.thevoids.oncologic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
