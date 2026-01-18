package com.hitachi.drone;

import com.hitachi.drone.entity.DroneEntity;
import com.hitachi.drone.enums.DroneEnum;
import com.hitachi.drone.enums.StateEnum;
import com.hitachi.drone.exception.BadRequest;
import com.hitachi.drone.exception.NotFoundException;
import com.hitachi.drone.model.request.DroneRequestModel;
import com.hitachi.drone.model.response.ApiResponse;
import com.hitachi.drone.model.response.DroneResponse;
import com.hitachi.drone.repository.DroneRepository;
import com.hitachi.drone.repository.MedicationRepository;
import com.hitachi.drone.service.DroneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private Environment environment;

    @InjectMocks
    private DroneService droneService;

    @Test
    void createDrone_success() {
        DroneRequestModel request = new DroneRequestModel();
        request.setSerialNumber("DRONE-001");
        request.setBatteryCapacity(100);
        request.setWeightLimit(500);
        request.setState(StateEnum.IDLE);
        request.setDroneModel(DroneEnum.LIGHT_WEIGHT);

        when(droneRepository.findBySerialNumber("DRONE-001"))
                .thenReturn(Optional.empty());

        ApiResponse response = droneService.createDrone(request);

        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        verify(droneRepository).save(any(DroneEntity.class));
    }

    @Test
    void createDrone_duplicateSerial_shouldThrowBadRequest() {
        DroneRequestModel request = new DroneRequestModel();
        request.setSerialNumber("DRONE-001");

        when(droneRepository.findBySerialNumber("DRONE-001"))
                .thenReturn(Optional.of(new DroneEntity()));

        assertThrows(BadRequest.class,
                () -> droneService.createDrone(request));
    }

    @Test
    void findDroneBySerialNumber_notFound() {
        when(droneRepository.findBySerialNumber("DRONE-X"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> droneService.findDroneBySerialNumber("DRONE-X"));
    }

    @Test
    void getDroneAvailability_success() {
        DroneEntity drone = new DroneEntity();
        drone.setSerialNumber("DRONE-001");
        drone.setState(StateEnum.IDLE);

        when(droneRepository.findAllByState(StateEnum.IDLE))
                .thenReturn(List.of(drone));

        List<DroneResponse> result = droneService.getDroneAvailability();

        assertEquals(1, result.size());
    }

    @Test
    void getDroneAvailability_noDrone() {
        when(droneRepository.findAllByState(StateEnum.IDLE))
                .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class,
                () -> droneService.getDroneAvailability());
    }
}

