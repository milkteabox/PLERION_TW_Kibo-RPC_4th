package jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch;


import java.util.List;

import gov.nasa.arc.astrobee.types.Point;

public class Node implements Comparable<Node> {
    public Point point;
    Node parent;
    double g, h, f;

    public Node(Point point){
        this.point = point;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f, other.f);
    }
}
