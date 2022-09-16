// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// java base imports

// WPILib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

// Vendor imports
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.NeutralMode;


// In package imports
import frc.robot.Constants.*;

public class Indexer extends SubsystemBase {
  /** Creates a new Indexer. */

  public boolean testing = true;

  private WPI_TalonFX top;
  private WPI_TalonFX bottom;

  private DigitalInput beam;

  public Indexer(WPI_TalonFX top, WPI_TalonFX bottom, DigitalInput beam) {
    this.top = top;
    this.bottom = bottom;
    this.beam = beam;

    config();
    
    if (testing) {
      enableTesting();
    }
  }

  public void config() {
    top.setNeutralMode(NeutralMode.Brake);
    bottom.setNeutralMode(NeutralMode.Brake);
  }

  public void setBottomBeltSpeed(double percent) {
    bottom.setVoltage(INDEXER_CONSTANTS.MAX_VOLTS * percent);
  }

  public void setTopBeltSpeed(double percent) {
    top.setVoltage(INDEXER_CONSTANTS.MAX_VOLTS * percent);
  }

  public double getBottomBeltSpeed() {
    return bottom.get();
  }

  public double getTopBeltSpeed() {
    return top.get();
  }

  public boolean isTripped() {
    return beam.get();
  }

  public void idle() {
    setTopBeltSpeed(0);
    setBottomBeltSpeed(0);
  }

  public void enableTesting() {
    ShuffleboardTab indexerTab = Shuffleboard.getTab("Indexer");
    indexerTab.addString("Top Belt Speed", () -> getBottomBeltSpeed() * 100 + "%");
    indexerTab.addString("Bottom Belt Speed", () -> getTopBeltSpeed() * 100 + "%");
    indexerTab.addBoolean("Beam Trip", () -> isTripped());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
