package jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch;

import gov.nasa.arc.astrobee.types.Point;

public class Node {
    private final Point point;
    private  Node previousNode;
    private double distance;

    public Node(Point point, double distance) {
        this.point = point;
        this.distance = distance;
        this.previousNode = null;
    }

    public Node(Point point, double distance, Node previousNode) {
        this.point = point;
        this.distance = distance;
        this.previousNode = previousNode;
    }

    public Point getPoint() {
        return point;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

