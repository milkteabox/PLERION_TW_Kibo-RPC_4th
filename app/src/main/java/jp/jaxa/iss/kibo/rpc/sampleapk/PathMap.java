package jp.jaxa.iss.kibo.rpc.sampleapk;

import java.util.List;

import gov.nasa.arc.astrobee.types.Point;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

public class PathMap {
    List<Point>[][] allPath = new List[9][9];

    public void  PathMap() {
        allPath[0][1].add(0, new Point(10.5, -9.92284, 5.2988));
        allPath[0][1].add(1, point1);

        allPath[0][2].add(0, point2);

        allPath[0][3].add(0, new Point(10.5,-9.8,4.85));
        allPath[0][3].add(1, new Point(10.71,-7.7,4.48));
    }
}
