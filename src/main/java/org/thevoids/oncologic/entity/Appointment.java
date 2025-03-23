package org.thevoids.oncologic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.sql.ast.tree.update.Assignment;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Appointments")
@Getter
@Setter
@ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(name = "date_time", nullable = false)
    private Date dateTime;

    @Column(name = "status", length = 200, nullable = false)
    private String status = "pending";
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AppointmentType appointmentType;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name="clinic_assignment_id", nullable = false)
    private ClinicAssignment clinicAssignment;

    // Getters and setters
}
