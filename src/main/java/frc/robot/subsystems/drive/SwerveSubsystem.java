// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.subsystems.drive.modules.WPI_SwerveModule;
import org.littletonrobotics.junction.Logger;

public class SwerveSubsystem extends SubsystemBase {

  public SwerveModule frontLeftModule;
  public SwerveModule frontRightModule;
  public SwerveModule backLeftModule;
  public SwerveModule backRightModule;

  private SwerveDriveKinematics kinematics;
  private SwerveDrivePoseEstimator estimator;

  private Pigeon2 gyro;

  private double lastEpoch = 0;
  private double lastAngularPos = 0;

  /**
   * Creates a Swerve subsystem with the added kinematics.
   * 
   * @param kinematics
   */
  public SwerveSubsystem(SwerveDriveKinematics kinematics) {
    this.gyro = new Pigeon2(Constants.DriveConstants.kPigeonImuId, Constants.DriveConstants.kCANNetworkName);

    this.frontRightModule = new WPI_SwerveModule(
        ModuleConstants.kFrontRightSteerMotorId,
        ModuleConstants.kFrontRightDriveMotorId,
        ModuleConstants.kFrontRightDriveCANCoderId,
        ModuleConstants.kFrontRightDriveReversed,
        ModuleConstants.kFrontRightSteerReversed,
        ModuleConstants.kFrontRightDriveAbsoluteEncoderOffsetRad, ModuleConstants.kFrontRightSteerGains,
        DriveConstants.kCANNetworkName);

    this.frontLeftModule = new WPI_SwerveModule(
        ModuleConstants.kFrontLeftSteerMotorId,
        ModuleConstants.kFrontLeftDriveMotorId,
        ModuleConstants.kFrontLeftDriveCANCoderId,
        ModuleConstants.kFrontLeftDriveReversed,
        ModuleConstants.kFrontLeftSteerReversed,
        ModuleConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad, ModuleConstants.kFrontLeftSteerGains,
        DriveConstants.kCANNetworkName);

    this.backRightModule = new WPI_SwerveModule(
        ModuleConstants.kBackRightSteerMotorId,
        ModuleConstants.kBackRightDriveMotorId, ModuleConstants.kBackRightDriveCANCoderId,
        ModuleConstants.kBackRightDriveReversed,
        ModuleConstants.kBackRightSteerReversed,
        ModuleConstants.kBackRightDriveAbsoluteEncoderOffsetRad, ModuleConstants.kBackRightSteerGains,
        DriveConstants.kCANNetworkName);

    this.backLeftModule = new WPI_SwerveModule(
        ModuleConstants.kBackLeftSteerMotorId,
        ModuleConstants.kBackLeftDriveMotorId,
        ModuleConstants.kBackLeftDriveCANCoderId,
        ModuleConstants.kBackLeftDriveReversed,
        ModuleConstants.kBackLeftSteerReversed,
        ModuleConstants.kBackLeftDriveAbsoluteEncoderOffsetRad, ModuleConstants.kBackLeftSteerGains,
        DriveConstants.kCANNetworkName);

    this.kinematics = kinematics;

    /**
     * Initalizing the pose estimator.
     * 
     * Requires kinematics and an array of swerve module positions on the robot.
     * The VecBuilders are standard devation coefficients for encoder robot pose
     * estimate, and the second VecBuilder is for vision robot pose estimate.
     */
    this.estimator = new SwerveDrivePoseEstimator(
        kinematics, getRotation(), new SwerveModulePosition[] { frontRightModule.getPosition(),
            frontLeftModule.getPosition(), backRightModule.getPosition(),
            backLeftModule
                .getPosition() },
        new Pose2d(0, 0, new Rotation2d()), VecBuilder.fill(0.0, 0.0, 0.0),
        VecBuilder.fill(0.9, 0.9, 0.9));

    calibrateGyro();
  }

  public SwerveModulePosition[] getPositions() {
    return new SwerveModulePosition[] {
        frontRightModule.getPosition(),
        frontLeftModule.getPosition(),
        backRightModule.getPosition(),
        backLeftModule.getPosition()
    };
  }

  public SwerveModuleState[] getStates() {
    return new SwerveModuleState[] {
        frontRightModule.getState(),
        frontLeftModule.getState(),
        backRightModule.getState(),
        backLeftModule.getState()
    };
  }

  public StatusSignal<Double> getAccelX() {
    return gyro.getAccelerationX();
  }

  public StatusSignal<Double> verticalAccel() {
    return gyro.getAccelerationY();
  }

  public Rotation2d getRotation2d() {
    return gyro.getRotation2d();
  }

  public double getHeading() {
    return Math.IEEEremainder(gyro.getAngle() * -1, 360);
  }

  public double getYaw() {
    return gyro.getYaw().getValueAsDouble();
  }

  public Rotation2d getRotation() {
    return Rotation2d.fromDegrees(getHeading());
  }

  public void calibrateGyro() {
  }

  public void setGyro(double angle) {
    this.gyro.setYaw(angle);
  }

  public void resetGyro() {
    this.gyro.setYaw(0);
  }

  public void setStates(ChassisSpeeds speeds) {
    /**
     * The three lines below allow you to change the directions of each chassis
     * speed (x_velocity, y_velocity, theta_velocity).
     * 
     * You cannot alter the directions however you like! there are rules!
     * The only time you need to invert these is when any of these rules are
     * violated:
     * - If speeds.vyMetersPerSecond > 0 doesn't move the robot forward.
     * - If speeds.vyMetersPerSecond < 0 doesn't move the robot backwards.
     * 
     * - If speeds.vxMetersPerSecond > 0 doesn't move the robot right.
     * - If speeds.vxMetersPerSecond < 0 doesn't move the robot left.
     * 
     * - If speeds.omegaRadiansPerSecond > 0 doesn't spin the robot
     * counter-clockwise.
     * 
     * Well if your wondering why? its so the robot moves along wpilib's coordinate
     * frame.
     * More info:
     * https://docs.wpilib.org/en/stable/docs/software/basic-programming/coordinate-system.html
     */
    speeds.omegaRadiansPerSecond = speeds.omegaRadiansPerSecond * -1;
    speeds.vxMetersPerSecond = speeds.vxMetersPerSecond * -1;
    speeds.vyMetersPerSecond = speeds.vyMetersPerSecond * -1;

    /*
     * Check the angular drift with this solution & if I doesn't work explore the
     * possiblity of steer error in swerve pods.
     * 
     * NOTE: This can be broken down to smoothen robot driving. Like removing the
     * looper and etc.
     */
    Pose2d currentPose = getCurrentPose();
    SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);

    /**
     * Update the pose2d in advantagekit its not in periodic becase we want to only
     * send data when the pose changes
     */
    Logger.recordOutput("Swerve/Pose", currentPose);

    backLeftModule.setDesiredState(states[0]);
    backRightModule.setDesiredState(states[1]);
    frontLeftModule.setDesiredState(states[2]);
    frontRightModule.setDesiredState(states[3]);

    Logger.recordOutput("Swerve/States", states);
  }

  public void setSpeedModules(double speed) {
    SwerveModuleState state = new SwerveModuleState(speed, new Rotation2d());

    this.frontRightModule.setDesiredState(state);
    this.frontLeftModule.setDesiredState(state);
    this.backRightModule.setDesiredState(state);
    this.backLeftModule.setDesiredState(state);

    Logger.recordOutput("Swerve/Front Left State", state);
    Logger.recordOutput("Swerve/Front Right State", state);
    Logger.recordOutput("Swerve/Back Left State", state);
    Logger.recordOutput("Swerve/Back Right State", state);
  }

  public void resetPose(Pose2d pose2d) {
    estimator.resetPosition(getRotation(), getPositions(), pose2d);
  }

  public ChassisSpeeds getSpeeds() {
    return kinematics.toChassisSpeeds(getStates());
  }

  public Pose2d getCurrentPose() {
    return estimator.getEstimatedPosition();
  }

  public Pose2d getDifferentPose() {
    return new Pose2d(getCurrentPose().getX(), getCurrentPose().getY(), getRotation2d());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    Logger.recordOutput("Swerve/Front Right Absolute", frontRightModule.getRawAbsoluteAngularPosition());
    Logger.recordOutput("Swerve/Back Left Absolute", backLeftModule.getRawAbsoluteAngularPosition());
    Logger.recordOutput("Swerve/Back Right Absolute", backRightModule.getRawAbsoluteAngularPosition());
    Logger.recordOutput("Swerve/Front Left Absolute", frontLeftModule.getRawAbsoluteAngularPosition());

    Logger.recordOutput("Swerve/Front Right Position", frontRightModule.getDrivePosition());
    Logger.recordOutput("Swerve/Back Left Position", backLeftModule.getDrivePosition());
    Logger.recordOutput("Swerve/Back Right Position", backRightModule.getDrivePosition());
    Logger.recordOutput("Swerve/Front Left Position", frontLeftModule.getDrivePosition());
    Logger.recordOutput("Swerve/Heading", getHeading());
    Logger.recordOutput("Swerve/Heading Cont", gyro.getAngle());
    Logger.recordOutput("Swerve/Continuious Rotation", getRotation2d().getRadians());

    Logger.recordOutput("Swerve/Front Right Temperature", frontRightModule.getMotorTemp());
    Logger.recordOutput("Swerve/Back Left Temperature", backLeftModule.getMotorTemp());
    Logger.recordOutput("Swerve/Back Right Temperature", backRightModule.getMotorTemp());
    Logger.recordOutput("Swerve/Front Left Temperature", frontLeftModule.getMotorTemp());

    Logger.recordOutput("Swerve/Front Right Temperature Overheat Warning", frontRightModule.isMotorOverheated());
    Logger.recordOutput("Swerve/Back Left Temperature Overheat Warning", backLeftModule.isMotorOverheated());
    Logger.recordOutput("Swerve/Back Right Temperature Overheat Warning", backRightModule.isMotorOverheated());
    Logger.recordOutput("Swerve/Front Left Temperature Overheat Warning", frontLeftModule.isMotorOverheated());

    /**
     * This calculates the current angular velocity. It's mainly used for auto
     * calibration.
     */
    if (lastEpoch != 0) {
      double currentAngularPos = gyro.getAngle();
      Logger.recordOutput("Base/Angular Vel Rads",
          (currentAngularPos - lastAngularPos) * (Math.PI / 180) / (Timer.getFPGATimestamp() - lastEpoch));
      lastAngularPos = currentAngularPos;
    }

    estimator.update(getRotation(), getPositions());
    lastEpoch = Timer.getFPGATimestamp();
  }
}
