// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class EncoderDrivePrelim extends CommandBase {
  /** Creates a new EncoderDrivePrelim. */
  DriveTrain drive;
  double dist;
  double speedVs;
  double initEncoder;

  public EncoderDrivePrelim(DriveTrain drive, double dist, double speedVs) {
    this.drive = drive;
    this.dist = dist;
    this.speedVs = speedVs;
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.initEncoder = drive.getEncoderLeft();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive.voltsDrive(speedVs, -speedVs);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return dist >= drive.getEncoderLeft() - initEncoder;
  }
}
