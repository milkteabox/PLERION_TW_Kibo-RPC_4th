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
    private static final double onetStep_distance = pointDistance(new Point(0, 0, 0),new Point(node_distance, 0, 0));
    private static final double twotStep_distance = pointDistance(new Point(0, 0, 0),new Point(node_distance, node_distance, 0));
    private static final double threetStep_distance = pointDistance(new Point(0, 0, 0),new Point(node_distance, node_distance, node_distance));


    public static List<Point> PathSearch(Point astrobeePoint, Point targetPoint){
        //Log.i("PathSearch", "START SEARCH");
        Node astrobeeNode = new Node(astrobeePoint);
        astrobeeNode.parent = null;

        PriorityQueue<Node> openList = new PriorityQueue<Node>(new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return Double.compare(n1.f, n2.f);
            }
        });

        Set<Node> closedSet = new HashSet<>();
        Map<Node, Double> gScore = new HashMap<>();

        gScore.put(astrobeeNode, 0.0);
        astrobeeNode.f = pointDistance(astrobeePoint, targetPoint);
        openList.offer(astrobeeNode);
        while (!openList.isEmpty()){
            Node current = openList.poll();
            if(closedSet.contains(current)){ continue; }

            closedSet.add(current);

            if (pointDistance(current.point, targetPoint) <= pathSearch_endDistance){
                return reconstructPath(current);
            }

            for (Node neighbor : getNeighbors(current)){
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                double tentativeGScore = gScore.get(current) + neighbor.neighborG;
                if(!openList.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    neighbor.parent = current;
                    gScore.put(neighbor, tentativeGScore);
                    neighbor.h = pointDistance(neighbor.point, targetPoint);
                    neighbor.f = tentativeGScore + neighbor.h * pathSearch_HWeight;
                    if(!openList.contains(neighbor)) {
                        openList.offer(neighbor);
                    }
                } else { closedSet.add(neighbor); }
            }
        }
        return null;
    }

    private static List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        Point nodePoint = node.point;
        double dX = nodePoint.getX();
        double dY = nodePoint.getY();
        double dZ = nodePoint.getZ();
        for (int i = -1; i<=1; i++){
            for (int j = -1; j<=1; j++){
                for (int k = -1; k<=1; k++){
                    if((i==0) && (j==0) && (k==0)){
                        continue;
                    }
                    Point point = new Point(
                            dX + i * node_distance,
                            dY + j * node_distance,
                            dZ + k * node_distance);
                    if(isNeighborAllow(point)){
                        Node nodeNew = new Node(point);
                        switch (Math.abs(i)+Math.abs(j)+Math.abs(k)){
                            case 1:
                                nodeNew.neighborG = onetStep_distance;
                                break;
                            case 2:
                                nodeNew.neighborG = twotStep_distance;
                                break;
                            case 3:
                                nodeNew.neighborG = threetStep_distance;
                                break;
                        }
                        neighbors.add(nodeNew);
                    }
                }
            }
        }
        return neighbors;
    }

//    private static List<Point> reconstructPath(Node node) {
//        List<Point> path = new ArrayList<>();
//        while (node != null) {
//            path.add(0, node.point);
//            node = node.parent;
//        }
//        return path;
//    }

    private static List<Point> reconstructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node.parent != null) {
            Point prevPoint = node.parent.point;
            Point curPoint = node.point;
            Point nextPoint = node.parent.parent != null ? node.parent.parent.point : null;
            if (nextPoint == null || !isCollinear(prevPoint, curPoint, nextPoint)) {
                path.add(curPoint);
            }
            node = node.parent;
        }
        path.add(node.point);
        Collections.reverse(path);
        return path;
    }

    private static boolean isCollinear(Point a, Point b, Point c) {
        double abDist = pointDistance(a, b);
        double bcDist = pointDistance(b, c);
        double acDist = pointDistance(a, c);
        return Math.abs(abDist + bcDist - acDist) == 0.0;
    }


    private static boolean isNeighborAllow(Point point) {
        return ((isInKIZZone(point, pointKIZmin_1, pointKIZmax_1)||isInKIZZone(point, pointKIZmin_2, pointKIZmax_2))
                && !isInKOZ(point));
    }

    private static boolean isInKOZ(Point point) {
        return isInKOZZone(point, pointKOZmin_1, pointKOZmax_1) ||
                isInKOZZone(point, pointKOZmin_2, pointKOZmax_2) ||
                isInKOZZone(point, pointKOZmin_3, pointKOZmax_3) ||
                isInKOZZone(point, pointKOZmin_4, pointKOZmax_4) ||
                isInKOZZone(point, pointKOZmin_5, pointKOZmax_5) ;
    }

    private static boolean isInKOZZone(Point point, Point zonePointMin, Point zonePointMax){
        return point.getX() >= zonePointMin.getX()-astroobee_KOZKeepSpace && point.getX() <= zonePointMax.getX()+astroobee_KOZKeepSpace &&
                point.getY() >= zonePointMin.getY()-astroobee_KOZKeepSpace && point.getY() <= zonePointMax.getY()+astroobee_KOZKeepSpace &&
                point.getZ() >= zonePointMin.getZ()-astroobee_KOZKeepSpace && point.getZ() <= zonePointMax.getZ()+astroobee_KOZKeepSpace;
    }

    private static boolean isInKIZZone(Point point, Point zonePointMin, Point zonePointMax){
        return point.getX() >= zonePointMin.getX()+astroobee_KOZKeepSpace && point.getX() <= zonePointMax.getX()-astroobee_KOZKeepSpace &&
                point.getY() >= zonePointMin.getY()+astroobee_KOZKeepSpace && point.getY() <= zonePointMax.getY()-astroobee_KOZKeepSpace &&
                point.getZ() >= zonePointMin.getZ()+astroobee_KOZKeepSpace && point.getZ() <= zonePointMax.getZ()-astroobee_KOZKeepSpace;
    }

    private static double pointDistance(Point point1, Point point2){
        double dx = point2.getX()-point1.getX();
        double dy = point2.getY()-point1.getY();
        double dz = point2.getZ()-point1.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
