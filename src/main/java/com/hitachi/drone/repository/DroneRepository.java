package com.hitachi.drone.repository;

import com.hitachi.drone.entity.DroneEntity;
import com.hitachi.drone.enums.StateEnum;
import com.hitachi.drone.model.response.DroneResponse;
import com.hitachi.drone.model.response.DroneWithMedicationResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<DroneEntity, Long> {
    public Optional<DroneEntity> findBySerialNumber(String serialNumber);

    @Modifying
    @Transactional
    @Query(
            "UPDATE DroneEntity d SET d.state = :nextState " +
                    "WHERE d.serialNumber = :serialNumber " +
                    "AND d.state = :currentState"
        )
    int updateState(
            @Param("currentState") StateEnum currentState,
            @Param("nextState") StateEnum nextState,
            @Param("serialNumber") String serialNumber
    );

    @Modifying
    @Transactional
    @Query(
            "UPDATE DroneEntity d " +
                    "SET d.batteryCapacity = :batteryCapacity, d.state = 'IDLE'" +
                    "WHERE d.serialNumber = :serialNumber " +
                    "AND d.state = :droneState"
    )
    int updateBatteryCapacity (
            @Param("batteryCapacity") Integer batteryCapacity,
            @Param("serialNumber") String serialNumber,
            @Param("droneState") StateEnum droneState
    );


    @Query(
            "SELECT d " +
            "FROM DroneEntity d " +
            "LEFT JOIN FETCH d.medicationEntityList m " +
            "WHERE d.serialNumber = :serialNumber"
    )
    Optional<DroneEntity> findDroneWithMedication(@Param("serialNumber") String serialNumber);

    List<DroneEntity> findAllByState(StateEnum state);
}
