package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import java.util.Date;

@Entity

@Table(name = "Laboratorios")
public class Laboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExamen;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_laboratorista", nullable = false)
    private Usuario laboratorista;

    @Column(name = "tipo_examen", length = 100)
    private String tipoExamen;

    @Column(name = "fecha_solicitud", nullable = false)
    private Date fechaSolicitud;

    @Column(name = "fecha_realizacion")
    private Date fechaRealizacion;

    @Column(name = "resultado", length = 300)
    private String resultado;

    @Column(name = "archivo_adjunto", length = 255)
    private String archivoAdjunto;

    // Getters and setters
}
