// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class UTIL_CONSTANTS {
        
        public static final class PTZ_CONTROL_TYPE {
            public static final int FREE = 0;
            public static final int PRESET_POINT = 1;
        }

        public static final class CAMERA_DEFAULTS {
            public static final int DEFAULT_BRIGHTNESS = 50;
            public static final int DEFAULT_SATURATION = 50;
            public static final int DEFAULT_HUE = 50;
            public static final int DEFAULT_CONTRAST = 50;
            public static final int MAX_FPS = 30;
            public static final int MIN_BITRATE = 48;
            public static final int MAX_BITRATE = 448;
            public static final String RESOLUTION = "1080P";
            public static final int FOCUS_RESET = -1;
            public static final int X_AXIS_ROTATE = 0;
            public static final int Y_AXIS_ROTATE = 0;
            public static final int ZOOM = 0;
        }
        
    }
}
