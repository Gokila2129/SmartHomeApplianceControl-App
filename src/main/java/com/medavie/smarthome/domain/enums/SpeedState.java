package com.medavie.smarthome.domain.enums;

/**
 * SpeedState enum for fan speeds with only three levels:
 * 0 = OFF, 1 = LOW, 2 = HIGH
 */
public enum SpeedState {
    OFF(0),    // 0
    LOW(1),    // 1
    HIGH(2);   // 2

    private final int value;

    SpeedState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}