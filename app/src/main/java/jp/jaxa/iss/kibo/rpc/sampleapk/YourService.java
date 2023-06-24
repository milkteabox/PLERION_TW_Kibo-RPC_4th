package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.util.List;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    private double[][] navCamIntrinsics;
    private int nowPoint = 0;
    private String Qr_Data = "ASTROBEE";
    private boolean QrScaned = false;
    @Override
    protected void runPlan1(){
        api.startMission();
        navCamIntrinsics = api.getNavCamIntrinsics();

        Thread threadQR = new Thread(new multiThreadAirQR());
        threadQR.start();

        moveAndHit();

        goToEnd();
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

    private void followPath (List<Point> path, Quaternion quaternion){
        for(Point p : path) {
            Point currentPOS = api.getRobotKinematics().getPosition();
            while (Math.abs(currentPOS.getX() - p.getX()) > 0.3535||
                    Math.abs(currentPOS.getY() - p.getY()) > 0.3535||
                    Math.abs(currentPOS.getZ() - p.getZ()) > 0.3535) {
                api.moveTo(p, quaternion, false);
                currentPOS = api.getRobotKinematics().getPosition();
            }
        }

        int lastIndex = path.size() - 1;
        Point lastPoint = path.get(lastIndex);

        moveToWithRetry(lastPoint, quaternion, 15);
    }

    private void followNumPath (int start, int target){
        Quaternion targetQuaternion = new Quaternion();
        switch (target){
            case 1:
                targetQuaternion = point1Quaternion;
                break;
            case 2:
                targetQuaternion = point2Quaternion;
                break;
            case 3:
                targetQuaternion = point3Quaternion;
                break;
            case 4:
                targetQuaternion = point4Quaternion;
                break;
            case 5:
                targetQuaternion = point5Quaternion;
                break;
            case 6:
                targetQuaternion = point6Quaternion;
                break;
            case 7:
                targetQuaternion = pointQRQuaternion;
                break;
            case 8:
                targetQuaternion = pointGoalQuaternion;
                break;
        }
        List path = getPath(start, target);

        followPath(path, targetQuaternion);
    }

    private List<Integer> getShortestTarget() {
        List<Integer> activeTargets = api.getActiveTargets();

        if(activeTargets.size() != 1){
            PathMap pathMap = new PathMap();
            int t1 = activeTargets.get(0);
            int t2 = activeTargets.get(1);

            if(pathMap.getPathLength(nowPoint, t1) + pathMap.getPathLength(t1, t2)
                    > pathMap.getPathLength(nowPoint, t2) + pathMap.getPathLength(t2, t1)){
                activeTargets.set(0, t2);
                activeTargets.set(1, t1);
            }
        }
        return activeTargets;
    }

    private void goToEnd() {
        Quaternion moveQuaternion = pointGoalQuaternion;
        if(!QrScaned){moveQuaternion = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f); }
        api.relativeMoveTo(new Point(0, 0, 0), moveQuaternion, false);

        for(Point p : getPath(nowPoint, 8)) {
            Point currentPOS = api.getRobotKinematics().getPosition();
            while (Math.abs(currentPOS.getX() - p.getX()) > 0.3535||
                    Math.abs(currentPOS.getY() - p.getY()) > 0.3535||
                    Math.abs(currentPOS.getZ() - p.getZ()) > 0.3535) {
                api.moveTo(p, moveQuaternion, false);
                currentPOS = api.getRobotKinematics().getPosition();
            }
        }

        moveToWithRetry(pointGoal, pointGoalQuaternion, 15);
    }

    private void moveAndHit (){
        for (int i = 0; i<6; i++){
            boolean secondTimeSkip = false;
            for(Integer p : getShortestTarget()){
                if(!isTimeAllow(nowPoint, p)) {
                    if(secondTimeSkip){ return;}
                    secondTimeSkip = true;
                    break;
                }
                long startTime = getMissionRemainingTime();
                Log.i("moveTimeCost", nowPoint + "->" + p + "  START");
                followNumPath(nowPoint, p);
                api.laserControl(true);
                api.takeTargetSnapshot(p);
                Log.i("moveTimeCost", nowPoint + "->" + p + "  ARRIVE and HIT DOWN" + "  Time:  " + (startTime - getMissionRemainingTime()) );
                nowPoint = p;
            }
        }
    }

    private Long getMissionRemainingTime (){
        Long missionRemainingTime = 0L;
        boolean success = false;

        while (!success) {
            try {
                missionRemainingTime = api.getTimeRemaining().get(1);
                success = true;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                sleep(300);
            }
        }

        return missionRemainingTime;
    }

    private Mat getCalibratedImage() {
        Mat originalImage = api.getMatNavCam();

        double[] camDoubleMatrix = navCamIntrinsics[0];
        double[] distortionCoefficientsDoubleMatrix = navCamIntrinsics[1];

        Mat cameraMatrix = new Mat(3, 3 , CvType.CV_64F);

        cameraMatrix.put(0,0, camDoubleMatrix[0]);
        cameraMatrix.put(0,1, camDoubleMatrix[1]);
        cameraMatrix.put(0,2, camDoubleMatrix[2]);
        cameraMatrix.put(1,0, camDoubleMatrix[3]);
        cameraMatrix.put(1,1, camDoubleMatrix[4]);
        cameraMatrix.put(1,2, camDoubleMatrix[5]);
        cameraMatrix.put(2,0, camDoubleMatrix[6]);
        cameraMatrix.put(2,1, camDoubleMatrix[7]);
        cameraMatrix.put(2,2, camDoubleMatrix[8]);

        Mat distortionCoefficients = new Mat(1 , 5 , CvType.CV_64F);

        distortionCoefficients.put(0,0, distortionCoefficientsDoubleMatrix[0]);
        distortionCoefficients.put(0,1, distortionCoefficientsDoubleMatrix[1]);
        distortionCoefficients.put(0,2, distortionCoefficientsDoubleMatrix[2]);
        distortionCoefficients.put(0,3, distortionCoefficientsDoubleMatrix[3]);
        distortionCoefficients.put(0,4, distortionCoefficientsDoubleMatrix[4]);


        Mat calibrateImaged = new Mat();


        Imgproc.undistort(
                originalImage,
                calibrateImaged,
                cameraMatrix,
                distortionCoefficients
        );

        return calibrateImaged;
    }

    private void scanQRCode(boolean saveImage){
        Log.i("QR", "Start ScanQr");
        Log.i("QR", api.getRobotKinematics().getPosition().toString());
        api.flashlightControlBack(0.05f);
        sleep(5000);
        Mat QRimage_Mat = getCalibratedImage();
        api.flashlightControlBack(0.0f);

        Mat QRimage_resizeMat = getCalibratedImage();
        Size newSize = new Size(QRimage_Mat.cols() * 2.75, QRimage_Mat.rows() * 2.75);

        Imgproc.resize(QRimage_Mat, QRimage_resizeMat, newSize, 0, 0, Imgproc.INTER_CUBIC);

        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        Qr_Data = qrCodeDetector.detectAndDecode(QRimage_resizeMat);

        Log.i("QR", "QR result: "+ Qr_Data);

        if(saveImage){ api.saveMatImage(QRimage_Mat, "QRcode_Image.mat"); }

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

    private static double pointDistance(Point point1, Point point2){
        double dx = point2.getX()-point1.getX();
        double dy = point2.getY()-point1.getY();
        double dz = point2.getZ()-point1.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    private static List<Point> getPath(int start, int end){
        PathMap pathMap = new PathMap();
        return pathMap.getPath(start, end);
    }

    private boolean isTimeAllow(int nowPoint, int target){
        PathMap pathMap = new PathMap();

        if (nowPoint == 0){return true;}

        if((getMissionRemainingTime() - 10000) < (pathMap.getPathTime(nowPoint, target) + pathMap.getPathTime(target, 8))){
            return false;
        }
        return true;
    }

    class multiThreadAirQR implements Runnable {
        Mat navCameraMatrix = new Mat(3, 3 , CvType.CV_64F);
        Mat navDistortionCoefficients = new Mat(1 , 5 , CvType.CV_64F);
        Mat dockCameraMatrix = new Mat(3, 3 , CvType.CV_64F);
        Mat dockDistortionCoefficients = new Mat(1 , 5 , CvType.CV_64F);

        @Override
        public void run() {
            getIntrinsics();
            while (!Thread.currentThread().isInterrupted()) {
                decodeQRCode(getDockCamCalibrateMat());
                decodeQRCode(getNavCamCalibrateMat());

                if (QrScaned) {
                    Log.i("QR", "QR result: "+ Qr_Data);
                    Thread.currentThread().interrupt();
                }
            }
        }


        private void decodeQRCode(Mat image){
            Size newSize = new Size(image.cols() * 3.25, image.rows() * 3.25);

            Imgproc.resize(image, image, newSize, 0, 0, Imgproc.INTER_CUBIC);

            QRCodeDetector qrCodeDetector = new QRCodeDetector();
            String data = qrCodeDetector.detectAndDecode(image);

            if(data.equals("JEM") || data.equals("COLUMBUS") || data.equals("RACK1") || data.equals("ASTROBEE") || data.equals("INTBALL") || data.equals("BLANK")){
                Qr_Data = data;
                QrScaned = true;
            }
        }

        private Mat getNavCamCalibrateMat(){
            Mat originalImage = api.getMatNavCam();

            Mat calibrateImaged = new Mat();

            Imgproc.undistort(
                    originalImage,
                    calibrateImaged,
                    navCameraMatrix,
                    navDistortionCoefficients
            );
            return calibrateImaged;
        }

        private Mat getDockCamCalibrateMat(){
            Mat originalImage = api.getMatDockCam();

            Mat calibrateImaged = new Mat();

            Imgproc.undistort(
                    originalImage,
                    calibrateImaged,
                    dockCameraMatrix,
                    dockDistortionCoefficients
            );
            return calibrateImaged;
        }

        private void getIntrinsics(){
            double[] navCamDoubleMatrix = api.getNavCamIntrinsics()[0];
            double[] navDistortionCoefficientsDoubleMatrix = api.getNavCamIntrinsics()[1];

            navCameraMatrix.put(0,0, navCamDoubleMatrix[0]);
            navCameraMatrix.put(0,1, navCamDoubleMatrix[1]);
            navCameraMatrix.put(0,2, navCamDoubleMatrix[2]);
            navCameraMatrix.put(1,0, navCamDoubleMatrix[3]);
            navCameraMatrix.put(1,1, navCamDoubleMatrix[4]);
            navCameraMatrix.put(1,2, navCamDoubleMatrix[5]);
            navCameraMatrix.put(2,0, navCamDoubleMatrix[6]);
            navCameraMatrix.put(2,1, navCamDoubleMatrix[7]);
            navCameraMatrix.put(2,2, navCamDoubleMatrix[8]);

            navDistortionCoefficients.put(0,0, navDistortionCoefficientsDoubleMatrix[0]);
            navDistortionCoefficients.put(0,1, navDistortionCoefficientsDoubleMatrix[1]);
            navDistortionCoefficients.put(0,2, navDistortionCoefficientsDoubleMatrix[2]);
            navDistortionCoefficients.put(0,3, navDistortionCoefficientsDoubleMatrix[3]);
            navDistortionCoefficients.put(0,4, navDistortionCoefficientsDoubleMatrix[4]);

            double[] dockCamDoubleMatrix = api.getDockCamIntrinsics()[0];
            double[] dockDistortionCoefficientsDoubleMatrix = api.getDockCamIntrinsics()[1];

            dockCameraMatrix.put(0,0, dockCamDoubleMatrix[0]);
            dockCameraMatrix.put(0,1, dockCamDoubleMatrix[1]);
            dockCameraMatrix.put(0,2, dockCamDoubleMatrix[2]);
            dockCameraMatrix.put(1,0, dockCamDoubleMatrix[3]);
            dockCameraMatrix.put(1,1, dockCamDoubleMatrix[4]);
            dockCameraMatrix.put(1,2, dockCamDoubleMatrix[5]);
            dockCameraMatrix.put(2,0, dockCamDoubleMatrix[6]);
            dockCameraMatrix.put(2,1, dockCamDoubleMatrix[7]);
            dockCameraMatrix.put(2,2, dockCamDoubleMatrix[8]);

            dockDistortionCoefficients.put(0,0, dockDistortionCoefficientsDoubleMatrix[0]);
            dockDistortionCoefficients.put(0,1, dockDistortionCoefficientsDoubleMatrix[1]);
            dockDistortionCoefficients.put(0,2, dockDistortionCoefficientsDoubleMatrix[2]);
            dockDistortionCoefficients.put(0,3, dockDistortionCoefficientsDoubleMatrix[3]);
            dockDistortionCoefficients.put(0,4, dockDistortionCoefficientsDoubleMatrix[4]);
        }

    }
}
