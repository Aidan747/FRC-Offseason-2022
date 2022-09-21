// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Indexer;

public class Shoot extends CommandBase {
  /** Creates a new Shoot. */
  Indexer index;
  WPI_TalonFX shootie;
  WPI_TalonFX loadie;
  double time;
  public Shoot(Indexer index, WPI_TalonFX shootie, WPI_TalonFX loadie) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.index = index;
    this.shootie = shootie;
    this.loadie = loadie;
    addRequirements(index);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    index.setTopBeltSpeed(-1);
    shootie.setVoltage(-4);
    loadie.setVoltage(4);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
