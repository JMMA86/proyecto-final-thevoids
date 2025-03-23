package org.thevoids.oncologic.entity;
import jakarta.persistence.*;




@Entity

@Table(name = "Personal_Especialidad")
public class PersonalEspecialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    // Getters and setters
}
