package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity

@Table(name = "Roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(name = "nombre_rol", length = 50, nullable = false, unique = true)
    private String nombreRol;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private List<RolAsignado> rolesAsignados;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private List<RolPermiso> rolesPermisos;

    // Getters and setters
}
