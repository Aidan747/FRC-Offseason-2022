package frc.robot.subsystems;

// Java base imports

//WPILib imports
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Vendor imports
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

// in package imports
import frc.robot.subsystems.DriveTrain;

import frc.robot.Constants.DRIVE_CONSTANTS;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */

  public boolean testing = true;
  
  private WPI_TalonFX leftMotorOne;
  private WPI_TalonFX leftMotorTwo;
  private WPI_TalonFX rightMotorOne;
  private WPI_TalonFX rightMotorTwo;
  
  private MotorControllerGroup left;
  private MotorControllerGroup right;
  
  private DifferentialDrive drive;
  
  public DriveTrain(WPI_TalonFX leftMotorOne, WPI_TalonFX leftMotorTwo, WPI_TalonFX rightMotorOne, WPI_TalonFX rightMotorTwo) {
    this.leftMotorOne = leftMotorOne;
    this.leftMotorTwo = leftMotorTwo;
    this.rightMotorOne = rightMotorOne;
    this.rightMotorTwo = rightMotorTwo;
    
    this.left = new MotorControllerGroup(this.leftMotorOne, this.leftMotorTwo);
    this.right = new MotorControllerGroup(this.rightMotorOne, this.rightMotorTwo);
    
    this.drive = new DifferentialDrive(left, right);
    
    config();
    // if we are testing, enable testing mode.
    if (testing) {
      enableTesting();
    }
    
  }
  
  // we should start doing this with our classes, just so we can easily change configs without the mess in the decl.
  public void config() {
    left.setInverted(false);
    right.setInverted(true);
    rightMotorTwo.setSelectedSensorPosition(0);
    leftMotorTwo.setSelectedSensorPosition(0);
  }
  
  // runs testing software to monitor and control specific aspects about the subsystems.
  public void enableTesting() {
    ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
    tab.add("drivetrain", this);
    tab.addNumber("left encoder", () -> getEncoderLeft());
    tab.addNumber("right encoder", () -> getEncoderRight());
  }

  public void voltsDrive(double leftVs, double rightVs) {
    left.setVoltage(leftVs);
    right.setVoltage(rightVs);
    // when setting voltages, make sure to feed the safety system.
    drive.feed();
  }

  public void joyDrive(double Y, double Z) {
    // add slope later?
    drive.arcadeDrive(Y * DRIVE_CONSTANTS.MAX_COEFF, Z * DRIVE_CONSTANTS.MAX_COEFF);
  }

  public double getEncoderLeft() {
    // returns in meters
    // pos / encoder ticks = shaft rotations
    // shaft rotations * gearing = wheel rotations
    // wheel rotations * circumference = meters traveled
    double wheelRotations = (leftMotorTwo.getSelectedSensorPosition() / DRIVE_CONSTANTS.ENCODER_TICKS) * DRIVE_CONSTANTS.GEAR_RATIO;
    return wheelRotations * DRIVE_CONSTANTS.WHEEL_CIRCUMFRENCE;
  }

  public double getEncoderRight() {
    double wheelRotations = (rightMotorTwo.getSelectedSensorPosition() / DRIVE_CONSTANTS.ENCODER_TICKS) * DRIVE_CONSTANTS.GEAR_RATIO;
    return wheelRotations * DRIVE_CONSTANTS.WHEEL_CIRCUMFRENCE;
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
