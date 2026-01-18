package com.hitachi.drone.dummy;

import com.hitachi.drone.entity.DroneEntity;
import com.hitachi.drone.entity.MedicationEntity;
import com.hitachi.drone.enums.StateEnum;
import com.hitachi.drone.repository.DroneRepository;
import com.hitachi.drone.repository.MedicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DummyDataLoader {

    @Bean
    CommandLineRunner loadDummyData(
            DroneRepository droneRepository,
            MedicationRepository medicationRepository
    ) {
        return args -> {
            if (droneRepository.count() > 0) {
                return;
            }

            DroneEntity drone1 = new DroneEntity();
            drone1.setSerialNumber("DRONE-001");
            drone1.setDroneModel("LIGHT_WEIGHT");
            drone1.setWeightLimit(500);
            drone1.setBatteryCapacity(100);
            drone1.setState(StateEnum.LOADING);

            DroneEntity drone2 = new DroneEntity();
            drone2.setSerialNumber("DRONE-002");
            drone2.setDroneModel("MIDDLE_WEIGHT");
            drone2.setWeightLimit(300);
            drone2.setBatteryCapacity(80);
            drone2.setState(StateEnum.IDLE);

            droneRepository.saveAll(List.of(drone1, drone2));

            MedicationEntity med1 = new MedicationEntity();
            med1.setName("Paracetamol");
            med1.setCode("PARA_500");
            med1.setWeight(120);
            med1.setImageBase64("BASE64_IMAGE_1");
            med1.setDrone(drone1);

            MedicationEntity med2 = new MedicationEntity();
            med2.setName("Ibuprofen");
            med2.setCode("IBU_200");
            med2.setWeight(80);
            med2.setImageBase64("BASE64_IMAGE_2");
            med2.setDrone(drone1);

            medicationRepository.saveAll(List.of(med1, med2));
        };
    }
}