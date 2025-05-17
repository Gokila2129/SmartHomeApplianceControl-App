package com.medavie.smarthome.domain.impl;

import com.medavie.smarthome.domain.Appliance;
import com.medavie.smarthome.domain.enums.ACMode;
import com.medavie.smarthome.domain.enums.ApplianceType;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Air Conditioner implementation - turned off by setting mode to OFF
 */
@Getter
@Component
public class AirConditioner implements Appliance {
    private static final Logger logger = LoggerFactory.getLogger(AirConditioner.class);

    private final String name = "Main AC";
    private ACMode mode = ACMode.OFF;

    @Override
    public boolean isOn() {
        return mode != ACMode.OFF;
    }

    /**
     * Set mode using ACMode enum
     */
    public void setMode(ACMode mode) {
        ACMode oldMode = this.mode;
        this.mode = mode;
        logger.info("{} mode changed from {} ({}) to {} ({})",
                name, oldMode, oldMode.getValue(), mode, mode.getValue());
    }

    /**
     * Set mode using numeric value
     */
    public void setMode(int modeValue) {
        setMode(ACMode.fromValue(modeValue));
    }

    /**
     * Set mode
     */
    public void setMode(String modeInput) {
        try {
            int modeValue = Integer.parseInt(modeInput);
            setMode(modeValue);
        } catch (NumberFormatException e) {
            try {
                ACMode newMode = ACMode.valueOf(modeInput.toUpperCase());
                setMode(newMode);
            } catch (IllegalArgumentException ex) {
                logger.error("Invalid AC mode: {}", modeInput);
            }
        }
    }



    /**
     * Turn off the AC by setting mode to OFF
     */
    @Override
    public void turnOff() {
        if (mode != ACMode.OFF) {
            logger.info("{} turning off by setting mode to OFF", name);
            setMode(ACMode.OFF);
        }
    }

    /**
     * Get status description
     */
    @Override
    public String getStatus() {
        if (isOn()) {
            return name + " is ON (Mode: " + mode + " - " + mode.getValue() + ")";
        } else {
            return name + " is OFF (Mode: " + mode + " - " + mode.getValue() + ")";
        }
    }

    /**
     * Get appliance type
     */
    @Override
    public ApplianceType getType() {
        return ApplianceType.AC;
    }
}