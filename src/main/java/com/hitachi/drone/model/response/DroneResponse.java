package com.hitachi.drone.model.response;

import com.hitachi.drone.enums.DroneEnum;
import com.hitachi.drone.enums.StateEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DroneResponse {
    private Long id;
    private String serialNumber;
    private String droneModel;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private StateEnum state;
}
