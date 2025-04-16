package org.thevoids.oncologic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
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

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "active";

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Patient patient;

    @Column(name = "password", length = 60, nullable = false)
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ClinicAssignment> getClinicAssignments() {
        return clinicAssignments;
    }

    public void setClinicAssignments(List<ClinicAssignment> clinicAssignments) {
        this.clinicAssignments = clinicAssignments;
    }

    public List<Appointment> getDoctorAppointments() {
        return doctorAppointments;
    }

    public void setDoctorAppointments(List<Appointment> doctorAppointments) {
        this.doctorAppointments = doctorAppointments;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Lab> getLabs() {
        return labs;
    }

    public void setLabs(List<Lab> labs) {
        this.labs = labs;
    }

    public List<UserSpecialty> getUserSpecialties() {
        return userSpecialties;
    }

    public void setUserSpecialties(List<UserSpecialty> userSpecialties) {
        this.userSpecialties = userSpecialties;
    }

    public List<AssignedRole> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(List<AssignedRole> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }
}
