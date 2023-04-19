package jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch;


import android.util.Log;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import gov.nasa.arc.astrobee.types.Point;

import static jp.jaxa.iss.kibo.rpc.sampleapk.Constants.*;

public class PathSearch {

    public static List<Node> PathSearch(Point astrobeePoint, Point targetPoint){
        Log.i("PathSearch", "START SEARCH");
        Node astrobeeNode = new Node(astrobeePoint);

        PriorityQueue<Node> openList = new PriorityQueue<Node>(new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return Double.compare(n1.f, n2.f);
            }
        });

        Set<Node> closedSet = new HashSet<>();
        Map<Node, Double> gScore = new HashMap<>();

        gScore.put(astrobeeNode, 0.0);
        astrobeeNode.f = pointDistance(astrobeePoint, targetPoint);
        openList.add(astrobeeNode);
        while (!openList.isEmpty()){
            Node current = openList.poll();
            closedSet.add(current);
            Log.i("PathSearch", "Current node: " + current.point.toString());

            if (pointDistance(current.point, targetPoint) <= pathSearch_endDistance){
                return reconstructPath(current);
            }


            for (Node neighbor : getNeighbors(current)){
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                double tentativeGScore = gScore.get(current) + pointDistance(current.point, neighbor.point);
                if(!openList.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    neighbor.parent = current;
                    gScore.put(neighbor, tentativeGScore);
                    neighbor.h = pointDistance(neighbor.point, targetPoint);
                    neighbor.f = tentativeGScore + neighbor.h;
                    if(!openList.contains(neighbor)) {
                        openList.add(neighbor);
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
                        neighbors.add(new Node(point));
                    }
                }
            }
        }
        return neighbors;
    }

    private static List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node);
            node = node.parent;
        }
        return path;
    }

    private static boolean isNeighborAllow(Point point) {
        return ((isInZone(point, pointKIZmin_1, pointKIZmax_1)||isInZone(point, pointKIZmin_2, pointKIZmax_2))
                && !isInKOZ(point));
    }

    private static boolean isInKOZ(Point point) {
        return isInZone(point, pointKOZmin_1, pointKOZmax_1) ||
                isInZone(point, pointKOZmin_2, pointKOZmax_2) ||
                isInZone(point, pointKOZmin_3, pointKOZmax_3) ||
                isInZone(point, pointKOZmin_4, pointKOZmax_4) ||
                isInZone(point, pointKOZmin_5, pointKOZmax_5) ;
    }

    private static boolean isInZone(Point point, Point zonePointMin, Point zonePointMax){
        return point.getX() >= zonePointMin.getX() && point.getX() <= zonePointMax.getX() &&
                point.getY() >= zonePointMin.getY() && point.getY() <= zonePointMax.getY() &&
                point.getZ() >= zonePointMin.getZ() && point.getZ() <= zonePointMax.getZ();
    }

    private static double pointDistance(Point point1, Point point2){
        double dx = point2.getX()-point1.getX();
        double dy = point2.getY()-point1.getY();
        double dz = point2.getZ()-point1.getZ();
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
