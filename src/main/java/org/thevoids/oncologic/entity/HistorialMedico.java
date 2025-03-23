package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import java.sql.Timestamp;

@Entity

@Table(name = "Historial_Medico")
public class HistorialMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @Column(name = "diagnostico", length = 500)
    private String diagnostico;

    @Column(name = "tratamiento", length = 500)
    private String tratamiento;

    @Column(name = "medicamentos", length = 500)
    private String medicamentos;

    @Column(name = "fecha_registro", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fechaRegistro;

    // Getters and setters
}
