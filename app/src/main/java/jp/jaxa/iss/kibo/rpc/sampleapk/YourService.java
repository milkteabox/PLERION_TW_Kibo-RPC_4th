package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch.PathSearch;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;
import static jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch.PathSearch.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    private String Qr_Data;
    @Override
    protected void runPlan1(){
        api.startMission();
        followPath(PathSearch(api.getRobotKinematics().getPosition(), pointQR), pointQR, pointQRQuaternion);
        scanQRCode(true);
        followPath(PathSearch(api.getRobotKinematics().getPosition(), pointGoal), pointGoal, pointGoalQuaternion);
        missionEnd();
    }

    @Override
    protected void runPlan2(){
       // write your plan 2 here
    }

    @Override
    protected void runPlan3(){
        // write your plan 3 here
    }

    // You can add your method
    private String yourMethod(){
        return "your method";
    }

    private void followPath (List<Point> path, Point targetPoint, Quaternion quaternion){
        for(Point p : path) {
            Point currentPOS = api.getRobotKinematics().getPosition();
            while (Math.abs(currentPOS.getX() - p.getX()) > 0.075||
                    Math.abs(currentPOS.getY() - p.getY()) > 0.075||
                    Math.abs(currentPOS.getY() - p.getY()) > 0.075) {
                api.moveTo(p, quaternion, false);
                currentPOS = api.getRobotKinematics().getPosition();
            }
        }
    }

    private void getActiveTarget(){
        List<Integer> activeTargets = api.getActiveTargets();
    }

    private void scanQRCode(boolean saveImage){
        Log.i("QR", "Start ScanQr");
        api.flashlightControlBack(0.05f);
        sleep(1);
        Bitmap QRimage_Bitmap = api.getBitmapNavCam();
        api.flashlightControlBack(0.0f);

        Mat QRimage_Mat = new Mat();
        Utils.bitmapToMat(QRimage_Bitmap, QRimage_Mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        Qr_Data = qrCodeDetector.detectAndDecode(QRimage_Mat);

        Log.i("QR", Qr_Data);

        api.saveMatImage(QRimage_Mat, "QRcode_Image");
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void missionEnd(){
        String reportString;
        switch (Qr_Data){
            case "JEM":
                reportString = "STAY_AT_JEM";
                break;
            case "COLUMBUS":
                reportString = "GO_TO_COLUMBUS";
                break;
            case "RACK1":
                reportString = "CHECK_RACK_1";
                break;
            case "ASTROBEE":
                reportString = "I_AM_HERE";
                break;
            case "INTBALL":
                reportString = "LOOKING_FORWARD_TO_SEE_YOU";
                break;
            case "BLANK":
                reportString = "NO_PROBLEM";
                break;
            default:
                reportString = "I_AM_HERE";
        }
        api.reportMissionCompletion(reportString);
    }
    private List<PointData> getActiveTargetPointList(){
        List<Integer> targets = api.getActiveTargets();
        List<PointData> points = new ArrayList<>();
        for(int t : targets){
            switch (t){
                case 1:
                    points.add(new PointData(point1, point1Quaternion));
                    break;
                case 2:
                    points.add(new PointData(point2, point2Quaternion));
                    break;
                case 3:
                    points.add(new PointData(point3, point3Quaternion));
                    break;
                case 4:
                    points.add(new PointData(point4, point4Quaternion));
                    break;
                case 5:
                    points.add(new PointData(point5, point5Quaternion));
                    break;
                case 6:
                    points.add(new PointData(point6, point6Quaternion));
                    break;
                case 7:
                    points.add(new PointData(pointQR, pointQRQuaternion));
                    break;
            }
        }
        return points;
    }

    private boolean moveToWithRetry(Point point, Quaternion quaternion, int loopMAX_time) {
        Result result;
        final int LOOP_MAX = loopMAX_time;
        final float MAX_THRESHOLD = 0.02f;
        result = api.moveTo(point, quaternion, false);
        Quaternion currentQuaternion = api.getRobotKinematics().getOrientation();
        int loopCounter = 0;
        while ((Math.abs(currentQuaternion.getX() - quaternion.getX()) >= MAX_THRESHOLD
                || Math.abs(currentQuaternion.getY() - quaternion.getY()) >= MAX_THRESHOLD
                || Math.abs(currentQuaternion.getZ() - quaternion.getZ()) >= MAX_THRESHOLD
                || Math.abs(currentQuaternion.getW() - quaternion.getW()) >= MAX_THRESHOLD
                || !result.hasSucceeded())
                && loopCounter < LOOP_MAX) {
            result = api.moveTo(point, quaternion, false);
            ++loopCounter;
        }
        return result.hasSucceeded();
    }
}
