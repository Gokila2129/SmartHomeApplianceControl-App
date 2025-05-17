# Smart Home Control System

## Project Overview

This application simulates a smart home control system capable of managing different types of appliances, each with their own unique way of being turned on and off.

### Appliance Types and Turn-off Mechanisms

1. **Light:** Turned off by toggling a switch to the "off" position
2. **Fan:** Turned off by reducing its speed to zero (0 = off, 1 = low, 2 = high)
3. **Air Conditioner:** Turned off by setting the thermostat to the "off" mode

The system also features an automatic update that runs once a year on January 1st at 1:00 AM, which turns off all devices for the update.

## Technology Stack

- Java 17
- Spring Boot 3.x
- Maven
- SLF4J for logging

## How to Run

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build and Run

```bash
# Clone the repository
git clone [repository-url]
cd smart-home-control

# Build the project
mvn clean package

# Run the application
java -jar target/smart-home-control-0.0.1-SNAPSHOT.jar
```

## Usage Guide

When the application starts, you'll see a menu with the following options:

```
======= SMART HOME CONTROL SYSTEM =======
1. Toggle light
2. Fan controls
3. Set AC thermostat mode
4. Display status
0. Exit

Enter your choice:
```

### Control Instructions

#### Light Control
- Option 1: Toggles the light on/off

#### Fan Control
- Option 2: Offers fan controls with a submenu:
    - 1: Increase fan speed (OFF → LOW → HIGH)
    - 2: Decrease fan speed (HIGH → LOW → OFF)

#### Air Conditioner Control
- Option 3: Set AC thermostat mode
    - Choose from modes: OFF=0, COOL=1, HEAT=2, FAN=3, AUTO=4
    - Enter either the mode name or its numeric value

#### System Controls
- Option 4: Display current status of all appliances
- Option 0: Exit the application

## Design Principles

This application implements:

- **SOLID principles** for maintainable, extensible code
- **Strategy Pattern** for different appliance behaviors
- **Command Pattern** for operations
- **Dependency Injection** through Spring

## Project Structure

```
com.medavie.smarthome
├── SmartHomeApplication.java
├── SmartHomeCommandLineRunner.java
├── controller
│   └── ApplianceController.java
├── domain
│   ├── Appliance.java (interface)
│   ├── enums
│   │   ├── ACMode.java
│   │   ├── ApplianceCommand.java
│   │   ├── ApplianceType.java
│   │   └── SpeedState.java
│   └── impl
│       ├── AirConditioner.java
│       ├── Fan.java
│       └── Light.java
```

## Implementation Notes

- Each appliance implements the core `Appliance` interface
- The scheduled system update is implemented using Spring's `@Scheduled` annotation
- Commands to control appliances are processed through a command pattern implementation
- The system logs detailed information about appliance state changes
