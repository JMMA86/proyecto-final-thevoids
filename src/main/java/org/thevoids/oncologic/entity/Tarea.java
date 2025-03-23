package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import java.util.Date;

@Entity

@Table(name = "Tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "completado", nullable = false)
    private Boolean completado;

    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

    @Column(name = "fecha_final", nullable = false)
    private Date fechaFinal;

    @ManyToOne
    @JoinColumn(name = "cita", nullable = false)
    private Cita cita;

    // Getters and setters
}
