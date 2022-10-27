package frc.robot.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Constants.UTIL_CONSTANTS.*;

public class CameraNetworkTable {
    public static TreeMap<String, CameraNetworkTable> CameraList = new TreeMap<String, CameraNetworkTable>();
    public static NetworkTable CameraTable = NetworkTableInstance.getDefault().getTable("CameraTable");
    public static CameraNetworkTable CurrentCamera_VIEW;
    public static CameraNetworkTable CurrentCamera_IMAGE_PROCESSING;

    private String id;
    private String cameraLocation;
    private NetworkTable camTable;

    public static CameraNetworkTable findCamera(String id) {
        return CameraList.get(id);
    }

    public static void cycleView() {
        SortedMap<String, CameraNetworkTable> mapUp = CameraList.tailMap(CurrentCamera_VIEW.id, false);
        if (mapUp.size() == 0) {
            // we've reached the end of the map
            setViewCamera(CameraList.firstEntry().getValue());
        } else {
            setViewCamera(mapUp.get(mapUp.firstKey()));
        }
    }

    public static void panTiltZoomControl(double pan, double tilt, double zoom) {
        CurrentCamera_VIEW.getNetworkTable().getEntry("xyzoom").setNumberArray(
            new Number[] {(int)Math.floor(pan * 6), (int)Math.floor(tilt * 6), (int)Math.floor(zoom * 6)}
        );
    }

    public static void cycleProcessedCamera() {
        SortedMap<String, CameraNetworkTable> mapUp = CameraList.tailMap(CurrentCamera_IMAGE_PROCESSING.id, false);
        if (mapUp.size() == 0) {
            // we've reached the end of the map
            setProcessedCamera(CameraList.firstEntry().getValue());
        } else {
            setProcessedCamera(mapUp.get(mapUp.firstKey()));
        }
    }

    public static void setProcessedCamera(CameraNetworkTable camera) {
        CameraTable.getEntry("CURRENT_PROCESSED").setString(camera.id);
        CurrentCamera_IMAGE_PROCESSING = camera;
    }

    public static CameraNetworkTable getProcessedCamera() {
        return CurrentCamera_IMAGE_PROCESSING;
    }

    public static CameraNetworkTable getViewCamera() {
        return CurrentCamera_VIEW;
    }

    public static void setViewCamera(CameraNetworkTable camera) {
        CameraTable.getEntry("CURRENT_VIEW").setString(camera.id);
        CurrentCamera_VIEW = camera;
    }

    public static CameraNetworkTable findCameraByLocation(String type) {
        for (Map.Entry<String, CameraNetworkTable> cameraClass: CameraList.entrySet()) {
            if(cameraClass.getValue().getNetworkTable().getEntry("camera_location").getString("None").equals(type)) {
                return cameraClass.getValue();
            }
        }
        return null;
    }

    public CameraNetworkTable(String id, String cameraLocation) {
        this.id = id;
        this.cameraLocation = cameraLocation;
        
        // Adds our camera to the static camera list. This allows us to avoid the need to pass around the camera object if we need to quickly reference it somewhere.
        CameraList.put(this.id, this);

        // Creates a new sub-table in the main CameraTable so our server can index information solely about this camera.
        this.camTable = CameraTable.getSubTable(id);
        // Set up our "entries" into the NetworkTables, meaning that our jetson can access these values and change the camera's settings based on them.
        setupEntries();
    }

    public void setupEntries() {
        camTable.getEntry("id").setString(id);
        camTable.getEntry("camera_location").setString(cameraLocation);
        camTable.getEntry("brightness").setNumber(CAMERA_DEFAULTS.DEFAULT_BRIGHTNESS);
        camTable.getEntry("hue").setNumber(CAMERA_DEFAULTS.DEFAULT_HUE);
        camTable.getEntry("contrast").setNumber(CAMERA_DEFAULTS.DEFAULT_CONTRAST);
        camTable.getEntry("saturation").setNumber(CAMERA_DEFAULTS.DEFAULT_SATURATION);
        camTable.getEntry("max_fps").setNumber(CAMERA_DEFAULTS.MAX_FPS);
        camTable.getEntry("bitrate_minmax").setNumberArray(new Number[] {CAMERA_DEFAULTS.MIN_BITRATE, CAMERA_DEFAULTS.MAX_BITRATE});
        camTable.getEntry("resolution_type").setString(CAMERA_DEFAULTS.RESOLUTION);
        camTable.getEntry("focus").setNumber(CAMERA_DEFAULTS.FOCUS_RESET);
        camTable.getEntry("reboot_request").setBoolean(false);
        camTable.getEntry("camera_PTZ_type").setNumber(PTZ_CONTROL_TYPE.FREE);
        camTable.getEntry("record").setBoolean(false);
        camTable.getEntry("server_ready_status").setBoolean(true);
    }

    public NetworkTable getNetworkTable() {
        return camTable;
    }

    public void setAsProcessedCamera() {
        setProcessedCamera(this);
    }

    public boolean isReady() {
        return camTable.getEntry("client_ready_status").getBoolean(false);
    }

    public void setAsViewCamera() {
        setViewCamera(this);
    }
}