// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

// java base imports

// WPILib imports

// Vendor imports

// In package imports
import frc.robot.subsystems.Indexer;

public class Index extends CommandBase {
  /** Creates a new Index. */
  Indexer indexSub;
  boolean reject;

  public Index(Indexer indexSub, boolean reject) {
    this.indexSub = indexSub;
    this.reject = reject;
    addRequirements(indexSub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!reject) {
      if(!indexSub.isTripped()) {
        // run top motors if it isnt tripped b/c we can store a ball in there
        indexSub.setTopBeltSpeed(-.25);
      } else {
        indexSub.setTopBeltSpeed(0);
      }
      indexSub.setBottomBeltSpeed(.25);
    } else {
      indexSub.setTopBeltSpeed(.5);
      indexSub.setBottomBeltSpeed(-.5);
    }
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
