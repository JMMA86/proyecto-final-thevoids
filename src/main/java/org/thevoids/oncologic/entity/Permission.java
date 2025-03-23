package org.thevoids.oncologic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "Permissions")
@Getter
@Setter
@ToString
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column(name = "permission_name", length = 50, nullable = false, unique = true)
    private String permissionName;

    @OneToMany(mappedBy = "permission")
    @JsonIgnore
    private List<RolePermission> rolePermissions;

    // Getters and setters
}
