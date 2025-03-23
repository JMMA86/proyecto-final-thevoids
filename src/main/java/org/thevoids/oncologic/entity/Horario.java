package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import java.util.Date;

@Entity

@Table(name = "Horarios")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorario;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "dia_semana")
    private Date diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private Date horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private Date horaFin;

    // Getters and setters
}
