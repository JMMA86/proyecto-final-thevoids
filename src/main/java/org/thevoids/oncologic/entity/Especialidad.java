package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity

@Table(name = "Especialidades")
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEspecialidad;

    @Column(name = "nombre_especialidad", length = 100, nullable = false, unique = true)
    private String nombreEspecialidad;

    @OneToMany(mappedBy = "especialidad")
    @JsonIgnore
    private List<PersonalEspecialidad> personalEspecialidades;

    // Getters and setters
}
