package com.hitachi.drone.entity;

import com.hitachi.drone.enums.StateEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "drone")
@Data
public class DroneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Column(nullable = false)
    private String droneModel;

    @Column(nullable = false)
    private Integer weightLimit;

    @Column(nullable = false)
    @Min(0)
    @Max(100)
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StateEnum state;

    @OneToMany(mappedBy = "drone", fetch = FetchType.LAZY)
    private List<MedicationEntity> medicationEntityList;
}
