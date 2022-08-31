// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

// we need to begin importing directly from embedded classes otherwise it looks ugly LOL
import frc.robot.Constants.MOTOR_IO;

public class RobotContainer {
  /* 
    Base level classes (motors, subsystems, etc) are declared here
    Logic should be used only in subsystems/commands.
  */
  // Battery = back of robot
  private WPI_TalonFX leftOne = new WPI_TalonFX(MOTOR_IO.LEFT_ONE); // look at how nice that is
  private WPI_TalonFX leftTwo = new WPI_TalonFX(MOTOR_IO.LEFT_TWO);

  private WPI_TalonFX rightOne = new WPI_TalonFX(MOTOR_IO.RIGHT_ONE);
  private WPI_TalonFX rightTwo = new WPI_TalonFX(MOTOR_IO.RIGHT_TWO);

  // ingenious strat using a for loop?!?!?!?!?!!?

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

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
