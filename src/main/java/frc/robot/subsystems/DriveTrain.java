package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;

import frc.robot.Constants.DRIVE_CONSTANTS;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */

  public boolean testing = false;
  
  private CANSparkMax leftMotorOne;
  private CANSparkMax leftMotorTwo;
  private CANSparkMax rightMotorOne;
  private CANSparkMax rightMotorTwo;
  
  private MotorControllerGroup left;
  private MotorControllerGroup right;
  
  private MecanumDrive drive;
  
  public DriveTrain(CANSparkMax leftMotorOne, CANSparkMax leftMotorTwo, CANSparkMax rightMotorOne, CANSparkMax rightMotorTwo) {
    this.leftMotorOne = leftMotorOne;
    this.leftMotorTwo = leftMotorTwo;
    this.rightMotorOne = rightMotorOne;
    this.rightMotorTwo = rightMotorTwo;
    
    this.left = new MotorControllerGroup(this.leftMotorOne, this.leftMotorTwo);
    this.right = new MotorControllerGroup(this.rightMotorOne, this.rightMotorTwo);
    
    drive = new MecanumDrive(leftMotorTwo, leftMotorOne, rightMotorTwo, rightMotorOne);
    
    Shuffleboard.getTab("DriveTrain").add("Mecanum", drive);

    config();
    // if we are testing, enable testing mode.
    if (testing) {
      enableTesting();
    }
    
  }
  
  // we should start doing this with our classes, just so we can easily change configs without the mess in the decl.
  public void config() {
    
  }
  
  // runs testing software to monitor and control specific aspects about the subsystems.
  public void enableTesting() {
  }

  public void voltsDrive(double leftVs, double rightVs) {
    left.setVoltage(leftVs);
    right.setVoltage(rightVs);
    // when setting voltages, make sure to feed the safety system.
    drive.feed();
  }

  public void joyDrive(double Y, double X, double Z) {
    // add slope later?
    double fY = Math.min(Y, DRIVE_CONSTANTS.MAX_COEFF);
    double fX = -Math.min(Z, DRIVE_CONSTANTS.MAX_COEFF);
    double fZ = Math.min(Z, DRIVE_CONSTANTS.MAX_COEFF);
    drive.driveCartesian(fY, fX, fZ);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}