package org.thevoids.oncologic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "Labs")
@Getter
@Setter
@ToString
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "lab_technician_id", nullable = false)
    private User labTechnician;

    @Column(name = "test_type", length = 100)
    private String testType;

    @Column(name = "request_date", nullable = false)
    private Date requestDate;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "result", length = 300)
    private String result;

    @Column(name = "attachment", length = 255)
    private String attachment;

    // Getters and setters
}
