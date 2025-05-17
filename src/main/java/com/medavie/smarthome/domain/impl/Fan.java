package com.medavie.smarthome.domain.impl;

import com.medavie.smarthome.domain.Appliance;
import com.medavie.smarthome.domain.enums.ApplianceType;
import com.medavie.smarthome.domain.enums.SpeedState;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Fan implementation - turned off by reducing speed to zero
 */
@Getter
@Component
public class Fan implements Appliance {
    private static final Logger logger = LoggerFactory.getLogger(Fan.class);

    private final String name = "Fan";
    private SpeedState level;
    private final boolean isPowerOn;

    public Fan() {
        this.level = SpeedState.OFF;
        this.isPowerOn = true;
    }

    /**
     * Check if fan is on - fan is on when speed is not OFF
     */
    @Override
    public boolean isOn() {
        return level != SpeedState.OFF && isPowerOn;
    }

    /**
     * Increase speed to next level
     * Cycles through: OFF → LOW → HIGH
     */
    public void increaseSpeed() {
        if (!isPowerOn) {
            logger.info("Cannot increase speed - {} is powered off", name);
            return;
        }

        SpeedState oldLevel = level;


        if (level == SpeedState.OFF) {
            level = SpeedState.LOW;
        }
        else if (level == SpeedState.LOW) {
            level = SpeedState.HIGH;
        }
        else {
            logger.info("{} already at maximum speed", name);
            return;
        }

        logger.info("{} speed increased from {} to {} (value: {})",
                name, oldLevel, level, level.getValue());
    }

    /**
     * Decrease speed by one level
     * Cycles through: HIGH → LOW → OFF (turns off)
     */
    public void decreaseSpeed() {
        if (!isPowerOn) {
            logger.info("Cannot decrease speed - {} is powered off", name);
            return;
        }

        if (level == SpeedState.OFF) {
            logger.info("{} already off", name);
            return;
        }

        SpeedState oldLevel = level;

        if (level == SpeedState.HIGH) {
            level = SpeedState.LOW;
            logger.info("{} speed decreased from {} to {} (value: {})",
                    name, oldLevel, level, level.getValue());
        }

        else if (level == SpeedState.LOW) {
            level = SpeedState.OFF;
            logger.info("{} turned off by reducing speed from LOW to OFF", name);
        }
    }

    /**
     * Turn off the fan by reducing speed to zero
     */
    @Override
    public void turnOff() {
        if (level == SpeedState.OFF) {
            return;
        }

        logger.info("{} turning off by reducing speed to zero", name);

        if (level == SpeedState.HIGH) {
            SpeedState oldLevel = level;
            level = SpeedState.LOW;
            logger.info("{} speed reduced from {} to {} (value: {})",
                    name, oldLevel, level, level.getValue());
        }

        SpeedState oldLevel = level;
        level = SpeedState.OFF;
        logger.info("{} speed reduced from {} to {} (value: {})",
                name, oldLevel, level, level.getValue());

        logger.info("{} has been turned off by reducing speed to zero", name);
    }

    /**
     * Get status description
     */
    @Override
    public String getStatus() {
        if (!isPowerOn) {
            return name + " is POWERED OFF (Speed setting: " + level + " - value: " + level.getValue() + ")";
        } else if (level == SpeedState.OFF) {
            return name + " is OFF (Speed value: 0)";
        } else {
            return name + " is ON (Speed: " + level + " - value: " + level.getValue() + ")";
        }
    }

    /**
     * Get appliance type
     */
    @Override
    public ApplianceType getType() {
        return ApplianceType.FAN;
    }
}