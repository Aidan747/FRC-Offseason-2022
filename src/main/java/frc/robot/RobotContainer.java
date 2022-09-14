package frc.robot;

// Java base imports
import java.util.HashMap;

//WPILib imports
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// Vendor imports
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// in package imports
import frc.robot.Constants.MOTOR_IO;
import frc.robot.Constants.MISC;
import frc.robot.subsystems.DriveTrain;

public class RobotContainer {
  /* 
    Base level classes (motors, subsystems, etc) are declared here
    Logic should be used only in subsystems/commands
  */
  // current reference point: Battery = back of robot
  
  // DriveTrain motors  
  private CANSparkMax leftOne = new CANSparkMax(MOTOR_IO.LEFT_ONE, MotorType.kBrushed);
  private CANSparkMax leftTwo = new CANSparkMax(MOTOR_IO.LEFT_TWO, MotorType.kBrushed);

  private CANSparkMax rightOne = new CANSparkMax(MOTOR_IO.RIGHT_ONE, MotorType.kBrushed);
  private CANSparkMax rightTwo = new CANSparkMax(MOTOR_IO.RIGHT_TWO, MotorType.kBrushed);

  private DriveTrain drive = new DriveTrain(leftOne, leftTwo, rightOne, rightTwo);

  // Joystick (is static so it can be ref anywhere)
  public static final XboxController xbox = new XboxController(0);

  // AHRS (navx), static for ref anywhere
  public static final AHRS navX = new AHRS();

  // init of JoystickButton hashmap. Usage in the @RobotContainer method
  public HashMap<String, JoystickButton> xboxBinds = new HashMap<String, JoystickButton>();

  public RobotContainer() {
    // bind JoystickButtons to string keys in the hashmap
    // usage: (binds.get({name}) -> JoystickButton), names are located in Constants under MISC, usable anywhere with a valid
    // RobotContainer object (there should only be one)
    for (int i = 1; i < MISC.KEY_NAMES.length; i++) {
      xboxBinds.put(MISC.KEY_NAMES[i], new JoystickButton(xbox, i));
    }
    
    // default commands: check repo stuff for guides
    drive.setDefaultCommand(new RunCommand(
      () -> drive.joyDrive(xbox.getLeftX(), xbox.getRightX(), xbox.getRightY() * .5), drive)
    );
    // Configure the button bindings
    configureButtonBindings();
  }

  // Configures button bindings using hashmap
  private void configureButtonBindings() {}
  
  // Returns the currently used SendableChooser auto profile (names in Constants)
  public Command getAutonomousCommand() {
    // InstantCommand being returned right now for simplicity sake (does nothing)
    return new InstantCommand();
  }
}
