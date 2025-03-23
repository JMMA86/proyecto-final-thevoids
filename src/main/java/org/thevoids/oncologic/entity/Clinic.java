package org.thevoids.oncologic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "Clinics")
@Getter
@Setter
@ToString
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "address", length = 20, nullable = false)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "specialty", length = 20)
    private String specialty;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "clinic")
    @JsonIgnore
    private List<ClinicAssignment> clinicAssignments;

    // Getters and setters
}
