package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity

@Table(name = "Permisos")
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPermiso;

    @Column(name = "nombre_permiso", length = 50, nullable = false, unique = true)
    private String nombrePermiso;

    @OneToMany(mappedBy = "permiso")
    @JsonIgnore
    private List<RolPermiso> rolesPermisos;

    // Getters and setters
}
