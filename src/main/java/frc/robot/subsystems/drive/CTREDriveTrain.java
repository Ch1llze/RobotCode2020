/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.util.Units;
import frc.robot.Constants;
import frckit.physics.drivetrain.differential.DifferentialDrivetrainDynamics;

public class CTREDriveTrain extends DriveTrainBase {

  private static final int configTimeoutInit = 10;
  private static final int configTimeoutRuntime = 0;

  private TalonSRX leftMaster;
  private BaseMotorController leftFollower1;
  private BaseMotorController leftFollower2;
  private TalonSRX rightMaster;
  private BaseMotorController rightFollower1;
  private BaseMotorController rightFollower2;
  private int ticksPerRotation;
  private boolean sixMotorDrive = false;

  /**
   * Creates a new CTREDriveTrain.
   */
  @SuppressWarnings("incomplete-switch") // Not all robots need to be supported
  public CTREDriveTrain(BooleanSupplier driveDisableSwitchAccess, BooleanSupplier openLoopSwitchAccess,
      BooleanSupplier shiftLockSwitchAccess) {
    super(driveDisableSwitchAccess, openLoopSwitchAccess, shiftLockSwitchAccess);
    FeedbackDevice encoderType = FeedbackDevice.None;
    boolean reverseOutputLeft = false;
    boolean reverseOutputRight = false;
    boolean reverseSensorLeft = false;
    boolean reverseSensorRight = false;
    switch (Constants.getRobot()) {
      case NOTBOT:
        leftMaster = new TalonSRX(1);
        leftFollower1 = new TalonSRX(2);
        rightMaster = new TalonSRX(3);
        rightFollower1 = new TalonSRX(4);
        maxVelocityLow = 122.282131;
        encoderType = FeedbackDevice.QuadEncoder;
        ticksPerRotation = 1440;
        wheelDiameter = 5.9000000002; // 6
        reverseSensorRight = false;
        reverseSensorLeft = false;
        reverseOutputLeft = false;
        reverseOutputRight = true;
        kPLow = 2;
        kILow = 0;
        kDLow = 40;
        kIZoneLow = 0;
        leftKsLow = 0;
        leftKvLow = 0.287655057;
        leftKaLow = 0;
        leftTorquePerVoltLow = Double.POSITIVE_INFINITY;
        rightKsLow = 0;
        rightKvLow = 0.287655057;
        rightKaLow = 0;
        rightTorquePerVoltLow = Double.POSITIVE_INFINITY;
        massKg = 0;
        moiKgM2 = 0;
        angularDragLow = 0.0;
        wheelbaseInches = 24.0;
        trackScrubFactor = 1;
        break;
      case ORIGINAL_ROBOT_2018:
        leftMaster = new TalonSRX(12);
        leftFollower1 = new TalonSRX(13);
        rightMaster = new TalonSRX(2);
        rightFollower1 = new TalonSRX(0);
        leftGearSolenoid1 = 0;
        leftGearSolenoid2 = 1;
        leftGearPCM = 1;
        rightGearSolenoid1 = 2;
        rightGearSolenoid2 = 3;
        rightGearPCM = 0;
        maxVelocityLow = 106;
        maxVelocityHigh = 230;
        dualGear = true;
        encoderType = FeedbackDevice.CTRE_MagEncoder_Relative;
        ticksPerRotation = 4096;
        wheelDiameter = 4.25;
        reverseSensorRight = false;
        reverseSensorLeft = false;
        reverseOutputLeft = true;
        reverseOutputRight = false;
        kPLow = 0.5;
        kILow = 0.003;
        kIZoneLow = 300;
        kDLow = 30;
        kPHigh = 0.8;
        kIHigh = 0;
        kDHigh = 10;
        leftKsLow = 0;
        leftKvLow = 0.24055;
        leftKaLow = 0;
        leftTorquePerVoltLow = Double.POSITIVE_INFINITY;
        rightKsLow = 0;
        rightKvLow = 0.24055;
        rightKaLow = 0;
        rightTorquePerVoltLow = Double.POSITIVE_INFINITY;
        leftKsHigh = 0;
        leftKvHigh = 0.11086;
        leftKaHigh = 0;
        leftTorquePerVoltHigh = Double.POSITIVE_INFINITY;
        rightKsHigh = 0;
        rightKvHigh = 0.11086;
        rightKaHigh = 0;
        rightTorquePerVoltHigh = Double.POSITIVE_INFINITY;
        massKg = 0;
        moiKgM2 = 0;
        angularDragLow = 0.0;
        wheelbaseInches = 24.0;
        trackScrubFactor = 1;
        break;
      case ROBOT_2019:
        leftMaster = new TalonSRX(12);
        leftFollower1 = new VictorSPX(14);
        leftFollower2 = new VictorSPX(13);
        rightMaster = new TalonSRX(3);
        rightFollower1 = new VictorSPX(2);
        rightFollower2 = new VictorSPX(1);
        ptoSolenoid1 = 3;
        ptoSolenoid2 = 2;
        ptoPCM = 1;
        maxVelocityLow = 170.566392;
        sixMotorDrive = true;
        encoderType = FeedbackDevice.CTRE_MagEncoder_Relative;
        ticksPerRotation = 4096;
        wheelDiameter = 4.633; // Testing of DriveDistanceOnHeading suggests this may not be right
        reverseSensorRight = true;
        reverseSensorLeft = true;
        reverseOutputLeft = true;
        reverseOutputRight = false;
        kPLow = 0.8;
        kILow = 0;
        kIZoneLow = 0;
        kDLow = 30;
        leftKsLow = 1.21;
        leftKvLow = 0.13690515;
        leftKaLow = 0.0421603;
        leftTorquePerVoltLow = (2.6 / 12.0) * 2 * Double.POSITIVE_INFINITY;
        rightKsLow = 1.21;
        rightKvLow = 0.13690515;
        rightKaLow = 0.0421603;
        rightTorquePerVoltLow = (2.6 / 12.0) * 2 * Double.POSITIVE_INFINITY;
        massKg = 0;
        moiKgM2 = 0;
        angularDragLow = 0.0;
        wheelbaseInches = 24.0;
        trackScrubFactor = 27.5932064868814 / wheelbaseInches;
        hasPTO = true;
        break;
      case REBOT:
        leftMaster = new TalonSRX(12);
        leftFollower1 = new VictorSPX(13);
        leftFollower2 = new VictorSPX(14);
        rightMaster = new TalonSRX(1);
        rightFollower1 = new VictorSPX(2);
        rightFollower2 = new VictorSPX(3);
        maxVelocityLow = 125.94;
        sixMotorDrive = true;
        encoderType = FeedbackDevice.CTRE_MagEncoder_Relative;
        ticksPerRotation = 4096;
        wheelDiameter = 5; // This is just a best guess, make sure to measure before tuning
        reverseSensorRight = true;
        reverseSensorLeft = true;
        reverseOutputLeft = false;
        reverseOutputRight = true;
        kPLow = 0.6;
        kILow = 0.0007;
        kDLow = 6;
        kIZoneLow = 4096 * 50 / 600;
        leftKsLow = 0;
        leftKvLow = 0.21733;
        leftKaLow = 0;
        leftTorquePerVoltLow = (2.6 / 12.0) * 2 * Double.POSITIVE_INFINITY;
        rightKsLow = 0;
        rightKvLow = 0.21733;
        rightKaLow = 0;
        rightTorquePerVoltLow = (2.6 / 12.0) * 2 * Double.POSITIVE_INFINITY;
        massKg = 0;
        moiKgM2 = 0;
        angularDragLow = 0.0;
        wheelbaseInches = 24.0;
        trackScrubFactor = 1;
        break;
    }
    dynamicsLow = DifferentialDrivetrainDynamics.fromHybridCharacterization(massKg, moiKgM2, angularDragLow,
        Units.inchesToMeters(wheelDiameter / 2), Units.inchesToMeters(wheelbaseInches) * trackScrubFactor / 2.0,
        leftKsLow, leftKvLow, leftTorquePerVoltLow, rightKsLow, rightKvLow, rightTorquePerVoltLow);
    dynamicsHigh = DifferentialDrivetrainDynamics.fromHybridCharacterization(massKg, moiKgM2, angularDragHigh,
        Units.inchesToMeters(wheelDiameter / 2), Units.inchesToMeters(wheelbaseInches) * trackScrubFactor / 2.0,
        leftKsHigh, leftKvHigh, leftTorquePerVoltHigh, rightKsHigh, rightKvHigh, rightTorquePerVoltHigh);

    leftMaster.configFactoryDefault(configTimeoutInit);
    leftFollower1.configFactoryDefault(configTimeoutInit);
    leftMaster.configSelectedFeedbackSensor(encoderType, 0, configTimeoutInit);
    leftMaster.setInverted(reverseOutputLeft);
    leftFollower1.setInverted(reverseOutputLeft);
    leftMaster.setSensorPhase(reverseSensorLeft);
    leftFollower1.follow(leftMaster);
    rightMaster.configFactoryDefault(configTimeoutInit);
    rightFollower1.configFactoryDefault(configTimeoutInit);
    rightMaster.configSelectedFeedbackSensor(encoderType, 0, configTimeoutInit);
    rightMaster.setInverted(reverseOutputRight);
    rightFollower1.setInverted(reverseOutputRight);
    rightMaster.setSensorPhase(reverseSensorRight);
    rightFollower1.follow(rightMaster);
    if (sixMotorDrive) {
      leftFollower2.configFactoryDefault(configTimeoutInit);
      leftFollower2.setInverted(reverseOutputLeft);
      leftFollower2.follow(leftMaster);
      rightFollower2.configFactoryDefault(configTimeoutInit);
      rightFollower2.setInverted(reverseOutputRight);
      rightFollower2.follow(rightMaster);
    }
    leftMaster.configVoltageCompSaturation(12.0, configTimeoutInit);
    rightMaster.configVoltageCompSaturation(12.0, configTimeoutInit);
    leftMaster.enableVoltageCompensation(true);
    rightMaster.enableVoltageCompensation(true);
    initialize();
  }

  @Override
  public double getGearReduction() {
    return 1.0; // Encoder is directly on drive wheels
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void neutralOutput() {
    leftMaster.neutralOutput();
    rightMaster.neutralOutput();
  }

  @Override
  protected void driveOpenLoopLowLevel(double left, double right) {
    leftMaster.set(ControlMode.PercentOutput, left / 12);
    rightMaster.set(ControlMode.PercentOutput, right / 12);
  }

  @Override
  protected void driveClosedLoopLowLevel(double left, double right, double leftVolts, double rightVolts) {
    double leftTicksPer100ms = left * ticksPerRotation / (20 * Math.PI);
    double rightTicksPer100ms = right * ticksPerRotation / (20 * Math.PI);
    leftMaster.set(ControlMode.Velocity, leftTicksPer100ms, DemandType.ArbitraryFeedForward, leftVolts / 12.0);
    rightMaster.set(ControlMode.Velocity, rightTicksPer100ms, DemandType.ArbitraryFeedForward, rightVolts / 12.0);
  }

  @Override
  public void enableBrakeMode(boolean enable) {
    NeutralMode mode = enable ? NeutralMode.Brake : NeutralMode.Coast;
    leftMaster.setNeutralMode(mode);
    leftFollower1.setNeutralMode(mode);
    rightMaster.setNeutralMode(mode);
    rightFollower1.setNeutralMode(mode);
    if (sixMotorDrive) {
      leftFollower2.setNeutralMode(mode);
      rightFollower2.setNeutralMode(mode);
    }
  }

  @Override
  public void resetPosition() {
    leftMaster.setSelectedSensorPosition(0);
    rightMaster.setSelectedSensorPosition(0);
  }

  @Override
  public double getRotationsLeft() {
    return (double) leftMaster.getSelectedSensorPosition() / ticksPerRotation;
  }

  @Override
  public double getRotationsRight() {
    return (double) rightMaster.getSelectedSensorPosition() / ticksPerRotation;
  }

  @Override
  public double getRPSRight() {
    return (double) rightMaster.getSelectedSensorVelocity() / ticksPerRotation * 10;
  }

  @Override
  public double getRPSLeft() {
    return (double) leftMaster.getSelectedSensorVelocity() / ticksPerRotation * 10;
  }

  @Override
  public double getCurrent() {
    return (rightMaster.getStatorCurrent() + leftMaster.getStatorCurrent()) / 2;
  }

  @Override
  protected void setPID(int slotIdx, double p, double i, double d, int iZone) {
    leftMaster.config_kP(slotIdx, p, configTimeoutRuntime);
    leftMaster.config_kI(slotIdx, i, configTimeoutRuntime);
    leftMaster.config_kD(slotIdx, d, configTimeoutRuntime);
    leftMaster.config_kF(slotIdx, 0.0, configTimeoutRuntime);
    leftMaster.config_IntegralZone(slotIdx, iZone, configTimeoutRuntime);
    rightMaster.config_kP(slotIdx, p, configTimeoutRuntime);
    rightMaster.config_kI(slotIdx, i, configTimeoutRuntime);
    rightMaster.config_kD(slotIdx, d, configTimeoutRuntime);
    rightMaster.config_kF(slotIdx, 0.0, configTimeoutRuntime);
    rightMaster.config_IntegralZone(slotIdx, iZone, configTimeoutRuntime);
  }

  @Override
  public void changeStatusRate(int ms) {
    leftMaster.setStatusFramePeriod(StatusFrame.Status_1_General, ms, configTimeoutRuntime);
    rightMaster.setStatusFramePeriod(StatusFrame.Status_1_General, ms, configTimeoutRuntime);
  }

  @Override
  public void resetStatusRate() {
    leftMaster.setStatusFramePeriod(StatusFrame.Status_1_General, 10, configTimeoutRuntime);
    rightMaster.setStatusFramePeriod(StatusFrame.Status_1_General, 10, configTimeoutRuntime);
  }

  @Override
  public void changeSensorRate(int ms) {
    leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, ms, configTimeoutRuntime);
    rightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, ms, configTimeoutRuntime);
  }

  @Override
  public void resetSensorRate() {
    leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, configTimeoutRuntime);
    rightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, configTimeoutRuntime);
  }

  @Override
  public void changeControlRate(int ms) {
    leftMaster.setControlFramePeriod(ControlFrame.Control_3_General, ms);
    rightMaster.setControlFramePeriod(ControlFrame.Control_3_General, ms);
  }

  @Override
  public void resetControlRate() {
    leftMaster.setControlFramePeriod(ControlFrame.Control_3_General, 10);
    rightMaster.setControlFramePeriod(ControlFrame.Control_3_General, 10);
  }

  @Override
  protected void setProfileSlot(int slotIdx) {
    leftMaster.selectProfileSlot(slotIdx, 0);
    rightMaster.selectProfileSlot(slotIdx, 0);
  }
}
