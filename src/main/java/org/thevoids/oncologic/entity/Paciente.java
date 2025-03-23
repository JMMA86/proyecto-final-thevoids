package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity

@Table(name = "Pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "grupo_sanguineo", length = 10)
    private String grupoSanguineo;

    @Column(name = "alergias", length = 500)
    private String alergias;

    @Column(name = "historia_familiar", length = 500)
    private String historiaFamiliar;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    private List<HistorialMedico> historialesMedicos;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnore
    private List<Laboratorio> laboratorios;

    // Getters and setters
}
