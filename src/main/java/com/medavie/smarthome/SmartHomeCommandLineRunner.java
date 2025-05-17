package com.medavie.smarthome;

import com.medavie.smarthome.controller.ApplianceController;
import com.medavie.smarthome.domain.enums.ACMode;
import com.medavie.smarthome.domain.enums.ApplianceCommand;
import com.medavie.smarthome.domain.enums.ApplianceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

@Component
public class SmartHomeCommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SmartHomeCommandLineRunner.class);

    @Bean
    public CommandLineRunner interactiveDemo(ApplianceController controller) {
        return args -> {
            logger.info("Starting Smart Home Control System");
            logger.info("Available appliances:");
            controller.displayAllAppliancesStatus();

            try (Scanner scanner = new Scanner(System.in)) {
                Map<String, Consumer<Scanner>> commands = createCommandMap(controller);
                boolean running = true;

                while (running) {
                    displayMenu();

                    String choice = scanner.nextLine().trim();
                    if (choice.equals("0")) {
                        running = false;
                        logger.info("Exiting Smart Home Control System");
                        continue;
                    }

                    if (commands.containsKey(choice)) {
                        try {
                            commands.get(choice).accept(scanner);
                        } catch (Exception e) {
                            logger.error("Error executing command: {}", e.getMessage());
                        }
                    } else {
                        logger.info("Invalid option. Please try again.");
                    }
                }
            }
        };
    }

    /**
     * Create a map of command functions
     */
    private Map<String, Consumer<Scanner>> createCommandMap(ApplianceController controller) {
        Map<String, Consumer<Scanner>> commands = new HashMap<>();

        // 1. Toggle light
        commands.put("1", scanner -> {
            controller.executeAction(ApplianceType.LIGHT, ApplianceCommand.TOGGLE, null);
            logger.info("Light toggled");
        });

        // 2. Fan controls
        commands.put("2", scanner -> {
            logger.info("""
                Fan Control Options:
                1. Increase fan speed
                2. Decrease fan speed
                
                Enter option: """);
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> {
                    controller.executeAction(ApplianceType.FAN, ApplianceCommand.INCREASE_SPEED, null);
                }
                case "2" -> {
                    controller.executeAction(ApplianceType.FAN, ApplianceCommand.DECREASE_SPEED, null);
                }
                default -> logger.info("Invalid fan control option");
            }
        });

        // 3. Set AC mode
        commands.put("3", scanner -> {
            displayACModes();
            logger.info("Enter AC mode (0-4): ");
            String modeInput = scanner.nextLine().trim();

            try {
                int modeValue = Integer.parseInt(modeInput);
                ACMode mode = ACMode.fromValue(modeValue);
                controller.executeAction(ApplianceType.AC, ApplianceCommand.SET_MODE, String.valueOf(modeValue));
                logger.info("AC mode set to {} ({})", mode, modeValue);
            } catch (NumberFormatException e) {
                try {
                    ACMode mode = ACMode.valueOf(modeInput.toUpperCase());
                    controller.executeAction(ApplianceType.AC, ApplianceCommand.SET_MODE, mode.name());
                    logger.info("AC mode set to {} ({})", mode, mode.getValue());
                } catch (IllegalArgumentException ex) {
                    logger.error("Invalid AC mode. Please use a number (0-4) or mode name (OFF, COOL, etc.)");
                }
            }
        });

        // 4. Display status
        commands.put("4", scanner -> controller.displayAllAppliancesStatus());



        return commands;
    }

    private void displayMenu() {
        logger.info("""
            
            ======= SMART HOME CONTROL SYSTEM =======
            1. Toggle light
            2. Fan controls
            3. Set AC mode
            4. Display status
            0. Exit
            
            Enter your choice:""");
    }

    /**
     * Display AC modes with their numeric values in a clearer format
     */
    private void displayACModes() {
        logger.info("""
            Available AC modes:
            OFF = 0
            COOL = 1
            HEAT = 2
            FAN = 3
            AUTO = 4
            """);
    }
}

