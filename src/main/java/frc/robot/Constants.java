package frc.robot;

// Constants class, usable anywhere if imported. 
// DO NOT SOLELY IMPORT THE CONSTANTS CLASS
// It looks ugly and refs get very long very quickly.

public final class Constants {
    public static final class MOTOR_IO {
        // motor ids go from back to front (closest to battery -> 1)
        // left motor IO
        public static final int LEFT_ONE = 4;
        public static final int LEFT_TWO = 2;
        // right motor IO
        public static final int RIGHT_ONE = 3;
        public static final int RIGHT_TWO = 1;
    }
    
    public static final class MISC {
        public static final String[] KEY_NAMES = {
            "null",
            "A",
            "B",
            "X",
            "Y",
            "LB",
            "RB",
            "back",
            "start"
        };
    }

    public static final class DRIVE_CONSTANTS {
        public static final double MAX_COEFF = .8;
    }
}
