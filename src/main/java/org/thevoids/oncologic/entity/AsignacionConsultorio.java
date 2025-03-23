package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import java.util.Date;

@Entity

@Table(name = "Asignacion_Consultorio")
public class AsignacionConsultorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hora_inicio", nullable = false)
    private Date horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private Date horaFin;

    @ManyToOne
    @JoinColumn(name = "Consultorios_id", nullable = false)
    private Consultorio consultorio;

    @ManyToOne
    @JoinColumn(name = "Usuarios_id_usuario", nullable = false)
    private Usuario usuario;

    // Getters and setters
}
