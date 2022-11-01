package frc.robot.util.widgets;

import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class SendableChooserWidget<T> {
    SendableChooser<T> chooser;
    ComplexWidget widget;

    public SendableChooserWidget(ShuffleboardTab tab, String name, String[] names, T[] opts) {
        chooser = new SendableChooser<T>();
        for (int i = 0; i < opts.length; i++) {
            chooser.addOption(names[i], opts[i]);
        }
        this.widget = tab.add(name, chooser);
    }

    public ComplexWidget place(int x, int y) {
        return widget.withPosition(x, y);
    }

    public ComplexWidget resize(int x, int y) {
        return widget.withSize(x, y);
    }

}