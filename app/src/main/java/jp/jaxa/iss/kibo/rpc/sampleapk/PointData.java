package jp.jaxa.iss.kibo.rpc.sampleapk;


import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class PointData {
    public Point point;
    public Quaternion quaternion;

    public PointData(Point point, Quaternion quaternion){
        this.point = point;
        this.quaternion = quaternion;
    }
}
