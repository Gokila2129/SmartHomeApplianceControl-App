package com.medavie.smarthome.domain;

import com.medavie.smarthome.domain.enums.ApplianceType;

/**
 * Appliance interface - common behavior for all appliances
 */
public interface Appliance {
    boolean isOn();
    void turnOff();
    String getStatus();
    ApplianceType getType();
}