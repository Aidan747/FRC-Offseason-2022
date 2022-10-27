// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.CameraNetworkTable;

public class CameraControls extends CommandBase {
  XboxController joy;

  /** Creates a new CameraControls. */
  public CameraControls(XboxController joy) {
    this.joy = joy;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (joy.getRightTriggerAxis() > .5) {
      CameraNetworkTable.panTiltZoomControl(joy.getLeftX(), joy.getLeftY(), joy.getRightY());
    } else {
      CameraNetworkTable.panTiltZoomControl(0, 0, 0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
