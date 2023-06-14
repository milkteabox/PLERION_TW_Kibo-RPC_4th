package jp.jaxa.iss.kibo.rpc.sampleapk;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class Constants {
    public final static Point pointStart = new Point(9.815d, -9.806d, 4.293d);

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

    public final static Point Aim1Point = new Point(11.207, -9.92284, 5.474);
    public final static Point Aim2Point = new Point(10.456, -9.192, 4.48);
    public final static Point Aim3Point = new Point(10.713, -7.768, 4.48);
    public final static Point Aim4Point = new Point(10.51, -6.613, 5.207);
    public final static Point Aim5Point = new Point(11.046, -7.914, 5.3393);
    public final static Point Aim6Point = new Point(11.355, -8.155, 5.016);

    public final static List<Mat> boardArucoObjPoints(){
        List<Mat> boardArucoObjPoints = new ArrayList<>();

        MatOfPoint3f RUAruco = new MatOfPoint3f(
                new Point3(7.5, 6.25, 0),
                new Point3(12.5, 6.25, 0),
                new Point3(12.5, 1.25, 0),
                new Point3(7.5, 1.25, 0)
        );
        boardArucoObjPoints.add(RUAruco);

        MatOfPoint3f LUAruco = new MatOfPoint3f(
                new Point3(-12.5, 6.25, 0),
                new Point3(-7.5, 6.25, 0),
                new Point3(-7.5, 1.25, 0),
                new Point3(-12.5, 1.25, 0)
        );
        boardArucoObjPoints.add(LUAruco);

        MatOfPoint3f LDAruco = new MatOfPoint3f(
                new Point3(-12.5, -1.25, 0),
                new Point3(-7.5, -1.25, 0),
                new Point3(-7.5, -6.25, 0),
                new Point3(-12.5, -6.25, 0)
        );
        boardArucoObjPoints.add(LDAruco);

        MatOfPoint3f RDAruco = new MatOfPoint3f(
                new Point3(7.5, -1.25, 0),
                new Point3(12.5, -1.25, 0),
                new Point3(12.5, -6.25, 0),
                new Point3(7.5, -6.25, 0)
        );
        boardArucoObjPoints.add(RDAruco);

        return boardArucoObjPoints;
    }
}
