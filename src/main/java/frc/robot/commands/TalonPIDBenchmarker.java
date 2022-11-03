// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.PIDTunerTalon;

public class TalonPIDBenchmarker extends CommandBase {
  /** Creates a new TalonPIDBenchmarker. */
  private PIDTunerTalon tuner;
  private WPI_TalonFX to_tune;
  private SimpleWidget time_to_reporter;
  private SimpleWidget bench_reporter;

  private double time_to = -1;
  double CONVERSION_RATE = 2048.0 / 600.0;

  private double lower_bound;
  private double upper_bound;
  private int counter = 0;
  private boolean reached_time = false;


  private double start_time;

  public TalonPIDBenchmarker(PIDTunerTalon tuner, WPI_TalonFX to_tune, SimpleWidget time_to_reporter, SimpleWidget bench_reporter) {
    this.tuner = tuner;
    this.to_tune = to_tune;
    this.time_to_reporter = time_to_reporter;
    this.bench_reporter = bench_reporter;
    
    this.lower_bound = tuner.getRPMSetpoint() - tuner.getThreshold();
    this.upper_bound = tuner.getRPMSetpoint() + tuner.getThreshold();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    start_time = System.currentTimeMillis();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    to_tune.set(ControlMode.Velocity, tuner.getRPMSetpoint() * CONVERSION_RATE);
    double current_velo = to_tune.getSelectedSensorVelocity();
    if((current_velo >= lower_bound) && (current_velo <= upper_bound)) {
      counter++;
    } else {
      counter = 0;
    }

    if(counter >= 30) {
      time_to = ((System.currentTimeMillis() - start_time) - (counter * 20)) / 1000;
      reached_time = true; 
    }
    System.out.println(counter);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    tuner.setBenchValue(false);
    if (time_to == -1) {
      time_to_reporter.getEntry().setString("DNF");
    } else {
      time_to_reporter.getEntry().setString("results: " + time_to);
    }
    bench_reporter.getEntry().setBoolean(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return reached_time || !bench_reporter.getEntry().getBoolean(true);
  }
}
