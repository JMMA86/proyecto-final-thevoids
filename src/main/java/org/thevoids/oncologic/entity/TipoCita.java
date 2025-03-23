package org.thevoids.oncologic.entity;
import jakarta.persistence.*;




@Entity

@Table(name = "Tipos_Citas")
public class TipoCita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoCita;

    @Column(name = "nombre_tipo_cita", length = 100, nullable = false, unique = true)
    private String nombreTipoCita;

    @Column(name = "duracion_estandar", nullable = false)
    private Integer duracionEstandar;

    // Getters and setters
}
