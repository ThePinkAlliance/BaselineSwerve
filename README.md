# Baseline Swerve

This is a simple and configurable swerve project. The goal is to give you a starting point so have driving swerve robot ASAP with a relatively modular structure.

Sweve drive resources:

- https://www.youtube.com/watch?v=0Xi9yb1IMyA&t=28s

## Downloading/Setup

**Steps**:

- Clone git repo `git clone https://github.com/ThePinkAlliance/BaselineSwerve.git`
- Open cloned project in VSCode
- Build the project **Windows**: `gradlew build` **Linux**: `./gradlew build`

The project requires these libraries to function `ChoreoLib`, `REVLib`, `Phoenix6`, `AdvantageKit` when executing gradle build command (which vscode does automatically on first open) all of them will be downloaded automatically.

These are the json manifests for the required libraries.

- [AdvantageKit.json](./vendordeps/AdvantageKit.json)
- [ChoreoLib.json](./vendordeps/ChoreoLib.json)
- [Phoenix6.json](./vendordeps/Phoenix6.json)
- [REVLib.json](./vendordeps/REVLib.json)
- [WPILibNewCommands.json](./vendordeps/WPILibNewCommands.json)

## Project Structure

The project is comprised of two major folders located in [src/main/java/frc](./src/main/java/frc/) they are the [lib](./src/main/java/frc/lib/) and [robot](./src/main/java/frc/robot/) folders, both will contain all of java code for the robot.

#### [Robot](./src/main/java/frc/robot/)
  - ##### [Commands/](./src/main/java/frc/robot/commands/)
    This folder contains all the commands for the robot like driving the swerve drive. The swerve command in the [drive/](./src/main/java/frc/robot/commands/drive/) folder called `JoystickDrive.java`
  - ##### [Subsystems/](./src/main/java/frc/robot/subsystems/)
    The subsystems folder contains all the robot subsystems which manage the different mechanisms on the robot. The [drive](./src/main/java/frc/robot/subsystems/drive) folder contains the Swerve Subsystem and folder with the swerve pod module code.
  - ##### [Constants.java](./src/main/java/frc/robot/Constants.java)
    The constants file defines all the properties of the drivetrain to the swerve subsystem. Some of these properties are motor id's, drivetrain size, and maximum velocity. 
  - ##### [RobotContainer.java](./src/main/java/frc/robot/Constants.java)
    RobotContainer will be where you define button bindings for commands, default commands, and autonomous commands. It's where all of your subsytems will be [instantiated](https://stackoverflow.com/a/44315962) and passed to your commands.

## Configuring Constants
