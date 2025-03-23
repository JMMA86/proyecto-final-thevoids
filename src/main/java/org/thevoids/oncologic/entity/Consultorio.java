package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity

@Table(name = "Consultorios")
public class Consultorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 20, nullable = false)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "especialidad", length = 20)
    private String especialidad;

    @Column(name = "capacidad")
    private Integer capacidad;

    @OneToMany(mappedBy = "consultorio")
    @JsonIgnore
    private List<AsignacionConsultorio> asignacionesConsultorio;

    // Getters and setters
}
