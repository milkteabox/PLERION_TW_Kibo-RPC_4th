package jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import gov.nasa.arc.astrobee.types.Point;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

public class PathSearch {

    public static List<Point> PathSearch(Point astrobeePoint, Point targetPoint){


        if(isTwoLinePointAllow(astrobeePoint, targetPoint)){
            List<Point> path = new ArrayList<>();
            path.add(0, targetPoint);
            return path;
        }else {
            return findPath(astrobeePoint, targetPoint);
        }
    }

    private static List<Point> findPath(Point startPoint, Point targetPoint) {
        PriorityQueue<Node> queue = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                return Double.compare(node1.getDistance(), node2.getDistance());
            }
        });
        Set<Point> visited = new HashSet<>();

        Node startNode = new Node(startPoint, 0, null);
        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.getPoint().equals(targetPoint)) {
                return reconstructPath(currentNode);
            }

            visited.add(currentNode.getPoint());

            List<Point> neighbors = getCanStraightGoPoints(currentNode.getPoint());

            for (Point neighbor : neighbors) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                double distanceToNeighbor = pointDistance(currentNode.getPoint(), neighbor) + currentNode.getDistance();
                Node newNode = new Node(neighbor, distanceToNeighbor, currentNode);
                queue.offer(newNode);
            }
        }

        return new ArrayList<>(); // No path found
    }

    private static List<Point> reconstructPath(Node node) {
        List<Point> path = new ArrayList<>();
        Node currentNode = node;

        while (currentNode != null) {
            path.add(0, currentNode.getPoint());
            currentNode = currentNode.getPreviousNode();
        }

        return path;
    }
    private static List<Point> getCanStraightGoPoints(Point point){
        List<Point> canStraightGoPoints = new ArrayList<>();
        for(Point p : allPoints()){
            if(isTwoLinePointAllow(p, point)){
                canStraightGoPoints.add(p);
            };
        }
        return canStraightGoPoints;
    }

    private static boolean isTwoLinePointAllow(Point pointStart, Point pointEnd){
        if(
                isTwoPointLineInZone(pointStart, pointEnd, pointKOZmin_1, pointKOZmax_1)||
                        isTwoPointLineInZone(pointStart, pointEnd, pointKOZmin_2, pointKOZmax_2)||
                        isTwoPointLineInZone(pointStart, pointEnd, pointKOZmin_3, pointKOZmax_3)||
                        isTwoPointLineInZone(pointStart, pointEnd, pointKOZmin_4, pointKOZmax_4)||
                        isTwoPointLineInZone(pointStart, pointEnd, pointKOZmin_5, pointKOZmax_5)
        ){ return false; }

        return true;
    }

    private static boolean isTwoPointLineInZone(Point point1, Point point2, Point cubePoint1, Point cubePoint2) {
        double[] cubeMin = {Math.min(cubePoint1.getX(), cubePoint2.getX()), Math.min(cubePoint1.getY(), cubePoint2.getY()), Math.min(cubePoint1.getZ(), cubePoint2.getZ())};
        double[] cubeMax = {Math.max(cubePoint1.getX(), cubePoint2.getX()), Math.max(cubePoint1.getY(), cubePoint2.getY()), Math.max(cubePoint1.getZ(), cubePoint2.getZ())};

        double[] point1Coords = {point1.getX(), point1.getY(), point1.getZ()};
        double[] point2Coords = {point2.getX(), point2.getY(), point2.getZ()};

        return checkLineCubeIntersection(cubeMin, cubeMax, point1Coords, point2Coords);
    }

    public static boolean checkLineCubeIntersection(double[] cubeMin, double[] cubeMax, double[] point1, double[] point2) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 3; i++) {
            if (Math.abs(point2[i] - point1[i]) < 0.00001) {
                if (point1[i] < cubeMin[i] || point1[i] > cubeMax[i]) {
                    return false;
                }
            } else {
                double t1 = (cubeMin[i] - point1[i]) / (point2[i] - point1[i]);
                double t2 = (cubeMax[i] - point1[i]) / (point2[i] - point1[i]);

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tmin = Math.max(tmin, t1);
                tmax = Math.min(tmax, t2);

                if (tmin > tmax) {
                    return false;
                }
            }
        }
        return true;
    }
    private static double pointDistance(Point point1, Point point2){
        double dx = point2.getX()-point1.getX();
        double dy = point2.getY()-point1.getY();
        double dz = point2.getZ()-point1.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
