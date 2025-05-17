package com.medavie.smarthome.domain.impl;

import com.medavie.smarthome.domain.Appliance;
import com.medavie.smarthome.domain.enums.ApplianceType;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Light implementation - turned off by toggling switch
 */
@Component
public class Light implements Appliance {

    private static final Logger logger = LoggerFactory.getLogger(Light.class);

    @Getter
    private final String name = "Light";
    private boolean isOn;

    @Override
    public boolean isOn() {
        return isOn;
    }

    public void toggle() {
        isOn = !isOn;
        logger.info("{} turned {}", name, isOn ? "ON" : "OFF");
    }

    @Override
    public void turnOff() {
        if (isOn) {
            toggle();
        }
    }

    @Override
    public String getStatus() {
        return name + " is " + (isOn ? "ON" : "OFF");
    }

    @Override
    public ApplianceType getType() {
        return ApplianceType.LIGHT;
    }
}
