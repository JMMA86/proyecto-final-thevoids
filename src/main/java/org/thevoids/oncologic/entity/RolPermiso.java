package org.thevoids.oncologic.entity;
import jakarta.persistence.*;




@Entity

@Table(name = "Roles_Permisos")
public class RolPermiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_permiso", nullable = false)
    private Permiso permiso;

    // Getters and setters
}
