package com.hitachi.drone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "medication")
@Data
public class MedicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String imageBase64;

    @Column()
    private boolean isUnLoaded = false;

    @ManyToOne
    @JoinColumn(name = "drone_serial_number", referencedColumnName = "serialNumber")
    @JsonIgnore
    private DroneEntity drone;
}
