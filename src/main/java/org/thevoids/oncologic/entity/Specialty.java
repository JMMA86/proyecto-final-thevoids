package org.thevoids.oncologic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "Specialties")
@Getter
@Setter
@ToString
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialtyId;

    @Column(name = "specialty_name", length = 100, nullable = false, unique = true)
    private String specialtyName;

    @OneToMany(mappedBy = "specialty")
    @JsonIgnore
    private List<UserSpecialty> userSpecialties;

    // Getters and setters
}
