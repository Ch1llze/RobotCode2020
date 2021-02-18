/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.drive.DriveTrainBase;

public class DriveWithJoysticks extends CommandBase {
  /**
   * Creates a new DriveWithJoysticks.
   */

  private static final boolean alwaysUseHighMaxVel = true; // Whether to always use the max velocity of high gear or
  // of current gear
  private static final double rightStickScale = 0.5; // Factor of right stick when added
  private static final double curvatureTurnSensitivity = 1.5; // Greater than 1 allows for reverse output on inner side

  private DoubleSupplier oiLeftDriveX;
  private DoubleSupplier oiLeftDriveY;
  private DoubleSupplier oiLeftDriveTrigger;
  private DoubleSupplier oiRightDriveX;
  private DoubleSupplier oiRightDriveY;
  private DoubleSupplier oiRightDriveTrigger;
  private DoubleSupplier oiGetDeadband;
  private BooleanSupplier oiSniperMode;
  private DoubleSupplier oiSniperLevel;
  private DoubleSupplier oiSniperHighLevel;
  private DoubleSupplier oiSniperLowLevel;
  private BooleanSupplier oiSniperLow;
  private BooleanSupplier oiSniperHigh;
  private boolean oiHasDualSniperMode;
  private DriveTrainBase driveSubsystem;
  private SendableChooser<JoystickMode> joystickChooser;

  private boolean joysticksReversed = false;

  public DriveWithJoysticks(DoubleSupplier leftDriveX, DoubleSupplier leftDriveY, DoubleSupplier leftDriveTrigger,
      DoubleSupplier rightDriveX, DoubleSupplier rightDriveY, DoubleSupplier rightDriveTrigger,
      DoubleSupplier getDeadband, BooleanSupplier sniperMode, DoubleSupplier sniperLevel,
      DoubleSupplier sniperHighLevel, DoubleSupplier sniperLowLevel, BooleanSupplier sniperLow,
      BooleanSupplier sniperHigh, boolean hasDualSniperMode, SendableChooser<JoystickMode> joystickModeChooser,
      DriveTrainBase driveSubsystem) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    super();
    addRequirements(driveSubsystem);
    oiLeftDriveX = leftDriveX;
    oiLeftDriveY = leftDriveY;
    oiLeftDriveTrigger = leftDriveTrigger;
    oiRightDriveX = rightDriveX;
    oiRightDriveY = rightDriveY;
    oiRightDriveTrigger = rightDriveTrigger;
    oiGetDeadband = getDeadband;
    oiSniperMode = sniperMode;
    oiSniperLevel = sniperLevel;
    oiSniperHighLevel = sniperHighLevel;
    oiSniperLowLevel = sniperLowLevel;
    oiSniperLow = sniperLow;
    oiSniperHigh = sniperHigh;
    oiHasDualSniperMode = hasDualSniperMode;
    joystickChooser = joystickModeChooser;
    this.driveSubsystem = driveSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putBoolean("Driver Control", true);
  }

  private double processJoystickAxis(double joystickAxis) {
    // cube to improve low speed control, multiply by -1 because negative joystick
    // means forward, 0 if within deadband
    double deadband = oiGetDeadband.getAsDouble();
    if (Math.abs(joystickAxis) > deadband) {
      double adjustedValue = (joystickAxis - deadband) / (1 - deadband);
      return adjustedValue * Math.abs(adjustedValue) * -1;
    } else {
      return 0;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double joystickLeft = 0, joystickRight = 0;
    double baseDrive;
    double turnSpeed;
    switch (joystickChooser.getSelected()) {
      case Tank:
        joystickRight = joysticksReversed ? (processJoystickAxis(oiLeftDriveY.getAsDouble()) * -1)
            : (processJoystickAxis(oiLeftDriveY.getAsDouble()));
        joystickLeft = joysticksReversed ? (processJoystickAxis(oiRightDriveY.getAsDouble()) * -1)
            : processJoystickAxis(oiRightDriveY.getAsDouble());
        break;
      case Trigger:
        baseDrive = joysticksReversed ? ((oiRightDriveTrigger.getAsDouble() - oiLeftDriveTrigger.getAsDouble()) * -1)
            : (oiLeftDriveTrigger.getAsDouble() - oiRightDriveTrigger.getAsDouble()) * -1;
        turnSpeed = oiLeftDriveX.getAsDouble() + (oiRightDriveX.getAsDouble() * rightStickScale);

        joystickRight = joysticksReversed ? (baseDrive - processJoystickAxis(turnSpeed))
            : baseDrive + processJoystickAxis(turnSpeed);
        joystickLeft = joysticksReversed ? (baseDrive + processJoystickAxis(turnSpeed))
            : baseDrive - processJoystickAxis(turnSpeed);

        joystickRight = joystickRight > 1 ? 1 : joystickRight;
        joystickLeft = joystickLeft > 1 ? 1 : joystickLeft;
        break;
      case SplitArcade:
        baseDrive = joysticksReversed ? (processJoystickAxis(oiLeftDriveY.getAsDouble()) * -1)
            : processJoystickAxis(oiLeftDriveY.getAsDouble());
        joystickRight = joysticksReversed ? (baseDrive - processJoystickAxis(oiRightDriveX.getAsDouble()) * -1)
            : baseDrive + processJoystickAxis(oiRightDriveX.getAsDouble());
        joystickLeft = joysticksReversed ? (baseDrive + processJoystickAxis(oiRightDriveX.getAsDouble()) * -1)
            : baseDrive - processJoystickAxis(oiRightDriveX.getAsDouble());

        joystickRight = joystickRight > 1 ? 1 : joystickRight;
        joystickLeft = joystickLeft > 1 ? 1 : joystickLeft;
        break;
      case SplitArcadeRightDrive:
        baseDrive = joysticksReversed ? (processJoystickAxis(oiRightDriveY.getAsDouble()) * -1)
            : processJoystickAxis(oiRightDriveY.getAsDouble());
        joystickRight = joysticksReversed ? (baseDrive - processJoystickAxis(oiLeftDriveX.getAsDouble()))
            : baseDrive + processJoystickAxis(oiLeftDriveX.getAsDouble());
        joystickLeft = joysticksReversed ? (baseDrive + processJoystickAxis(oiLeftDriveX.getAsDouble()))
            : baseDrive - processJoystickAxis(oiLeftDriveX.getAsDouble());

        joystickRight = joystickRight > 1 ? 1 : joystickRight;
        joystickLeft = joystickLeft > 1 ? 1 : joystickLeft;
        break;
      case Curvature:
        baseDrive = joysticksReversed ? (processJoystickAxis(oiLeftDriveY.getAsDouble()) * -1)
            : processJoystickAxis(oiLeftDriveY.getAsDouble());
        turnSpeed = joysticksReversed ? (processJoystickAxis(oiRightDriveX.getAsDouble()) * -1)
            : processJoystickAxis(oiRightDriveX.getAsDouble());
        if (baseDrive != 0) {
          double maxBaseDrive = 1 / (1 + (turnSpeed * curvatureTurnSensitivity)); // Max speed where no output >1
          baseDrive = MathUtil.clamp(baseDrive, maxBaseDrive * -1, maxBaseDrive);
          turnSpeed = Math.abs(baseDrive) * turnSpeed * curvatureTurnSensitivity;
        }
        joystickRight = MathUtil.clamp(baseDrive + turnSpeed, -1, 1);
        joystickLeft = MathUtil.clamp(baseDrive - turnSpeed, -1, 1);
        break;
    }

    if (oiSniperMode.getAsBoolean()) {
      double multiplierLevel = getMultiplierForSniperMode();
      joystickLeft *= multiplierLevel;
      joystickRight *= multiplierLevel;
    }

    driveSubsystem.drive(joystickLeft, joystickRight, alwaysUseHighMaxVel);
  }

  public double getMultiplierForSniperMode() {
    double sniperLevel;
    if (oiHasDualSniperMode) {
      if (oiSniperHigh.getAsBoolean()) {
        sniperLevel = oiSniperHighLevel.getAsDouble();
      } else {
        sniperLevel = oiSniperLowLevel.getAsDouble();
      }
    } else {
      sniperLevel = oiSniperLevel.getAsDouble();
    }
    return sniperLevel;
  }

  public void setReversed(boolean reverse) {
    joysticksReversed = reverse;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putBoolean("Driver Control", false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public static enum JoystickMode {
    Tank, Trigger, SplitArcade, SplitArcadeRightDrive, Curvature;
  }
}
