// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ThePinkAlliance.core.util.Gains;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

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
    public static final double kPTurning = 0.47;
    public static final Gains kSteerGains = new Gains(0, 0, 0);
  }

  public static final class OIConstants {
    public static final double kJoystickDeadband = 0.05;
  }

  public static final class DriveConstants {

    // Distance between right and left wheels
    public static final double kTrackWidth = Units.inchesToMeters(23.75);

    // Distance between front and back wheels
    public static final double kWheelBase = Units.inchesToMeters(23.75);

    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2));

    public static final int kFrontLeftDriveMotorPort = 2;
    public static final int kBackLeftDriveMotorPort = 8;
    public static final int kFrontRightDriveMotorPort = 5;
    public static final int kBackRightDriveMotorPort = 11;

    public static final int kFrontLeftTurningMotorPort = 1;
    public static final int kBackLeftTurningMotorPort = 7;
    public static final int kFrontRightTurningMotorPort = 4;
    public static final int kBackRightTurningMotorPort = 10;

    /**
     * Don't changes these unless you know what your doing.
     * 
     * Unpon change without necessary module updates this will cause the pods to
     * oscillate.
     */
    public static final boolean kFrontLeftTurningReversed = false;
    public static final boolean kBackLeftTurningReversed = false;
    public static final boolean kFrontRightTurningReversed = false;
    public static final boolean kBackRightTurningReversed = false;

    public static final boolean kFrontLeftDriveEncoderReversed = true;
    public static final boolean kBackLeftDriveEncoderReversed = true;
    public static final boolean kFrontRightDriveEncoderReversed = true;
    public static final boolean kBackRightDriveEncoderReversed = true;

    /**
     * Port numbers for all the cancoders.
     */
    public static final int kFrontLeftDriveAbsoluteEncoderPort = 3;
    public static final int kBackLeftDriveAbsoluteEncoderPort = 9;
    public static final int kFrontRightDriveAbsoluteEncoderPort = 6;
    public static final int kBackRightDriveAbsoluteEncoderPort = 12;

    /**
     * These values where determined by lining up all the wheels and recording the
     * outputed positions.
     */
    public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = 0; // 1.9036
    public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = 0; // 1.9236
    public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 0; // 1.5615
    public static final double kBackRightDriveAbsoluteEncoderOffsetRad = 0; // 1.49563

    // This is the max speed without load.
    public static final double kPhysicalMaxSpeedMetersPerSecond = 6;
    public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 2 * 2 * Math.PI;

    public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond * 1; // 0.96
    public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxAngularSpeedRadiansPerSecond
        / 2.8;
    public static double kTeleDriveSpeedReduction = 1;
    public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 2.5;
    public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 3.5;
  }
}
