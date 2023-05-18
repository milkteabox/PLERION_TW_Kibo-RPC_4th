package jp.jaxa.iss.kibo.rpc.sampleapk;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class Constants {
    public final static Point point1 = new Point(11.2746d, -9.92284d, 5.2988d);
    public final static Quaternion point1Quaternion = new Quaternion(0.0f, 0.0f, -0.707f, 0.707f);

    public final static Point point2 = new Point(10.612d, -9.0709d, 4.48d);
    public final static Quaternion point2Quaternion = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f);

    public final static Point point3 = new Point(10.71d, -7.7d, 4.48d);
    public final static Quaternion point3Quaternion = new Quaternion(0.0f, 0.707f, 0.0f, 0.707f);

    public final static Point point4 = new Point(10.51d, -6.7185d, 5.1804d);
    public final static Quaternion point4Quaternion = new Quaternion(0.0f, 0.0f, -1.0f, 0.0f);

    public final static Point point5 = new Point(11.114d, -7.9756d, 5.3393d);
    public final static Quaternion point5Quaternion = new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f);

    public final static Point point6 = new Point(11.355d, -8.9929d, 4.7818d);
    public final static Quaternion point6Quaternion = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);

    public final static Point pointQR = new Point(11.369d, -8.5518d, 4.48d);
    public final static Quaternion pointQRQuaternion = new Quaternion(0.0f, 0.707f, 0.0f, 0.707f);

    public final static Point pointGoal = new Point(11.381944d, -8.566172d, 4.48d);
    public final static Quaternion pointGoalQuaternion = new Quaternion(0.0f, 0.0f, -0.707f, 0.707f);


    public final static Point pointKIZmin_1 = new Point(10.3d, -10.2d, 4.32d);
    public final static Point pointKIZmax_1 = new Point(11.55d, -6.0d, 5.57d);

    public final static Point pointKIZmin_2 = new Point(9.5d, -10.5d, 4.02d);
    public final static Point pointKIZmax_2 = new Point(10.5d, -9.6d, 4.8d);


    public final static Point pointKOZmin_1 = new Point(10.783d, -9.8899d, 4.8385d);
    public final static Point pointKOZmax_1 = new Point(11.071d, -9.6929d, 5.0665d);

    public final static Point pointKOZmin_2 = new Point(10.8652d, -9.0734d, 4.3861d);
    public final static Point pointKOZmax_2 = new Point(10.9628d, -8.7314d, 4.6401d);

    public final static Point pointKOZmin_3 = new Point(10.185d, -8.3826d, 4.1475d);
    public final static Point pointKOZmax_3 = new Point(11.665d, -8.2826d, 4.6725d);

    public final static Point pointKOZmin_4 = new Point(10.7955d, -8.0635d, 5.1055d);
    public final static Point pointKOZmax_4 = new Point(11.3525d, -7.7305d, 5.1305d);

    public final static Point pointKOZmin_5 = new Point(10.563d, -7.1449d, 4.6544d);
    public final static Point pointKOZmax_5 = new Point(10.709d, -6.8099d, 4.8164d);

    public final static Point pointNav_point1Upper = new Point(11.2746d, -9.92284d, 4.735d);
    public final static Point pointNav_point1ClosedAFT = new Point(10.695d, -9.92284d, 5.2988d);
    public final static Point pointNav_point1ClosedAFTandSTBD = new Point(10.695d, -9.75d, 5.2988d);
    public final static Point pointNav_point1ClosedSTBD = new Point(11.2746d, -9.75d, 5.2988d);

    public final static Point pointNav_point2ClosedLock = new Point(10.695d, -9.275d, 5.2988d);
    public final static Point pointNav_point2Lower = new Point(10.612d, -9.0709d, 4.85d);

    public final static Point pointNav_point3Lower = new Point(10.71d, -7.7d, 4.85d);
    public final static Point pointNav_point3LowerL = new Point(10.71d, -7.7d, 5.3393d);

    public final static Point pointNav_point4Lower = new Point(10.51d, -6.7185d, 4.985d);
    public final static Point pointNav_point4ClosedLock = new Point(10.51d, -7.375d, 5.1804d);
    public final static Point pointNav_point4ClosedSTBD = new Point(10.51d, -6.615d, 5.1804d);

    public final static Point pointNav_point5UnderKOZ3 = new Point(11.114d, -8.3326d, 5.3393d);
    public final static Point pointNav_point5ClosedAFT = new Point(10.635d, -7.9756d, 5.3393d);
    public final static Point pointNav_point5ClosedSTBD = new Point(11.2746d, -7.575d, 5.2988d);

    public final static Point pointNav_between2andQR = new Point(11.295d, -9.0709d, 4.5);

    public final static Point pointNav_underKOZ3ClosedAFT = new Point(10.5d, -8.3326d, 4.889d);
    public final static Point pointNav_underKOZ3Middle = new Point(10.935d, -8.3326d, 4.889d);
    public final static Point pointNav_underKOZ3ClosedFWD = new Point(11.3d, -8.3326d, 4.889d);

    public final static Point pointNav_pointQRLower = new Point(11.369d, -8.5518d, 4.85d);

    public final static List<Point> allNavPoints(){
        List<Point> allNavPoints = new ArrayList<>();

        allNavPoints.add(pointNav_point1Upper);
        allNavPoints.add(pointNav_point1ClosedAFT);
        allNavPoints.add(pointNav_point1ClosedAFTandSTBD);
        allNavPoints.add(pointNav_point1ClosedSTBD);
        allNavPoints.add(pointNav_point2ClosedLock);
        allNavPoints.add(pointNav_point2Lower);
        allNavPoints.add(pointNav_point3Lower);
        allNavPoints.add(pointNav_point3LowerL);
        allNavPoints.add(pointNav_point4Lower);
        allNavPoints.add(pointNav_point4ClosedLock);
        allNavPoints.add(pointNav_point4ClosedSTBD);
        allNavPoints.add(pointNav_point5UnderKOZ3);
        allNavPoints.add(pointNav_point5ClosedAFT);
        allNavPoints.add(pointNav_point5ClosedSTBD);
        allNavPoints.add(pointNav_between2andQR);
        allNavPoints.add(pointNav_underKOZ3ClosedAFT);
        allNavPoints.add(pointNav_underKOZ3Middle);
        allNavPoints.add(pointNav_underKOZ3ClosedFWD);
        allNavPoints.add(pointNav_pointQRLower);



        return allNavPoints;
    }

    public final static List<Point> allTargetPoints(){
        List<Point> allTargetPoints = new ArrayList<>();

        allTargetPoints.add(point1);
        allTargetPoints.add(point2);
        allTargetPoints.add(point3);
        allTargetPoints.add(point4);
        allTargetPoints.add(point5);
        allTargetPoints.add(point6);
        allTargetPoints.add(pointQR);
        allTargetPoints.add(pointGoal);

        return allTargetPoints;
    }

    public final static List<Point> allPoints(){
        List<Point> allPoints = new ArrayList<>();
        allPoints.addAll(allNavPoints());
        allPoints.addAll(allTargetPoints());
        return allPoints;
    }
}
