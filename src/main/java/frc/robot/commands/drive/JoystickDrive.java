// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.drive.SwerveSubsystem;
import org.littletonrobotics.junction.Logger;

public class JoystickDrive extends Command {
  private SwerveSubsystem swerveSubsystem;
  private Supplier<Double> xInput, yInput, rotInput;
  private SlewRateLimiter xLimiter, yLimiter;
  private boolean useFieldCentric;
  private double startTime;

  /** Creates a new JoystickDrive. */
  public JoystickDrive(SwerveSubsystem swerveSubsystem, Supplier<Double> xInput, Supplier<Double> yInput,
      Supplier<Double> rotInput) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.useFieldCentric = true;

    this.swerveSubsystem = swerveSubsystem;
    this.xInput = xInput;
    this.yInput = yInput;
    this.rotInput = rotInput;
    this.startTime = 0;

    this.yLimiter = new SlewRateLimiter(Constants.DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
    this.xLimiter = new SlewRateLimiter(Constants.DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);

    addRequirements(swerveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.startTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = Math.abs(xInput.get()) > Constants.OIConstants.kJoystickDeadband ? xInput.get() : 0;
    double y = Math.abs(yInput.get()) > Constants.OIConstants.kJoystickDeadband ? yInput.get() : 0;
    double r = Math.abs(rotInput.get()) > Constants.OIConstants.kJoystickDeadband ? rotInput.get() : 0;

    // Square the controller input while preserving the sign.
    x = Math.copySign(x * x, x) * 1;
    y = Math.copySign(y * y, y) * 1;
    r = r * -1;

    // Limit the max acceleration and convert to meters.
    x = (xLimiter.calculate(x) * Constants.DriveConstants.kTeleDriveMaxSpeedMetersPerSecond);
    y = (yLimiter.calculate(y) * Constants.DriveConstants.kTeleDriveMaxSpeedMetersPerSecond);
    r = r * Constants.DriveConstants.kTeleDriveMaxAngularAccelerationUnitsPerSecond;

    // Convert from robot centric to field centric.
    Rotation2d robotAngle = swerveSubsystem.getRotation();

    ChassisSpeeds speeds = new ChassisSpeeds();
    if (useFieldCentric) {
      speeds = ChassisSpeeds.discretize(speeds, 0.02);
      speeds = ChassisSpeeds.fromRobotRelativeSpeeds(speeds, robotAngle);
    } else {
      speeds = new ChassisSpeeds(x, y, r);
    }

    double loop = 0.02;
    Pose2d robot_pose_vel = new Pose2d(speeds.vxMetersPerSecond * loop,
        speeds.vyMetersPerSecond * loop,
        Rotation2d.fromRadians(speeds.omegaRadiansPerSecond * loop));
    Twist2d twist_vel = new Pose2d().log(robot_pose_vel);
    speeds = new ChassisSpeeds(
        twist_vel.dx / loop, twist_vel.dy / loop, (twist_vel.dtheta / loop));

    Logger.recordOutput("Commands/JoystickDrive/vx_input", speeds.vxMetersPerSecond);
    Logger.recordOutput("Commands/JoystickDrive/vy_input", speeds.vyMetersPerSecond);
    Logger.recordOutput("Commands/JoystickDrive/omega_input", speeds.omegaRadiansPerSecond);

    Logger.recordOutput("Commands/JoystickDrive/robot_heading", robotAngle.getDegrees());
    Logger.recordOutput("Commands/JoystickDrive/robot_heading_rad", robotAngle.getRadians());
    Logger.recordOutput("Commands/JoystickDrive/drift_over_time",
        (robotAngle.getRadians() / (startTime - Timer.getFPGATimestamp())));

    swerveSubsystem.setStates(speeds);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.setStates(new ChassisSpeeds());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
