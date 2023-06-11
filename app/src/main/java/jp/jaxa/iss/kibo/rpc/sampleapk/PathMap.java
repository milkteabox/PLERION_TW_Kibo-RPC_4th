package jp.jaxa.iss.kibo.rpc.sampleapk;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.types.Point;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

public class PathMap {
    private List<Point>[][] allPath = new List[9][9];

    public PathMap() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                allPath[i][j] = new ArrayList<>();
            }
        }

        allPath[0][1].add(0, new Point(10.5, -9.92284, 5.2988));
        allPath[0][1].add(1, point1);

        allPath[0][2].add(0, point2);

        allPath[0][3].add(0, new Point(10.5,-8.635,4.85));
        allPath[0][3].add(1, new Point(10.71,-7.7,4.85));
        allPath[0][3].add(2, point3);

        allPath[0][4].add(0, new Point(10.51,-7.7,5.1804));
        allPath[0][4].add(1, point4);

        allPath[0][5].add(0, new Point(11.114,-9.0734,5.3393));
        allPath[0][5].add(1, point5);

        allPath[0][6].add(0, new Point(11.355,-8.9929,3.8));
        allPath[0][6].add(1, point6);

        allPath[5][4].add(0, point4);
    }

    public List<Point> getPath(int start, int end){
        return allPath[start][end];
    }
}
