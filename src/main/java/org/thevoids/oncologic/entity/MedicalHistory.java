package org.thevoids.oncologic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "Medical_History")
@Getter
@Setter
@ToString
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "diagnosis", length = 500)
    private String diagnosis;

    @Column(name = "treatment", length = 500)
    private String treatment;

    @Column(name = "medications", length = 500)
    private String medications;

    @Column(name = "record_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp recordDate;

    // Getters and setters
}
