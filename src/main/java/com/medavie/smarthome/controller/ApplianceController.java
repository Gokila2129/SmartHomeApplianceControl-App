package com.medavie.smarthome.controller;

import com.medavie.smarthome.domain.Appliance;
import com.medavie.smarthome.domain.enums.ApplianceCommand;
import com.medavie.smarthome.domain.enums.ApplianceType;
import com.medavie.smarthome.domain.impl.AirConditioner;
import com.medavie.smarthome.domain.impl.Fan;
import com.medavie.smarthome.domain.impl.Light;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * ApplianceController - responsible for managing appliances
 */
@Service
public class ApplianceController {

    private static final Logger logger = LoggerFactory.getLogger(ApplianceController.class);

    private final List<Appliance> appliances;

    private final Map<ApplianceCommand, Map<ApplianceType, BiConsumer<Appliance, String>>> commandHandlers;

    @Autowired
    public ApplianceController(List<Appliance> appliances) {
        this.appliances = appliances;
        this.commandHandlers = initializeCommandHandlers();
    }

    /**
     * Initialize the command handlers map
     */
    private Map<ApplianceCommand, Map<ApplianceType, BiConsumer<Appliance, String>>> initializeCommandHandlers() {
        Map<ApplianceCommand, Map<ApplianceType, BiConsumer<Appliance, String>>> handlers = new HashMap<>();

        handlers.put(ApplianceCommand.TOGGLE,
                Map.of(ApplianceType.LIGHT, (appliance, value) -> {
                    if (appliance instanceof Light light) {
                        light.toggle();
                    }
                })
        );

        handlers.put(ApplianceCommand.INCREASE_SPEED,
                Map.of(ApplianceType.FAN, (appliance, value) -> {
                    if (appliance instanceof Fan fan) {
                        fan.increaseSpeed();
                    }
                })
        );

        handlers.put(ApplianceCommand.DECREASE_SPEED,
                Map.of(ApplianceType.FAN, (appliance, value) -> {
                    if (appliance instanceof Fan fan) {
                        fan.decreaseSpeed();
                    }
                })
        );

        handlers.put(ApplianceCommand.SET_MODE,
                Map.of(ApplianceType.AC, (appliance, value) -> {
                    if (appliance instanceof AirConditioner ac) {
                        ac.setMode(value);
                    }
                })
        );

        return handlers;
    }

    /**
     * Execute a specific action on an appliance
     */
    public void executeAction(ApplianceType type, ApplianceCommand command, String value) {
        Appliance appliance = findAppliance(type);
        if (appliance == null) {
            logger.error("Appliance not found: {}", type);
            return;
        }

        Map<ApplianceType, BiConsumer<Appliance, String>> typeHandlers = commandHandlers.get(command);
        if (typeHandlers == null) {
            logger.error("Unsupported command: {}", command);
            return;
        }

        BiConsumer<Appliance, String> handler = typeHandlers.get(type);
        if (handler == null) {
            logger.error("Cannot execute {} on appliance type {}", command, type);
            return;
        }

        handler.accept(appliance, value);
    }

    /**
     * Turn off all appliances
     */
    public void turnOffAllAppliances() {
        logger.info("Turning off all appliances");
        appliances.forEach(Appliance::turnOff);
    }

    /**
     * Display status of all appliances
     */
    public void displayAllAppliancesStatus() {
        logger.info("Current appliance status:");
        appliances.forEach(appliance -> logger.info("  {}", appliance.getStatus()));
    }

    /**
     * Find appliance by type
     */
    private Appliance findAppliance(ApplianceType type) {
        return appliances.stream()
                .filter(a -> a.getType() == type)
                .findFirst()
                .orElse(null);
    }

    /**
     * Scheduled system update - runs yearly on January 1st at 1:00 AM
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    public void performSystemUpdate() {
        logger.info("""
            ------------------------------------------
            SYSTEM UPDATE INITIATED
            Turning off all appliances for the update
            ------------------------------------------""");
        turnOffAllAppliances();
        logger.info("""
            ------------------------------------------
            SYSTEM UPDATE COMPLETED
            ------------------------------------------""");
    }
}
