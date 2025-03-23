package org.thevoids.oncologic.entity;
import jakarta.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

@Entity

@Table(name = "Usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(name = "nombre_completo", length = 255, nullable = false)
    private String nombreCompleto;

    @Column(name = "identificacion", length = 50, nullable = false, unique = true)
    private String identificacion;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo_electronico", length = 255)
    private String correoElectronico;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado = "activo";

    @OneToOne(mappedBy = "usuario")
    private Paciente paciente;

    @Column(name = "contraseña", length = 20, nullable = false)
    private String contraseña;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<AsignacionConsultorio> asignacionesConsultorio;

    @OneToMany(mappedBy = "medico")
    @JsonIgnore
    private List<Cita> citasMedico;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Horario> horarios;

    @OneToMany(mappedBy = "laboratorista")
    @JsonIgnore
    private List<Laboratorio> laboratorios;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<PersonalEspecialidad> personalEspecialidades;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<RolAsignado> rolesAsignados;

    // Getters and setters
}
