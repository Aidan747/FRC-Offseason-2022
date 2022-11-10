package frc.robot.util;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
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
    SimpleWidget kPwidgetDirect;
    SimpleWidget kIwidgetDirect;
    SimpleWidget kDwidgetDirect;

    public PIDTunerTalon(WPI_TalonFX tuning_motor, ShuffleboardTab tab) {
        this.tuning_motor = tuning_motor;
        this.subsystem_tab = tab;
        this.id = tuning_motor.getDeviceID();
        initalize();
    }

    public void initalize() {
        // RPM
        ShuffleboardLayout RPMlayout = subsystem_tab.getLayout("RPM Settings", BuiltInLayouts.kList)
        .withSize(4, 2)
        .withPosition(3, 3);

        SimpleWidget RPMDirect = RPMlayout.add("RPM Control Direct", 0);

        RPMDirect.getEntry().addListener(event -> {
            if (!sus_mode) {
                tuning_motor.set(ControlMode.Velocity, event.getEntry().getDouble(0) / CONVERSION_RATE);
                RPM.getEntry().setNumber(event.getEntry().getDouble(0));
            } else {
                RPM.getEntry().setDouble(save.get("RPM"));
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.RPM = RPMlayout.add("RPM Control Slider", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6380));

        RPM.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.set(ControlMode.Velocity, event.getEntry().getDouble(0) / CONVERSION_RATE);
                    RPMDirect.getEntry().setNumber(event.getEntry().getDouble(0));
                } else {
                    RPMDirect.getEntry().setDouble(save.get("RPM"));
                }
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);


        SimpleWidget inverter = RPMlayout.add("Invert Direction?", false)
            .withWidget(BuiltInWidgets.kToggleButton);

        inverter.getEntry()
            .addListener(event -> {
                tuning_motor.setInverted(event.getEntry().getBoolean(false));
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        
        // kP
        ShuffleboardLayout kPLayout = subsystem_tab.getLayout("kP Settings", BuiltInLayouts.kList)
        .withSize(2, 2)
        .withPosition(0, 0);

        SimpleWidget kPwidget = kPLayout.add("kP", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 100));
        
        kPwidget.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kP(0, event.getEntry().getDouble(0), 30);
                    kPwidgetDirect.getEntry().setNumber(event.getEntry().getDouble(0));
                } else {
                    kPwidget.getEntry().setDouble(save.get("kP"));
                }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.kPwidgetDirect = kPLayout.add("kP Direct", 0);
        kPwidgetDirect.getEntry().addListener(event -> {
            if (!sus_mode) {
                tuning_motor.config_kP(0, event.getEntry().getDouble(0), 30);
                kPwidget.getEntry().setNumber(event.getEntry().getDouble(0));
            } else {
                kPwidgetDirect.getEntry().setDouble(save.get("kP"));
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // kI
        ShuffleboardLayout kILayout = subsystem_tab.getLayout("kI Settings", BuiltInLayouts.kList)
        .withPosition(8, 3)
        .withSize(2,2);

        SimpleWidget kIwidget = kILayout.add("kI", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 100));
        
        kIwidget.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kI(0, event.getEntry().getDouble(0), 30);
                    kIwidgetDirect.getEntry().setNumber(event.getEntry().getDouble(0));
                } else {
                    kIwidget.getEntry().setDouble(save.get("kI"));
                }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.kIwidgetDirect = kILayout.add("kI Direct", 0);
        kIwidgetDirect.getEntry().addListener(event -> {
            if (!sus_mode) {
                tuning_motor.config_kI(0, event.getEntry().getDouble(0), 30);
                kIwidget.getEntry().setNumber(event.getEntry().getDouble(0));
            } else {
                kIwidgetDirect.getEntry().setDouble(save.get("kI"));
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // kD
        ShuffleboardLayout kDLayout = subsystem_tab.getLayout("kD Settings", BuiltInLayouts.kList)
        .withPosition(0, 2)
        .withSize(2, 2);

        SimpleWidget kDwidget = kDLayout.add("kD", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 100));
        
        kDwidget.getEntry()
            .addListener(event -> {
                if (!sus_mode) {
                    tuning_motor.config_kD(0, event.getEntry().getDouble(0), 30);
                    kDwidgetDirect.getEntry().setNumber(event.getEntry().getDouble(0));
                } else {
                    kDwidget.getEntry().setDouble(save.get("kD"));
                }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.kDwidgetDirect = kDLayout.add("kD Direct", 0);
        kDwidgetDirect.getEntry().addListener(event -> {
            if (!sus_mode) {
                tuning_motor.config_kD(0, event.getEntry().getDouble(0), 30);
                kDwidget.getEntry().setNumber(event.getEntry().getDouble(0));
            } else {
                kDwidgetDirect.getEntry().setDouble(save.get("kD"));
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    
        // FF
        ShuffleboardLayout FFLayout = subsystem_tab.getLayout("FF Settings", BuiltInLayouts.kList)
        .withSize(2, 1)
        .withPosition(0, 4);
        
        SimpleWidget FFTune = FFLayout.add("FF Tune Direct", 0);
        FFTune.getEntry().addListener(event -> {
            if (!sus_mode) {
                tuning_motor.config_kF(0, event.getEntry().getDouble(0), 30);
            } else {
                kDwidget.getEntry().setDouble(save.get("FFTune"));
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        graphSetups();

        ShuffleboardLayout benchmode_layout = subsystem_tab.getLayout("Bench Settings", BuiltInLayouts.kList)
        .withSize(2, 3)
        .withPosition(8, 0);

        SimpleWidget bench_mode = benchmode_layout.add("Benchmark Mode", false)
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
                    save.put("kI", kIwidget.getEntry().getDouble(0.0));
                    save.put("kD", kDwidget.getEntry().getDouble(0.0));
                    save.put("FFTune", FFTune.getEntry().getDouble(0.0));
                    // ready to start motors on command
                    tuning_motor.set(ControlMode.PercentOutput, 0); // effectively turns off motor.
                }
            } else {
                bench_mode.getEntry().setBoolean(false);
                sus_mode = false;
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        this.time_to_threshold_reporter = benchmode_layout.add("Time", 0)
        .withWidget(BuiltInWidgets.kTextView)
        .getEntry();

        SimpleWidget thresholder = benchmode_layout.add("Threshold", 5.0)
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

        this.benchWidget = benchmode_layout.add("Begin Benchmark", false)
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

        this.errorGraph = subsystem_tab.addNumber("Current Error", () -> tuning_motor.getErrorDerivative() * CONVERSION_RATE)
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
        return new double[] {tuning_motor.getSelectedSensorVelocity() * CONVERSION_RATE, RPM.getEntry().getDouble(0.0)};
    }


    public String getString() {
        return cur_string;
    }

    public double getThreshold() {
        return threshold;
    }
}