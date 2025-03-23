package org.thevoids.oncologic.entity;
import jakarta.persistence.*;




@Entity

@Table(name = "Roles_Asignados")
public class RolAsignado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Usuarios_id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "Roles_id_rol", nullable = false)
    private Rol rol;

    // Getters and setters
}
