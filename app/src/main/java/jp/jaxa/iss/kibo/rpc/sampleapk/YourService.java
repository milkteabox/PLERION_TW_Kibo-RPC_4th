package jp.jaxa.iss.kibo.rpc.sampleapk;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Switch;

import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Board;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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
    private double[][] navCamIntrinsics;

    private String Qr_Data = "ASTROBEE";
    @Override
    protected void runPlan1(){
        api.startMission();
        navCamIntrinsics = api.getNavCamIntrinsics();
        moveToWithRetry(point2, point2Quaternion, 10);
        aimAndHitTarget(2);
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
            while (Math.abs(currentPOS.getX() - p.getX()) > 0.00735||
                    Math.abs(currentPOS.getY() - p.getY()) > 0.00735||
                    Math.abs(currentPOS.getY() - p.getY()) > 0.00735) {
                api.moveTo(p, quaternion, false);
                currentPOS = api.getRobotKinematics().getPosition();
            }
        }
    }

    private void getActiveTarget(){
        List<Integer> activeTargets = api.getActiveTargets();
    }

    private void aimAndHitTarget(int targetNum) {

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


        sleep(5000);

        Mat originalImage = api.getMatNavCam();
        Mat navCamMat = originalImage;

        Mat arucoDrawMat = navCamMat;

        List<Mat> arucoCorners = new ArrayList<>();
        Mat arucoIDs = new Mat();

        Aruco.detectMarkers(
                navCamMat,
                Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250),
                arucoCorners,
                arucoIDs
                );

        Aruco.drawDetectedMarkers(arucoDrawMat, arucoCorners, arucoIDs, new Scalar(0, 255, 0));

        double arucoPixelSize = 8;
        int arucoSideNum = 0;

        if(!arucoCorners.isEmpty()){
            for (int i = 0; i < arucoIDs.total(); i++) {
                Mat curentCornerPoints = arucoCorners.get(i);

                for (int j = 0; j < 4; j++) {
                    double[] point1 = curentCornerPoints.get(0, j);
                    double[] point2 = curentCornerPoints.get(0, (j + 1) % 4);

                    arucoPixelSize += getPixelDistance(point1, point2);
                    arucoSideNum++;
                }
            }
            arucoPixelSize = (arucoPixelSize / arucoSideNum) / 5;
        }

        Log.i("arucoPixelSize", "a : " + arucoPixelSize);

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

        int startingValue = (targetNum - 1) * 4 + 1;
        MatOfInt boardArucoIDs = new MatOfInt(startingValue, startingValue + 1, startingValue + 2, startingValue + 3);

        Board targetBoard = Board.create(boardArucoObjPoints, Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250), boardArucoIDs);

        Mat rvec = new Mat();
        Mat tvec = new Mat();

        Aruco.estimatePoseBoard(arucoCorners, arucoIDs, targetBoard, cameraMatrix, distortionCoefficients, rvec, tvec);

        MatOfPoint3f origin = new MatOfPoint3f(new Point3(0, 0, 0));

        MatOfDouble DoubleDistortionCoefficients = new MatOfDouble();
        distortionCoefficients.convertTo(DoubleDistortionCoefficients, CvType.CV_64F);

        MatOfPoint2f arucoCenterPoints = new MatOfPoint2f();
        Calib3d.projectPoints(origin, rvec, tvec, cameraMatrix, DoubleDistortionCoefficients, arucoCenterPoints);

        Imgproc.circle(arucoDrawMat, arucoCenterPoints.toArray()[0], 1, new Scalar(255, 255, 255),2);

        api.saveMatImage(arucoDrawMat, "Axis.mat");

        Log.i("Axis", tvec.dump());

        double arucoCenterX = arucoCenterPoints.toArray()[0].x;
        double arucoCenterY = arucoCenterPoints.toArray()[0].y;



        double tx = tvec.get(0, 0)[0] - 9.94;
        double ty = tvec.get(1, 0)[0] + 2.85;


//        Mat targetCutMat = navCamMat.submat(
//                (int) (arucoCenterX - 15 * arucoPixelSize), (int) (arucoCenterX + 15 * arucoPixelSize),
//                (int) (arucoCenterY - (21 * arucoPixelSize)), (int) (arucoCenterY + 21 * arucoPixelSize));
//        Imgproc.threshold(targetCutMat, targetCutMat, 70, 255, Imgproc.THRESH_BINARY);
//
//        Mat circles = new Mat();
//
//        Imgproc.HoughCircles(targetCutMat, circles, Imgproc.HOUGH_GRADIENT,1, 20, 25, 25,25);
//
//        for (int col = 0; col < circles.cols(); col++) {
//            double[] circle = circles.get(0, col);
//            Log.i("Circle", "X: " + circle[0] + " Y: " + circle[1] +"R: " + circle[2]);
//            Imgproc.circle(targetCutMat, arucoCenterPoints.toArray()[0], 1, new Scalar(255, 255, 255),2);
//        }
//        api.saveMatImage(targetCutMat, "Circle.mat");
//
//        double ty = arucoCenterY + (arucoCenterY - 21 * arucoPixelSize) + circles.get(0, 0)[1];
//        double tx = arucoCenterX + (arucoCenterX - 15 * arucoPixelSize) + circles.get(0, 0)[0];

        Log.i("Aim", "X:"+ tx + "  Y:" + ty);
        moveToWithRetry(new Point(point2.getX() + tx/100, point2.getY() + ty/100, point2.getZ()), point2Quaternion, 15);
        api.laserControl(true);
        api.takeTargetSnapshot(2);
    }

    private double getPixelDistance(double[] pixel1, double[] pixel2) {
        double dX = pixel2[0] - pixel1[0];
        double dY = pixel2[1] - pixel1[1];

        double distance = Math.sqrt(dX * dX + dY * dY);

        return distance;
    }


    private Mat getCalibratedImage() {
        double[] camDoubleMatrix = navCamIntrinsics[0];
        double[] distortionCoefficientsDoubleMatrix = navCamIntrinsics[1];

        Mat originalImage = api.getMatNavCam();

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
        sleep(1);
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

    private static double pointDistance(Point point1, Point point2){
        double dx = point2.getX()-point1.getX();
        double dy = point2.getY()-point1.getY();
        double dz = point2.getZ()-point1.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
