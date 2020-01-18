package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class OIHandheld extends OI {
    private boolean driveEnabled = true;
    private boolean openLoop = true;

    private Trigger openLoopSwitch = new Trigger(() -> openLoop);
    private Trigger driveDisableSwitch = new Trigger(() -> driveEnabled).negate();

    // map driver controller to ID 0 and operator controller to ID 1 in driver
    // station
    private XboxController driverController = new XboxController(0);
    private XboxController operatorController = new XboxController(1);

    private POVButton joysticksForward = new POVButton(driverController, 0);
    private POVButton joysticksReverse = new POVButton(driverController, 180);
    private JoystickButton toggleDriveEnabled = new JoystickButton(driverController, 7); // back button
    private JoystickButton toggleOpenLoop = new JoystickButton(driverController, 8); // start button

    public OIHandheld() {
        resetRumble();
        // The toggle buttons are not exposed and this class fakes having a disable
        // switch
        toggleDriveEnabled.whenPressed(new InstantCommand(() -> driveEnabled = !driveEnabled));
        toggleOpenLoop.whenPressed(new InstantCommand(() -> openLoop = !openLoop));
    }

    @Override
    public double getLeftDriveY() {
        return driverController.getY(Hand.kLeft);
    }

    @Override
    public double getLeftDriveX() {
        return driverController.getX(Hand.kLeft);
    }

    @Override
    public double getLeftDriveTrigger() {
        return driverController.getTriggerAxis(Hand.kLeft);
    }

    @Override
    public double getRightDriveY() {
        return driverController.getY(Hand.kRight);
    }

    @Override
    public double getRightDriveX() {
        return driverController.getX(Hand.kRight);
    }

    @Override
    public double getRightDriveTrigger() {
        return driverController.getTriggerAxis(Hand.kRight);
    }

    @Override
    public boolean hasDriveTriggers() {
        return true;
    }

    @Override
    public void setRumble(OIRumbleType type, double value) {
        value = value > 1 ? 1 : value;
        switch (type) {
        case DRIVER_LEFT:
            driverController.setRumble(RumbleType.kLeftRumble, value);
        case DRIVER_RIGHT:
            driverController.setRumble(RumbleType.kRightRumble, value);
        case OPERATOR_LEFT:
            operatorController.setRumble(RumbleType.kLeftRumble, value);
        case OPERATOR_RIGHT:
            operatorController.setRumble(RumbleType.kRightRumble, value);
        }
    }

    @Override
    public void resetRumble() {
        for (OIRumbleType type : OIRumbleType.values()) {
            setRumble(type, 0);
        }
    }

    @Override
    public Trigger getOpenLoopSwitch() {
        return openLoopSwitch;
    }

    @Override
    public Trigger getDriveDisableSwitch() {
        return driveDisableSwitch;
    }

    @Override
    public boolean getSniperMode() {
        return driverController.getAButton() || driverController.getBButton() || driverController.getBumper(Hand.kLeft)
                || driverController.getBumper(Hand.kRight);
    }

    @Override
    public boolean getSniperHigh() {
        return driverController.getBButton() || driverController.getBumper(Hand.kRight);
    }

    @Override
    public boolean getSniperLow() {
        return driverController.getAButton() || driverController.getBumper(Hand.kLeft);
    }

    @Override
    public boolean hasDualSniperMode() {
        return true;
    }

    @Override
    public Trigger getJoysticksForwardButton() {
        return joysticksForward;
    }

    @Override
    public Trigger getJoysticksReverseButton() {
        return joysticksReverse;
    }

    @Override
    public double getDeadband() {
        return 0.09;
    }
}
