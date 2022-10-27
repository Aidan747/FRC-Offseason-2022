package frc.robot.util;

import java.util.Map;
import java.util.TreeMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class CameraNetworkTable {
    public static TreeMap<String, CameraNetworkTable> CameraList = new TreeMap<String, CameraNetworkTable>();
    public static NetworkTable CameraTable = NetworkTableInstance.getDefault().getTable("CameraTable");

    private String id;
    private String cameraLocation;
    private NetworkTable camTable;

    public static CameraNetworkTable findCamera(String id) {
        return CameraList.get(id);
    }

    public static CameraNetworkTable findCameraLocation(String type) {
        for (Map.Entry<String, CameraNetworkTable> cameraClass: CameraList.entrySet()) {
            if(cameraClass.getValue().getNetworkTable().getEntry("camera_location").getString("None").equals(type)) {
                return cameraClass.getValue();
            }
        }
        return null;
    }

    public CameraNetworkTable(String id, String cameraLocation) {
        this.id = id;
        
        // Adds our camera to the static camera list. This allows us to avoid the need to pass around the camera object if we need to quickly reference it somewhere.
        CameraList.put(this.id, this);

        // Creates a new sub-table in the main CameraTable so our server can index information solely about this camera.
        this.camTable = CameraTable.getSubTable(cameraLocation + " Camera");
        setupEntries();
    }

    public void setupEntries() {
        camTable.getEntry("camera_location").setString(cameraLocation);
    }

    public NetworkTable getNetworkTable() {
        return camTable;
    }

}
