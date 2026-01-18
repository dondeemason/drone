package com.hitachi.drone.model.response;

        import com.hitachi.drone.entity.MedicationEntity;
        import com.hitachi.drone.enums.StateEnum;
        import lombok.Data;

        import java.util.List;

@Data
public class DroneWithMedicationResponse {
    private String serialNumber;
    private String droneModel;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private StateEnum state;
    private List<MedicationEntity> medicationList;
}
