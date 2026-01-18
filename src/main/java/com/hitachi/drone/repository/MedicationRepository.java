package com.hitachi.drone.repository;

import com.hitachi.drone.entity.MedicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {
}
