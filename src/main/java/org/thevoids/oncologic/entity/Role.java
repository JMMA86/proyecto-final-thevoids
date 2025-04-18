package org.thevoids.oncologic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Roles")
@Getter
@Setter
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "role_name", length = 50, nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<AssignedRole> assignedRoles;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<RolePermission> rolePermissions;
}