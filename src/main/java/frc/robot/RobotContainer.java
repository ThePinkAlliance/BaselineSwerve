// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.JoystickMap;
import frc.robot.commands.drive.JoystickDrive;
import frc.robot.subsystems.drive.SwerveSubsystem;

public class RobotContainer {
    public final SwerveSubsystem swerveSubsystem;
    public final Joystick driverJoystick;

    public RobotContainer() {
        this.swerveSubsystem = new SwerveSubsystem(Constants.DriveConstants.kDriveKinematics);
        this.driverJoystick = new Joystick(0);

        configureAuto();
        configureBindings();
    }

    private void configureAuto() {

    }

    public void onTelInit() {
        swerveSubsystem.resetGyro();
    }

    public void onDisabled() {

    }

    private void configureBindings() {
        this.swerveSubsystem
                .setDefaultCommand(
                        new JoystickDrive(swerveSubsystem, () -> driverJoystick.getRawAxis(JoystickMap.LEFT_X_AXIS),
                                () -> driverJoystick.getRawAxis(JoystickMap.LEFT_Y_AXIS),
                                () -> driverJoystick.getRawAxis(JoystickMap.RIGHT_X_AXIS)));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
