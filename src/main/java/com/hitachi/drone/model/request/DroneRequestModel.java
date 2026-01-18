package com.hitachi.drone.model.request;

import com.hitachi.drone.enums.DroneEnum;
import com.hitachi.drone.enums.StateEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DroneRequestModel {
    @NotNull(message = "Serial Number is required")
    @Size(min = 1, max = 100)
    private String serialNumber;
    @NotNull(message = "Drone Model is required")
    private DroneEnum droneModel;
    @NotNull(message = "Weight Limit is required")
    private Integer weightLimit;

    @NotNull(message = "Batter Capacity is required")
    @Min(value = 1, message = "Battery must be > 0")
    @Max(value = 100, message = "Batter must be <= 100")
    private Integer batteryCapacity;

    @NotNull(message = "State is required")
    private StateEnum state;

    public void setDroneModel(DroneEnum droneModel) {
        this.droneModel = droneModel;

        if(droneModel != null)
            this.weightLimit = droneModel.getValue();
    }
}
