package org.thevoids.oncologic.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(name = "identification", length = 50, nullable = false, unique = true)
    private String identification;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "status", length = 20)
    private String status = "active";

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Patient patient;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ClinicAssignment> clinicAssignments;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private List<Appointment> doctorAppointments;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "labTechnician")
    @JsonIgnore
    private List<Lab> labs;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserSpecialty> userSpecialties;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<AssignedRole> assignedRoles;
}