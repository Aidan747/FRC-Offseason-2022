// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.util.PIDTunerTalon;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //CameraNetworkTable camera1 = new CameraNetworkTable("10.43.65.34", "Indexer");
  //public static final XboxController xbox = new XboxController(1);

  WPI_TalonFX falcon1 = new WPI_TalonFX(13);
  ShuffleboardTab tab = Shuffleboard.getTab("Tuning Tab");
  PIDTunerTalon talon_tuner1 = new PIDTunerTalon(falcon1, tab);

  // TalonFXSimCollection simmed;
  
  public HashMap<String, JoystickButton> xboxBinds = new HashMap<String, JoystickButton>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    // simmed = falcon1.getSimCollection();
    // simmed.setIntegratedSensorVelocity((int)(1000.0 * 600.0/2048.0));
    // for (int i = 1; i < MISC.KEY_NAMES.length; i++) {
    //   xboxBinds.put(MISC.KEY_NAMES[i], new JoystickButton(xbox, i));
    // }

    //xboxBinds.get("A").whenPressed(new RunCommand(() -> CameraNetworkTable.cycleView()));


    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new InstantCommand();
  }
}
