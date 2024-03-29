// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive.modules;

import com.ThePinkAlliance.core.util.Gains;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.subsystems.drive.SwerveModule;

/** Add your docs here. */
public class REV_SwerveModule implements SwerveModule {
  private PIDController steerController;

  private CANSparkMax driveMotor;
  private CANSparkMax steerMotor;
  private WPI_CANCoder canCoder;

  private RelativeEncoder driveEncoder;
  private RelativeEncoder steerEncoder;

  private double absoluteEncoderOffsetRad;

  public REV_SwerveModule(int steerId, int driveId, int canCoderId, boolean invertDrive, boolean invertSteer,
      double absoluteEncoderOffsetRad,
      Gains steerGains) {
    this.canCoder = new WPI_CANCoder(canCoderId);
    this.steerMotor = new CANSparkMax(steerId, MotorType.kBrushless);
    this.driveMotor = new CANSparkMax(driveId, MotorType.kBrushless);

    this.driveEncoder = driveMotor.getEncoder();
    this.steerEncoder = steerMotor.getEncoder();

    this.absoluteEncoderOffsetRad = absoluteEncoderOffsetRad;

    this.steerController = new PIDController(steerGains.kP, steerGains.kI, steerGains.kD);
    this.steerController.enableContinuousInput(-Math.PI, Math.PI);

    this.driveMotor.setInverted(invertDrive);
    this.steerMotor.setInverted(invertSteer);

    this.driveMotor.setIdleMode(IdleMode.kBrake);
    this.canCoder.configAbsoluteSensorRange(AbsoluteSensorRange.Signed_PlusMinus180);

    resetEncoders();
  }

  /**
   * Returns the drive wheel position in meters.
   */
  @Override
  public double getDrivePosition() {
    return ((driveEncoder.getPosition())
        * Constants.ModuleConstants.kDriveMotorGearRatio)
        * (Constants.ModuleConstants.kWheelDiameterMeters * Math.PI);
  }

  /*
   * This returns the current position of the steer shaft in radians.
   */
  @Override
  public double getSteerPosition() {
    return Math.toRadians(canCoder.getAbsolutePosition()) - absoluteEncoderOffsetRad;
  }

  @Override
  public double getRawAbsoluteAngularPosition() {
    return Math.toRadians(canCoder.getAbsolutePosition());
  }

  @Override
  public double getDriveVelocity() {
    /*
     * It might be necessary to change the constant because it does not take into
     * account the gear ratio.
     */
    return ((driveMotor.getEncoder().getVelocity() / 60) * Constants.ModuleConstants.kDriveMotorGearRatio)
        * (Constants.ModuleConstants.kWheelDiameterMeters * Math.PI);
  }

  /**
   * Returns the velocity of the steer motor in rad/sec.
   */
  @Override
  public double getSteerVelocity() {
    return Math.toRadians(canCoder.getVelocity());
  }

  @Override
  public void resetEncoders() {
    driveEncoder.setPosition(0);
    steerEncoder.setPosition(getAbsoluteEncoderAngle());
  }

  @Override
  public double getAbsoluteEncoderAngle() {
    double angle = canCoder.getAbsolutePosition();

    angle *= (2.0 * Math.PI / 180);
    angle -= absoluteEncoderOffsetRad;

    return angle;
  }

  /*
   * Returns the current state of the swerve module using velocity.
   */
  @Override
  public SwerveModuleState getState() {
    return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getSteerPosition()));
  }

  /*
   * Returns the current state of the swerve module using position.
   */
  @Override
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(getDrivePosition(), new Rotation2d(getSteerPosition()));
  }

  /**
   * Sets the current module state to the desired one.
   * 
   * @param state desired swerve module state
   */
  @Override
  public void setDesiredState(SwerveModuleState state) {
    state = SwerveModuleState.optimize(state, getState().angle);
    driveMotor
        .setVoltage((state.speedMetersPerSecond / Constants.DriveConstants.kPhysicalMaxSpeedMetersPerSecond) * 12);

    double output = steerController.calculate(getSteerPosition(), state.angle.getRadians());
    steerMotor.set(output);
  }

  @Override
  public void stop() {
    driveMotor.set(0);
    steerMotor.set(0);
  }
}
