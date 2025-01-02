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

Let's start with the three subclasses inside [Constants.java](./src/main/java/frc/robot/Constants.java).

- DriveConstants
- ModuleConstants
- OIConstants

`DriveConstants` and `ModuleConstants` are the two you will interact with the most as a programmer since both of them are used to make swerve work.

### Configuring drivetrain properties

So before we jump into changing constants there's a few questions we should ask.

- What's the gear ratio of the swerve pods? (steer & drive)
- Do we know the track-width & wheel-base of the drivetrain?
- Do we know the id's of each modules motors and magnetic encoders?
- Is there more than one can network?

**NOTE:** If your not using falcons for both steering & driving and you still want to use this project you will need to write a custom module implementation which is out of scope of this guide.

Let's start with the motor id's for all the swerve modules in `ModuleConstants` you'll want to make sure that you change them to reflect the motors on each of the respective pods. An example would be blinking the steer & drive motors on the front left module and recording the id's.

```java
// All the driving ids
public static final int kFrontLeftDriveMotorId = 11;
public static final int kBackLeftDriveMotorId = 5;
public static final int kFrontRightDriveMotorId = 8;
public static final int kBackRightDriveMotorId = 2;

// All the steering ids
public static final int kFrontLeftSteerMotorId = 10;
public static final int kBackLeftSteerMotorId = 4;
public static final int kFrontRightSteerMotorId = 7;
public static final int kBackRightSteerMotorId = 1;
```

After you find all the id's for the motors on each pod we need to do the same thing for the CANCoder which report wheel orientation.

```java
/**
 * Port numbers for all the cancoders.
 */
public static final int kFrontLeftDriveCANCoderId = 12;
public static final int kBackLeftDriveCANCoderId = 6;
public static final int kFrontRightDriveCANCoderId = 9;
public static final int kBackRightDriveCANCoderId = 3;
```

Now that all the motor id's have been configured we can do our first test drive! 🎉

## Configuration test

So we are going to test that all of the motors on the swerve drive move and that the x & y axis on the controller. So, open frc driver station and enable teleop and the wheels should move!

### How are the modules moving?

Well in `RobotContainer.java` there a method called `configureBindings` this method is where all of your button bindings will be.

Here we are telling the `swerveSubsystem` that it will have a default command called `JoystickDrive` which will execute repeatly we also pass the joystick axises via `DoubleSuppliers`.

```java
private void configureBindings() {
  this.swerveSubsystem
      .setDefaultCommand(
            new JoystickDrive(swerveSubsystem, () -> driverJoystick.getRawAxis(JoystickMap.LEFT_X_AXIS),
                  () -> driverJoystick.getRawAxis(JoystickMap.LEFT_Y_AXIS),
                  () -> driverJoystick.getRawAxis(JoystickMap.RIGHT_X_AXIS)));
}
```

Now if all the wheels aren't moving the same direction that okay. In `Constants.java` the code below will let you change the directions independly for each module.

**Make sure when you move the left stick forward and all the wheels are spining forward**

```java
public static final boolean kFrontLeftSteerReversed = false;
public static final boolean kBackLeftSteerReversed = false;
public static final boolean kFrontRightSteerReversed = false;
public static final boolean kBackRightSteerReversed = false;
```

If not change the direction variables for each until they are all moving forward.

