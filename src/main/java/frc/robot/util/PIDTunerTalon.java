package frc.robot.util;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.TalonPIDBenchmarker;

public class PIDTunerTalon {
    WPI_TalonFX tuning_motor;
    ShuffleboardTab subsystem_tab;
    int id;
    int modifier = 1;
    double CONVERSION_RATE = 600.0 / 2048.0;

    boolean sus_mode = false;
    boolean bench_on = false;
    public String cur_string = "none";
    HashMap<String, Double> save = new HashMap<String, Double>();
    double threshold = 5.0;
    SuppliedValueWidget<double[]> veloGraph;
    SuppliedValueWidget<Double> errorGraph;
    NetworkTableEntry time_to_threshold_reporter;
    SimpleWidget benchWidget;
    SimpleWidget RPM;


    public PIDTunerTalon(WPI_TalonFX tuning_motor, ShuffleboardTab tab) {
        this.tuning_motor = tuning_motor;
        this.subsystem_tab = tab;
        this.id = tuning_motor.getDeviceID();
        initalize();
    }

    public void initalize() {
        this.RPM = subsystem_tab.add("RPM Control", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6380));

        RPM.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.set(ControlMode.Velocity, event.getEntry().getDouble(0) * CONVERSION_RATE);
                } else {
                    RPM.getEntry().setDouble(save.get("RPM"));
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget inverter = subsystem_tab.add("Invert Direction?", false)
            .withWidget(BuiltInWidgets.kToggleButton);

        inverter.getEntry()
            .addListener(event -> {
                tuning_motor.setInverted(event.getEntry().getBoolean(false));
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        SimpleWidget kPwidget = subsystem_tab.add("kP", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));
            
        kPwidget.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kP(0, event.getEntry().getDouble(0), 30);
                } else {
                    kPwidget.getEntry().setDouble(save.get("kP"));
                }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kPMax = subsystem_tab.add("kP Max", 20)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 200));
        
        kPMax.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    kPwidget.withProperties(Map.of("min", 0, "max", event.getEntry().getDouble(20)));
                } else {
                    kPMax.getEntry().setDouble(save.get("kPMax"));
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kIwidget = subsystem_tab.add("kI", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));
            
        kIwidget.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kI(0, event.getEntry().getDouble(0), 30);
                } else {
                    kIwidget.getEntry().setDouble(save.get("kI"));
                }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kIMax = subsystem_tab.add("kI Max", 20)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 200));

        kIMax.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    kIwidget.withProperties(Map.of("min", 0, "max", event.getEntry().getDouble(20)));
                } else {
                    kIMax.getEntry().setDouble(save.get("kIMax"));
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kDwidget = subsystem_tab.add("kD", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));
            
        kDwidget.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kD(0, event.getEntry().getDouble(0), 30);
                } else {
                    kDwidget.getEntry().setDouble(save.get("kD"));
                }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        SimpleWidget kDMax = subsystem_tab.add("kD Max", 20)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 200));

        kDMax.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    kDwidget.withProperties(Map.of("min", 0, "max", event.getEntry().getDouble(20)));
                } else {
                    kDMax.getEntry().setDouble(save.get("kDMax"));
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        SimpleWidget FFTune = subsystem_tab.add("FF Tune", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 20));

        FFTune.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kF(0, event.getEntry().getDouble(0), 30);
                } else {
                    FFTune.getEntry().setDouble(save.get("FFTune"));
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        graphSetups();

        SimpleWidget bench_mode = subsystem_tab.add("Benchmark Mode", false)
        .withWidget(BuiltInWidgets.kToggleButton);
        
        bench_mode.getEntry()
        .addListener(event -> {
            if (DriverStation.isEnabled()) {
                sus_mode = event.getEntry().getBoolean(false);
                if (bench_on || benchWidget.getEntry().getBoolean(false)) {
                    sus_mode = true;
                    bench_mode.getEntry().setBoolean(true);
                }
                if (sus_mode) {
                    save.put("RPM", RPM.getEntry().getDouble(0.0));
                    save.put("kP", kPwidget.getEntry().getDouble(0.0));
                    save.put("kPMax", kPMax.getEntry().getDouble(0.0));
                    save.put("kI", kIwidget.getEntry().getDouble(0.0));
                    save.put("kIMax", kIMax.getEntry().getDouble(0.0));
                    save.put("kD", kDwidget.getEntry().getDouble(0.0));
                    save.put("kDMax", kDMax.getEntry().getDouble(0.0));
                    save.put("FFTune", FFTune.getEntry().getDouble(0.0));
                    // ready to start motors on command
                    tuning_motor.set(ControlMode.PercentOutput, 0); // effectively turns off motor.
                }
            } else {
                bench_mode.getEntry().setBoolean(false);
                sus_mode = false;
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.time_to_threshold_reporter = subsystem_tab.add("Time", 0)
        .withWidget(BuiltInWidgets.kTextView)
        .getEntry();

        SimpleWidget thresholder = subsystem_tab.add("Threshold", 5.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", .01, "max", 20) // threshold should not exceed 20%.. seriously.
        );

        thresholder.getEntry()
        .addListener(event -> {
            if (sus_mode && !bench_on) {
                threshold = event.getEntry().getDouble(5);
            } else if (!sus_mode) {
                thresholder.getEntry().setDouble(threshold);
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.benchWidget = subsystem_tab.add("Begin Benchmark", false)
        .withWidget(BuiltInWidgets.kToggleButton);

        benchWidget.getEntry()
        .addListener(event -> {
            if (sus_mode && !bench_on && event.getEntry().getBoolean(true)) {
                bench_on = true;
                new ParallelCommandGroup(
                    new TalonPIDBenchmarker(this, tuning_motor, benchWidget, time_to_threshold_reporter),
                    new SequentialCommandGroup(
                        new WaitCommand(3), // .141 seconds off on test 1
                        new InstantCommand(() -> tuning_motor.getSimCollection().setIntegratedSensorVelocity((int)(2000 * CONVERSION_RATE)))
                    )
                ).schedule();
            } else if (sus_mode && bench_on && !event.getEntry().getBoolean(false)) {
                benchWidget.getEntry().setBoolean(true);
            } else {
                benchWidget.getEntry().setBoolean(false);
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    }

    public void graphSetups() {
        this.veloGraph = subsystem_tab.addDoubleArray("Current Velocity", () -> getGraphSetpoints())
            .withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("Visible time", 20, "Unit", "RPM")
        );

        this.errorGraph = subsystem_tab.addNumber("Current Error", () -> tuning_motor.getErrorDerivative() / CONVERSION_RATE)
            .withWidget(BuiltInWidgets.kGraph)
            .withProperties(Map.of("Visible time", 20, "Unit", "RPM")
        );
    }

    public void setBenchValue(boolean value) {
        bench_on = value;
    }

    public double getRPMSetpoint() {
        return save.get("RPM");
    }

    public double[] getGraphSetpoints() {
        return new double[] {tuning_motor.getSelectedSensorVelocity() / CONVERSION_RATE, RPM.getEntry().getDouble(0.0)};
    }


    public String getString() {
        return cur_string;
    }

    public double getThreshold() {
        return threshold;
    }
}