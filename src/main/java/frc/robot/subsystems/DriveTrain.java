package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Constants.DRIVE_CONSTANTS;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */

  public boolean testing = false;
  
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
    
    drive = new DifferentialDrive(left, right);
    
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
    
  }

  public void voltsDrive(double leftVs, double rightVs) {
    left.setVoltage(leftVs);
    right.setVoltage(rightVs);
    // when setting voltages, make sure to feed the safety system.
    drive.feed();
  }

  public void joyDrive(double Y, double Z) {
    // add slope later?
    double fY = Math.min(Y, DRIVE_CONSTANTS.MAX_COEFF);
    double fZ = Math.min(Z, DRIVE_CONSTANTS.MAX_COEFF);
    drive.tankDrive(fY, fZ);
  }

  public double getEncoderLeft() {
    // returns in meters
    return (leftMotorTwo.getSelectedSensorPosition() / DRIVE_CONSTANTS.ENCODER_TICKS) * DRIVE_CONSTANTS.GEAR_RATIO;
  }

  public double getEncoderRight() {
    return rightMotorTwo.getSelectedSensorPosition();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
