package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.List;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;
import jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch.Node;
import jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch.PathSearch;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    private String Qr_Data;
    @Override
    protected void runPlan1(){
        api.startMission();
        Log.i("PathSearch", "TEST");
        List<Node> path = PathSearch.PathSearch(point4, pointQR);
        path.get(0);
        scanQRCode(true);
        moveToWithRetry(pointGoal, pointGoalQuaternion);
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

    private void getActiveTarget(){
        List<Integer> activeTargets = api.getActiveTargets();
    }
    private void scanQRCode(boolean saveImage){
        System.out.println("ScanQr");
        api.flashlightControlBack(0.05f);
        sleep(1);
        Bitmap QRimage_Bitmap = api.getBitmapNavCam();
        api.flashlightControlBack(0.0f);

        Mat QRimage_Mat = new Mat();
        Utils.bitmapToMat(QRimage_Bitmap, QRimage_Mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        Mat QR_Point = new Mat();
        Qr_Data = qrCodeDetector.detectAndDecode(QRimage_Mat, QR_Point);

        api.saveMatImage(QRimage_Mat, "QRcode_Image");

        if(!QR_Point.empty()&&saveImage){
            org.opencv.core.Point[] vertices = new org.opencv.core.Point[4];
            Scalar color = new Scalar(255, 0, 0);
            for (int i = 0; i < 4; i++) {
                vertices[i] = new org.opencv.core.Point(
                        QR_Point.get(0, i)[0],
                        QR_Point.get(0, i)[1]);

                Imgproc.circle(QRimage_Mat, vertices[i], 6, color);
            }
            
            Imgproc.line(QRimage_Mat, vertices[0], vertices[1], color, 3);
            Imgproc.line(QRimage_Mat, vertices[1], vertices[2], color, 3);
            Imgproc.line(QRimage_Mat, vertices[2], vertices[3], color, 3);
            Imgproc.line(QRimage_Mat, vertices[3], vertices[0], color, 3);

            api.saveMatImage(QRimage_Mat, "QRcode_Image");
        }
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

    private boolean moveToWithRetry(Point point, Quaternion quaternion) {
        Result result;
        final int LOOP_MAX = 10;
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
