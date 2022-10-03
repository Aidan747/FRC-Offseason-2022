package frc.robot;

// Java base imports
import java.util.HashMap;

//WPILib imports
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.DigitalInput;

// Vendor imports
import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

// in package imports
import frc.robot.Constants.MOTOR_IO.DRIVE_IO;
import frc.robot.Constants.MOTOR_IO.INDEX_IO;
import frc.robot.commands.EncoderDrivePrelim;
import frc.robot.commands.Index;
import frc.robot.commands.Shoot;
import frc.robot.Constants.MISC;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;


public class RobotContainer {
  /* 
    Base level classes (motors, subsystems, etc) are declared here
    Logic should be used only in subsystems/commands
  */
  // current reference point: Battery = back of robot
  
  // DriveTrain motors  
  private WPI_TalonFX leftOne = new WPI_TalonFX(DRIVE_IO.LEFT_ONE);
  private WPI_TalonFX leftTwo = new WPI_TalonFX(DRIVE_IO.LEFT_TWO);

  private WPI_TalonFX rightOne = new WPI_TalonFX(DRIVE_IO.RIGHT_ONE);
  private WPI_TalonFX rightTwo = new WPI_TalonFX(DRIVE_IO.RIGHT_TWO);

  private DriveTrain drive = new DriveTrain(leftOne, leftTwo, rightOne, rightTwo);

  // Indexer
  WPI_TalonFX indexTop = new WPI_TalonFX(INDEX_IO.TOP);
  WPI_TalonSRX indexBottom = new WPI_TalonSRX(INDEX_IO.BOTTOM);
  WPI_TalonSRX intakeWheels = new WPI_TalonSRX(INDEX_IO.INTAKE_WHEELS);
  WPI_TalonSRX drawbridgeMotor = new WPI_TalonSRX(INDEX_IO.DRAWBRIDGE);
  DigitalInput beam = new DigitalInput(1); // DIO 4 on roboRIO

  WPI_TalonFX shootie = new WPI_TalonFX(13);
  WPI_TalonFX loadie = new WPI_TalonFX(12);

  Indexer index = new Indexer(indexTop, indexBottom, intakeWheels, drawbridgeMotor, beam);

  // Joystick (is static so it can be ref anywhere)
  public static final Joystick joystick = new Joystick(0);
  public static final XboxController xbox = new XboxController(1);

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
      () -> drive.joyDrive(-joystick.getY(), joystick.getZ()), drive)
    );
    index.setDefaultCommand(new RunCommand(
      () -> index.idle(), index)
    );

    xboxBinds.get("A").toggleWhenPressed(new EncoderDrivePrelim(drive, 2, 4)); // drives 2 meters @ 4 volts
    xboxBinds.get("B").toggleWhenPressed(new ConditionalCommand(
      new Index(index, true), new Index(index, false), () -> xbox.getRawButton(8))
    );
    xboxBinds.get("X").toggleWhenPressed(new SequentialCommandGroup(
      new Shoot(index, shootie, loadie).andThen(new RunCommand(() -> index.setTopBeltSpeed(-.7), index).until(() -> index.isTripped())),
      new Index(index, true)
    ));
    
    xboxBinds.get("Y").toggleWhenPressed(new RunCommand(() -> index.setIntakeWheelSpeed(.4), index));

    xboxBinds.get("LB").toggleWhenPressed(new ConditionalCommand(
      new RunCommand(() -> index.setDrawbridgeSpeed(-.43), index), new RunCommand(() -> index.setDrawbridgeSpeed(.15), index), () -> xbox.getRawButton(8))
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
