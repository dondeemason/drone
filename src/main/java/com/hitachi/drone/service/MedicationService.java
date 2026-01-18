package com.hitachi.drone.service;

import com.hitachi.drone.entity.DroneEntity;
import com.hitachi.drone.entity.MedicationEntity;
import com.hitachi.drone.enums.StateEnum;
import com.hitachi.drone.exception.BadRequest;
import com.hitachi.drone.exception.NotFoundException;
import com.hitachi.drone.model.request.MedicationRequestModel;
import com.hitachi.drone.model.response.ApiResponse;
import com.hitachi.drone.model.response.DroneResponse;
import com.hitachi.drone.repository.MedicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final DroneService droneService;

    public MedicationService(MedicationRepository medicationRepository, DroneService droneService) {
        this.medicationRepository = medicationRepository;
        this.droneService = droneService;
    }

    public ApiResponse addMedication(MedicationRequestModel medicationRequestModel) {
        try {
            DroneEntity droneEntity = droneService.findDroneBySerialNumber(
                    medicationRequestModel.getDroneSerialNumber());

            MedicationEntity medicationEntity = new MedicationEntity();
            medicationEntity.setName(medicationRequestModel.getName());
            medicationEntity.setCode(medicationRequestModel.getCode());
            medicationEntity.setWeight(medicationRequestModel.getWeight());
            medicationEntity.setImageBase64(medicationRequestModel.getImage());
            medicationEntity.setDrone(droneEntity);

            validateDrone(droneEntity, medicationEntity.getWeight());
            medicationRepository.save(medicationEntity);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ApiResponse("Successfully added medication", HttpStatus.OK);
    }

    private void validateDrone(DroneEntity droneEntity, double currentMedicationWeight) {
        if(droneEntity == null)
            throw new BadRequest("Drone Entity is null");
        if(droneEntity.getBatteryCapacity() < 25)
            throw new BadRequest("Insufficient Battery");
        if(!droneEntity.getState().equals(StateEnum.LOADING))
            throw new BadRequest("Drone state must in Loading");
        if((getLoadedWeight(droneEntity) + currentMedicationWeight) > droneEntity.getWeightLimit())
            throw new BadRequest("Medication weight has exceed the limit");
    }

    private double getLoadedWeight(DroneEntity droneEntity) {
        double weight = droneEntity.getMedicationEntityList().stream()
                .mapToDouble(medication -> medication.getWeight())
                .sum();
        return weight;
    }
}
