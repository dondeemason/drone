package com.hitachi.drone.service;

import com.hitachi.drone.entity.DroneEntity;
import com.hitachi.drone.entity.MedicationEntity;
import com.hitachi.drone.enums.StateEnum;
import com.hitachi.drone.exception.BadRequest;
import com.hitachi.drone.exception.InsufficientBatteryException;
import com.hitachi.drone.exception.NoLoadedMedicationException;
import com.hitachi.drone.exception.NotFoundException;
import com.hitachi.drone.model.request.DroneRequestModel;
import com.hitachi.drone.model.response.ApiResponse;
import com.hitachi.drone.model.response.DroneResponse;
import com.hitachi.drone.model.response.DroneWithMedicationResponse;
import com.hitachi.drone.repository.DroneRepository;
import com.hitachi.drone.repository.MedicationRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private Environment env;
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    public DroneService(Environment env, DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.env = env;
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    public ApiResponse createDrone(DroneRequestModel droneRequestModel) {
        droneRepository.findBySerialNumber(droneRequestModel.getSerialNumber()).ifPresent(d -> {
            throw new BadRequest("Request Drone serial number already created");
        });
        DroneEntity droneEntity = new DroneEntity();
        droneEntity.setDroneModel(droneRequestModel.getDroneModel().getKey());
        droneEntity.setState(droneRequestModel.getState());
        droneEntity.setBatteryCapacity(droneRequestModel.getBatteryCapacity());
        droneEntity.setSerialNumber(droneRequestModel.getSerialNumber());
        droneEntity.setWeightLimit(droneRequestModel.getWeightLimit());

        try {
            droneRepository.save(droneEntity);
        } catch (Exception e) {
            return new ApiResponse("Error occurs while creating drone...", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ApiResponse("Create Drone Successfully!", HttpStatus.CREATED);
    }

    public DroneEntity findDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException("Find Drone By SerialNumber", "Serial Number", serialNumber));
    }

    public List<DroneResponse> getDroneAvailability() {
        List<DroneEntity> droneEntity = droneRepository.findAllByState(StateEnum.IDLE);
        if (droneEntity.isEmpty()) throw new NotFoundException("No available Drone");
        return droneEntity.stream().map(drone -> mapToDroneResponse(drone)).collect(Collectors.toList());
    }

    public DroneWithMedicationResponse findDroneWithMedication(String serialNumber) {
        DroneWithMedicationResponse response = new DroneWithMedicationResponse();
        DroneEntity droneEntity = droneRepository.findDroneWithMedication(serialNumber)
                .orElseThrow(() -> new NotFoundException("Find drone with medication", "serial number", serialNumber));

        List<MedicationEntity> loadedMedications = droneEntity.getMedicationEntityList()
                .stream()
                .filter(m -> !m.isUnLoaded())
                .toList();

        response.setSerialNumber(droneEntity.getSerialNumber());
        response.setDroneModel(droneEntity.getDroneModel());
        response.setWeightLimit(droneEntity.getWeightLimit());
        response.setBatteryCapacity(droneEntity.getBatteryCapacity());
        response.setState(droneEntity.getState());
        response.setMedicationList(loadedMedications);

        return response;
    }

    @Transactional
    public ApiResponse moveDroneState(String serialNumber) {
        DroneEntity droneEntity = droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException("Move Drone State", "Serial number", serialNumber));
        checkBattery(droneEntity.getBatteryCapacity());

        StateEnum currentState = droneEntity.getState();
        StateEnum nextState = currentState.nextState();

        if(nextState.equals(StateEnum.LOADED)) checkLoadItems(droneEntity.getMedicationEntityList());

        int update = droneRepository.updateState(currentState, nextState, serialNumber);
        if(update == 0) {
            return new ApiResponse("An error occurs while transitioning state", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(nextState.equals(StateEnum.RETURNING)) {
            updateBattery(droneEntity.getBatteryCapacity(), droneEntity.getSerialNumber());
            unload(droneEntity.getMedicationEntityList());
        }

        return new ApiResponse("Drone " + droneEntity.getSerialNumber() + " is current in state " + nextState, HttpStatus.OK);
    }

    @Transactional
    private void updateBattery(Integer battery, String serialNumber) {
        Integer batteryLeft = battery - Integer.parseInt(env.getProperty("drone.battery-consume"));
        int update = droneRepository.updateBatteryCapacity(batteryLeft, serialNumber, StateEnum.RETURNING);

        if(update==0) new ApiResponse("An error occur while updating drone battery", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Transactional
    private void unload(List<MedicationEntity> medicationEntityList) {
        for(MedicationEntity me : medicationEntityList) {
            MedicationEntity medicationEntity = medicationRepository.findById(me.getId()).orElse(null);
            if(null != medicationEntity) {
                medicationEntity.setUnLoaded(true);
                medicationRepository.save(medicationEntity);
            }
        }

    }

    private void checkLoadItems(List<MedicationEntity> medicationEntityList) {
        if(medicationEntityList.isEmpty())
            throw new NoLoadedMedicationException("The drone has no load medication");
    }

    private void checkBattery(int battery) {
        if(battery <= 25)
            throw new InsufficientBatteryException("Battery is insufficient");
    }

    private DroneResponse mapToDroneResponse(DroneEntity droneEntity) {
        DroneResponse droneResponse = new DroneResponse();
        droneResponse.setId(droneEntity.getId());
        droneResponse.setDroneModel(droneEntity.getDroneModel());
        droneResponse.setSerialNumber(droneEntity.getSerialNumber());
        droneResponse.setWeightLimit(droneEntity.getWeightLimit());
        droneResponse.setBatteryCapacity(droneEntity.getBatteryCapacity());
        droneResponse.setState(droneEntity.getState());
        return droneResponse;
    }
}
