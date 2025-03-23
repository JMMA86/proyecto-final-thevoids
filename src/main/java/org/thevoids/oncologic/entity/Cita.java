package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import java.util.Date;
import java.util.List;

@Entity

@Table(name = "Citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    @Column(name = "fecha_hora", nullable = false)
    private Date fechaHora;

    @Column(name = "estado", length = 200, nullable = false)
    private String estado = "pendiente";
    
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Usuario medico;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cita", nullable = false)
    private TipoCita tipoCita;

    @OneToMany(mappedBy = "cita")
    private List<Tarea> tareas;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "Asignacion_Consultorio_id", referencedColumnName = "id"),
        @JoinColumn(name = "Asignacion_Consultorio_Consultorios_id", referencedColumnName = "Consultorios_id"),
        @JoinColumn(name = "Asignacion_Consultorio_Usuarios_id_usuario", referencedColumnName = "Usuarios_id_usuario")
    })
    private AsignacionConsultorio asignacionConsultorio;

    // Getters and setters
}
