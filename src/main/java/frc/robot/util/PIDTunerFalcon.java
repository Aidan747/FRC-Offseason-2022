package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class PIDTunerFalcon {
    WPI_TalonFX tuning_motor;
    ShuffleboardTab subsystem_tab;

    public PIDTunerFalcon(WPI_TalonFX tuning_motor, ShuffleboardTab tab) {
        this.tuning_motor = tuning_motor;
        this.subsystem_tab = tab;
    }

    public void initalize() {
        subsystem_tab.add(sendable)
    }
}