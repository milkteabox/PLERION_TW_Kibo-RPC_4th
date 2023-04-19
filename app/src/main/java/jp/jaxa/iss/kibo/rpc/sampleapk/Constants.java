package jp.jaxa.iss.kibo.rpc.sampleapk;

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

    public final static Point point7 = new Point(11.369d, -8.5518d, 4.48d);
    public final static Quaternion point7Quaternion = new Quaternion(0.0f, 0.707f, 0.0f, 0.707f);

    public final static Point pointQR = new Point(11.381944d, -8.566172d, 3.76203d);
    public final static Quaternion pointQRQuaternion = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);

    public final static Point pointGoal = new Point(11.143d, -6.7607d, 4.9654d);
    public final static Quaternion pointGoalQuaternion = new Quaternion(0.0f, 0.0f, -0.707f, 0.707f);


    public final static Point pointKIZmin_1 = new Point(10.3d, -10.2d, 4.32d);
    public final static Point pointKIZmax_1 = new Point(11.55f, -6.0f, 5.57f);

    public final static Point pointKIZmin_2 = new Point(9.5d, -10.5d, 4.02d);
    public final static Point pointKIZmax_2 = new Point(10.5f, -9.6f, 4.8f);


    public final static Point pointKOZmin_1 = new Point(10.783d, -9.8899d, 4.8385d);
    public final static Point pointKOZmax_1 = new Point(11.071f, -9.6929f, 5.0665f);

    public final static Point pointKOZmin_2 = new Point(10.8652d, -9.0734d, 4.3861d);
    public final static Point pointKOZmax_2 = new Point(10.9628f, -8.7314f, 4.6401f);

    public final static Point pointKOZmin_3 = new Point(10.185d, -8.3826d, 4.1475d);
    public final static Point pointKOZmax_3 = new Point(11.665f, -8.2826f, 4.6725f);

    public final static Point pointKOZmin_4 = new Point(10.7955d, -8.0635d, 5.1055d);
    public final static Point pointKOZmax_4 = new Point(11.3525f, -7.7305f, 5.1305f);

    public final static Point pointKOZmin_5 = new Point(10.563d, -7.1449d, 4.6544d);
    public final static Point pointKOZmax_5 = new Point(10.709f, -6.8099f, 4.8164f);

    public final static double node_distance = 0.075d;
    public final static double astroobee_KOZKeepSpace = 0.5;
    public final static double search_threshold = 2.5;
    public final static double pathSearch_endDistance = 0.5;
}
