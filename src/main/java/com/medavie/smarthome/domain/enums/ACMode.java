package com.medavie.smarthome.domain.enums;

/**
 * AC modes enum - modes for air conditioner
 */
public enum ACMode {
    OFF(0),     // 0
    COOL(1),    // 1
    HEAT(2),    // 2
    FAN(3),     // 3
    AUTO(4);    // 4

    private final int value;

    ACMode(int value) {
        this.value = value;
    }

    /**
     * Get the numeric value of this mode
     */
    public int getValue() {
        return value;
    }

    /**
     * Get an ACMode from its numeric value
     */
    public static ACMode fromValue(int value) {
        for (ACMode mode : values()) {
            if (mode.value == value) {
                return mode;
            }
        }
        return OFF;
    }
}
