package com.hitachi.drone.enums;

import lombok.Getter;

public enum DroneEnum {
    LIGHT_WEIGHT("Lightweight", 1000),
    MIDDLE_WEIGHT("Middleweight", 1500),
    CRUISER_WEIGHT("Cruiserweight", 2000),
    HEAVY_WEIGHT("Heavyweight", 3000);

    @Getter
    private final String key;
    @Getter
    private final Integer value;

    DroneEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

}
