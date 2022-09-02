package frc.robot.subsystems;

import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */

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
    
    this.left = new MotorControllerGroup(leftMotorOne, leftMotorTwo);
    this.right = new MotorControllerGroup(leftMotorOne, rightMotorTwo);
    
    drive = new DifferentialDrive(left, right);
  }
  
  // we should start doing this with our classes, just so we can easily change configs without the mess in the decl.
  public void config() {
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
