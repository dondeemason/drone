package com.hitachi.drone.enums;

public enum StateEnum {
    IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING;

    public StateEnum nextState() {
        switch (this) {
            case IDLE:
                return LOADING;
            case LOADING:
                return LOADED;
            case LOADED:
                return DELIVERING;
            case DELIVERING:
                return DELIVERED;
            case DELIVERED:
                return RETURNING;
            default:
                return IDLE;
        }
    }
}
