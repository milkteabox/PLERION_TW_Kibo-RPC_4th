package jp.jaxa.iss.kibo.rpc.sampleapk;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.types.Point;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

public class PathMap {
    private List<Point>[][] allPath = new List[8][9];
    private Long[][] allPathTime = new Long[8][9];

    public PathMap() {
        setPathMap();
        setPathtime();
    }

    private void setPathtime(){
        allPathTime[1][2] = 44500l;
        allPathTime[1][3] = 66500l;
        allPathTime[1][4] = 76500l;
        allPathTime[1][5] = 35500l;
        allPathTime[1][6] = 36500l;
        allPathTime[1][8] = 62500l;

        allPathTime[2][1] = 50000l;
        allPathTime[2][3] = 62500l;
        allPathTime[2][4] = 73500l;
        allPathTime[2][5] = 65500l;
        allPathTime[2][6] = 57500l;
        allPathTime[2][8] = 82500l;

        allPathTime[3][1] = 50000l;
        allPathTime[3][2] = 62500l;
        allPathTime[3][4] = 73500l;
        allPathTime[3][5] = 65500l;
        allPathTime[3][6] = 57500l;
        allPathTime[3][8] = 82500l;
    }

    private void setPathMap() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                allPath[i][j] = new ArrayList<>();
            }
        }

        allPath[0][1].add(0, new Point(10.5, -9.92284, 5.2988));
        allPath[0][1].add(1, Aim1Point);

        allPath[0][2].add(0, Aim2Point);

        allPath[0][3].add(0, new Point(10.5,-8.635,4.85));
        allPath[0][3].add(1, new Point(10.71,-7.7,4.85));
        allPath[0][3].add(2, Aim3Point);

        allPath[0][4].add(0, new Point(10.51,-7.7,5.1804));
        allPath[0][4].add(1, Aim4Point);

        allPath[0][5].add(0, new Point(11.114,-9.0734,5.3393));
        allPath[0][5].add(1, Aim5Point);

        allPath[0][6].add(0, new Point(10.7,-8.9929,4.9));
        allPath[0][6].add(1, new Point(11.2,-8.9929,4.9));
        allPath[0][6].add(2, Aim6Point);


        allPath[1][2].add(0, new Point(10.6,-9.5,5.2988));
        allPath[1][2].add(1, Aim2Point);

        allPath[1][3].add(0, new Point(10.71,-9.5,5.2988));
        allPath[1][3].add(1, new Point(10.71,-7.7,4.85));
        allPath[1][3].add(2, Aim3Point);

        allPath[1][4].add(0, new Point(10.51,-9.5,5.2988));
        allPath[1][4].add(1, new Point(10.51,-8.3,5.18));
        allPath[1][4].add(2, Aim4Point);

        allPath[1][5].add(0, Aim5Point);

        allPath[1][6].add(0, new Point(11.19,-9.5,5.2988));
        allPath[1][6].add(1, Aim6Point);

        allPath[1][7].add(0, new Point(11.19,-9.5,5.2988));
        allPath[1][7].add(1, pointQR);

        allPath[1][8].add(0, new Point(11.143,-7.35,5.474));
        allPath[1][8].add(1, pointGoal);



        allPath[2][1].add(0, new Point(11.19,-9.5,5.2988));
        allPath[2][1].add(1, Aim1Point);

        allPath[2][3].add(0, new Point(10.61,-8.55,4.85));
        allPath[2][3].add(1, new Point(10.61,-8.1,4.85));
        allPath[2][3].add(2, Aim3Point);

        allPath[2][4].add(0, new Point(10.51,-8.55,5.3));
        allPath[2][4].add(1, new Point(10.51,-6.7185,5.3));
        allPath[2][4].add(2, Aim4Point);

        allPath[2][5].add(0, new Point(10.61,-8.55,4.85));
        allPath[2][5].add(1, new Point(10.61,-8.25,5.33));
        allPath[2][5].add(2, Aim5Point);

        allPath[2][6].add(0, new Point(10.61,-8.9929,4.85));
        allPath[2][6].add(1, new Point(11.355,-8.9929,4.85));
        allPath[2][6].add(2, Aim6Point);

        allPath[2][7].add(0, new Point(10.61,-8.5518,4.85));
        allPath[2][7].add(1, new Point(11.369,-8.5518,4.85));
        allPath[2][7].add(2, pointQR);

        allPath[2][8].add(0, new Point(10.61,-8.55,4.9));
        allPath[2][8].add(1, new Point(10.61,-8.21,5.33));
        allPath[2][8].add(2, new Point(10.90,-7.5,5.33));
        allPath[2][8].add(3, pointGoal);


        allPath[3][1].add(0, new Point(10.71,-7.7,4.9));
        allPath[3][1].add(1, new Point(10.96,-8.5,-5.2988));
        allPath[3][1].add(2, Aim1Point);

        allPath[3][2].add(0, new Point(10.61,-8.1,4.85));
        allPath[3][2].add(1, new Point(10.61,-8.55,4.85));
        allPath[3][2].add(2, Aim2Point);

        allPath[3][4].add(0, new Point(10.51,-7.35,5.1));
        allPath[3][4].add(1, Aim4Point);

        allPath[3][5].add(0, new Point(10.6,-7.7,4.48));
        allPath[3][5].add(1, new Point(10.6,-7.9756,5.3393));
        allPath[3][5].add(2, Aim5Point);

        allPath[3][6].add(0, new Point(10.9,-8.07,4.9));
        allPath[3][6].add(1, new Point(11.355,-8.9929,4.9));
        allPath[3][6].add(2, Aim6Point);

        allPath[3][7].add(0, new Point(10.9,-8.07,4.9));
        allPath[3][7].add(1, new Point(11.369,-8.5518,4.9));
        allPath[3][7].add(2, pointQR);

        allPath[3][8].add(0, pointGoal);



        allPath[4][1].add(0, new Point(10.51,-8.3,5.1));
        allPath[4][1].add(1, new Point(10.51,-9.5,5.2988));
        allPath[4][1].add(2, Aim1Point);

        allPath[4][2].add(0, new Point(10.51,-7.4,5.18));
        allPath[4][2].add(1, new Point(10.6,-8.6,4.9));
        allPath[4][2].add(2, Aim2Point);

        allPath[4][3].add(0, new Point(10.51,-7.4,5.18));
        allPath[4][3].add(1, new Point(10.51,-7.7,4.8));
        allPath[4][3].add(2, Aim3Point);

        allPath[4][5].add(0, new Point(11,-7.5,5.3393));
        allPath[4][5].add(1, Aim5Point);

        allPath[4][6].add(0, new Point(10.8,-7.5,5.3393));
        allPath[4][6].add(1, new Point(11.355,-8.3,5.33));
        allPath[4][6].add(2, Aim6Point);

        allPath[4][7].add(0, new Point(10.8,-7.5,5.3393));
        allPath[4][7].add(1, new Point(11.369,-8.3,5.33));
        allPath[4][7].add(2, pointQR);

        allPath[4][8].add(0, new Point(10.95,-6.75,5.1804));
        allPath[4][8].add(1, pointGoal);

        allPath[5][1].add(0, new Point(11.114,-8.3,5.3393));
        allPath[5][1].add(1, Aim1Point);

        allPath[5][2].add(0, new Point(10.8,-8.3,5.3393));
        allPath[5][2].add(1, new Point(10.7,-8.5,4.9));
        allPath[5][2].add(2, Aim2Point);

        allPath[5][3].add(0, new Point(10.5,-7.7,5.3393));
        allPath[5][3].add(1, new Point(10.5,-7.7,4.9));
        allPath[5][3].add(2, Aim3Point);

        allPath[5][4].add(0, Aim4Point);

        allPath[5][6].add(0, new Point(11.15,-8.3,5.3393));
        allPath[5][6].add(1, new Point(11.3,-8.5,4.9));
        allPath[5][6].add(2, Aim6Point);

        allPath[5][7].add(0, new Point(11.15,-8.3,5.3393));
        allPath[5][7].add(1, new Point(11.369,-8.5518,4.9));
        allPath[5][7].add(2, pointQR);

        allPath[5][8].add(0, new Point(11.114,-7.5,5.3393));
        allPath[5][8].add(1, pointGoal);

        allPath[6][1].add(0, new Point(11.2764,-9.45,5.2988));
        allPath[6][1].add(1, Aim1Point);

        allPath[6][2].add(0, new Point(11.2,-8.6,4.9));
        allPath[6][2].add(1, new Point(10.612,-9.0709,4.9));
        allPath[6][2].add(2, Aim2Point);

        allPath[6][3].add(0, new Point(11.2,-8.6,4.9));
        allPath[6][3].add(1, new Point(10.71,-7.7,4.9));
        allPath[6][3].add(2, Aim3Point);

        allPath[6][4].add(0, new Point(11.2,-8.6,5.3393));
        allPath[6][4].add(1, new Point(10.75,-7.5,5.3393));
        allPath[6][4].add(2, Aim4Point);

        allPath[6][5].add(0, new Point(11.2,-8.6,5.3393));
        allPath[6][5].add(1, Aim5Point);

        allPath[6][7].add(0, pointQR);

        allPath[6][8].add(0, new Point(11.2,-8.6,4.9));
        allPath[6][8].add(1, new Point(11.15,-7.5,4.9));
        allPath[6][8].add(2, pointGoal);

        allPath[7][1].add(0, new Point(11.2764,-9.45,5.2988));
        allPath[7][1].add(1, Aim1Point);

        allPath[7][2].add(0, new Point(11.2,-8.6,4.9));
        allPath[7][2].add(1, new Point(10.612,-9.0709,4.9));
        allPath[7][2].add(2, Aim2Point);

        allPath[7][3].add(0, new Point(11.2,-8.552,4.9));
        allPath[7][3].add(1, new Point(10.71,-7.7,4.9));
        allPath[7][3].add(2, Aim3Point);

        allPath[7][4].add(0, new Point(11.2,-8.6,5.3393));
        allPath[7][4].add(1, new Point(10.75,-7.5,5.3393));
        allPath[7][4].add(2, Aim4Point);

        allPath[7][5].add(0, new Point(11.2,-8.6,5.3393));
        allPath[7][5].add(1, Aim5Point);

        allPath[7][6].add(0, Aim6Point);

        allPath[7][8].add(0, new Point(11.2,-8.6,4.9));
        allPath[7][8].add(1, new Point(11.15,-7.5,4.9));
        allPath[7][8].add(2, pointGoal);
    }

    public List<Point> getPath(int start, int end){
        return allPath[start][end];
    }

    public double getPathLength(int start, int end){
        double pathLength =0;
        Point startPoint = new Point();
        switch (start){
            case 0:
                startPoint = pointStart;
                break;
            case 1:
                startPoint = point1;
                break;
            case 2:
                startPoint = point2 ;
                break;
            case 3:
                 startPoint  = point3 ;
                break;
            case 4:
                 startPoint  = point4 ;
                break;
            case 5:
                 startPoint  = point5 ;
                break;
            case 6:
                 startPoint  = point6 ;
                break;
            case 7:
                 startPoint  = pointQR ;
                break;
            case 8:
                 startPoint  = pointGoal ;
                break;
        }

        Point lastPoint = startPoint;
        for(Point p: allPath[start][end]){
            pathLength += pointDistance(lastPoint, p);
            lastPoint = p;
        }

        return pathLength;
    }

    private static double pointDistance(Point point1, Point point2){
        double dx = point2.getX()-point1.getX();
        double dy = point2.getY()-point1.getY();
        double dz = point2.getZ()-point1.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
