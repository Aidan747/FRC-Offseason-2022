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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;


// In package imports
import frc.robot.Constants.*;
import frc.robot.Constants.MOTOR_IO.INDEX_IO;

public class Indexer extends SubsystemBase {
  /** Creates a new Indexer. */

  public boolean testing = true;

  private WPI_TalonFX top;
  private WPI_TalonSRX bottom;
  private WPI_TalonSRX intakeWheels;
  private WPI_TalonSRX drawbridgeMotor;

  private DigitalInput beam;

  public Indexer() {
    initialize();

    config();
    
    if (testing) {
      enableTesting();
    }
  }

  public void initialize() {
    top = new WPI_TalonFX(INDEX_IO.TOP);
    bottom = new WPI_TalonSRX(INDEX_IO.BOTTOM);
    intakeWheels = new WPI_TalonSRX(INDEX_IO.INTAKE_WHEELS);
    drawbridgeMotor = new WPI_TalonSRX(INDEX_IO.DRAWBRIDGE);
    beam = new DigitalInput(1); // DIO 4 on roboRIO
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

  public void setIntakeWheelSpeed(double percent) {
    intakeWheels.setVoltage(INDEXER_CONSTANTS.MAX_VOLTS * percent);
  }

  public void setDrawbridgeSpeed(double percent) {
    drawbridgeMotor.setVoltage(INDEXER_CONSTANTS.MAX_VOLTS * percent);
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
    setIntakeWheelSpeed(0);
    setDrawbridgeSpeed(0);
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
