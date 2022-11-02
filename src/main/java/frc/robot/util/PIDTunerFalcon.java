package frc.robot.util;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

public class PIDTunerFalcon {
    WPI_TalonFX tuning_motor;
    ShuffleboardTab subsystem_tab;
    int modifier = 1;

    public PIDTunerFalcon(WPI_TalonFX tuning_motor, ShuffleboardTab tab) {
        this.tuning_motor = tuning_motor;
        this.subsystem_tab = tab;
        initalize();
    }

    public void initalize() {
        subsystem_tab.add("RPM Control", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6380))
            .getEntry()
            .addListener(event -> {
                tuning_motor.set(ControlMode.Velocity, event.getEntry().getDouble(0) * modifier);
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        subsystem_tab.add("Invert Direction?", false)
            .withWidget(BuiltInWidgets.kToggleButton)
            .getEntry()
            .addListener(event -> {
                boolean checker = event.getEntry().getBoolean(false);
                if (checker) {
                    modifier = 1;
                } else {
                    modifier = -1;
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        SimpleWidget kPwidget = subsystem_tab.add("kP", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));
            
        kPwidget.getEntry()
            .addListener(event -> {
                tuning_motor.config_kP(0, event.getEntry().getDouble(0), 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        subsystem_tab.add("kP Max", 20)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 200))
            .getEntry()
            .addListener(event -> {
                kPwidget.withProperties(Map.of("min", 0, "max", event.getEntry().getDouble(20)));
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kIwidget = subsystem_tab.add("kI", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));
            
        kIwidget.getEntry()
            .addListener(event -> {
                tuning_motor.config_kI(0, event.getEntry().getDouble(0), 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        subsystem_tab.add("kI Max", 20)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 200))
            .getEntry()
            .addListener(event -> {
                kIwidget.withProperties(Map.of("min", 0, "max", event.getEntry().getDouble(20)));
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kDwidget = subsystem_tab.add("kD", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));
            
        kDwidget.getEntry()
            .addListener(event -> {
                tuning_motor.config_kD(0, event.getEntry().getDouble(0), 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        subsystem_tab.add("kD Max", 20)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 200))
            .getEntry()
            .addListener(event -> {
                kDwidget.withProperties(Map.of("min", 0, "max", event.getEntry().getDouble(20)));
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        subsystem_tab.add("FF Tune", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20))
            .getEntry()
            .addListener(event -> {
                tuning_motor.config_kF(0, event.getEntry().getDouble(0), 30);
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        graphSetups();
    }

    public void graphSetups() {
        subsystem_tab.addNumber("Current Velocity", () -> tuning_motor.getSelectedSensorVelocity())
            .withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("Visible time", 60)
        );

        subsystem_tab.addNumber("Current Error", () -> tuning_motor.getClosedLoopError())
            .withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("Visible time", 60)
        );
    }
}