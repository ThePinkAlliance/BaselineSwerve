// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import frc.lib.Gains;

/** Add your docs here. */
public class Constants {
  public static final class ModuleConstants {
    public static final double kWheelDiameterMeters = Units.inchesToMeters(4.09);
    public static final double kDriveMotorGearRatio = 1 / 8.14;
    public static final double kTurningMotorGearRatio = 1 / 12.8;
    public static final double kDriveEncoderRot2Meter = kDriveMotorGearRatio * Math.PI
        * kWheelDiameterMeters;
    public static final double kTurningEncoderRot2Rad = kTurningMotorGearRatio * 2 * Math.PI;
    public static final double kDriveEncoderRPM2MeterPerSec = kDriveEncoderRot2Meter / 60;
    public static final double kTurningEncoderRPM2RadPerSec = kTurningEncoderRot2Rad / 60;

    public static final Gains kBackLeftSteerGains = new Gains(.34, 0.0, 0);
    public static final Gains kBackRightSteerGains = new Gains(.34, 0.0, 0);
    public static final Gains kFrontRightSteerGains = new Gains(.34, 0.0, 0);
    public static final Gains kFrontLeftSteerGains = new Gains(.34, 0.0, 0);

    public static final boolean kFrontLeftDriveReversed = true;
    public static final boolean kBackLeftDriveReversed = true;
    public static final boolean kFrontRightDriveReversed = false;
    public static final boolean kBackRightDriveReversed = false;

    public static final boolean kFrontLeftSteerReversed = false;
    public static final boolean kBackLeftSteerReversed = false;
    public static final boolean kFrontRightSteerReversed = false;
    public static final boolean kBackRightSteerReversed = false;

    public static final int kFrontLeftDriveMotorId = 11;
    public static final int kBackLeftDriveMotorId = 5;
    public static final int kFrontRightDriveMotorId = 8;
    public static final int kBackRightDriveMotorId = 2;

    public static final int kFrontLeftSteerMotorId = 10;
    public static final int kBackLeftSteerMotorId = 4;
    public static final int kFrontRightSteerMotorId = 7;
    public static final int kBackRightSteerMotorId = 1;

    /**
     * Port numbers for all the cancoders.
     */
    public static final int kFrontLeftDriveCANCoderId = 12;
    public static final int kBackLeftDriveCANCoderId = 6;
    public static final int kFrontRightDriveCANCoderId = 9;
    public static final int kBackRightDriveCANCoderId = 3;

    /**
     * These values where determined by lining up all the wheels and recording the
     * outputed positions.
     */
    public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -0.552;
    public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = -2.207;
    public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 1.446;
    public static final double kBackRightDriveAbsoluteEncoderOffsetRad = -0.0184;
  }

  public static final class OIConstants {
    public static final double kJoystickDeadband = 0.05;
  }

  public static final class DriveConstants {

    // Distance between right and left wheels
    public static final double kTrackWidth = Units.inchesToMeters(23.75);

    // Distance between front and back wheels
    public static final double kWheelBase = Units.inchesToMeters(23.75);

    // CAN Network name with both the pigeon and swerve pods.
    public static final String kCANNetworkName = "rio";

    // DEVNOTE: This is positionally correct fix the wheel direction bug without
    // changing
    // this!
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, kTrackWidth / 2));

    public static final int kPigeonImuId = 0;

    // This is the max speed without load.
    public static final double kPhysicalMaxSpeedMetersPerSecond = 4.437;
    public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 2 * 2 * Math.PI;

    public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond * 1; // 0.96
    public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxAngularSpeedRadiansPerSecond
        / 2.8;
    public static double kTeleDriveSpeedReduction = 1;
    public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 2.5;
    public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 3.5;
  }
}
