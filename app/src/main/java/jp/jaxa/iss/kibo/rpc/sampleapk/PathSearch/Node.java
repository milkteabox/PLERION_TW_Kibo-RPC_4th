package jp.jaxa.iss.kibo.rpc.sampleapk.PathSearch;


import java.util.List;

import gov.nasa.arc.astrobee.types.Point;

public class Node  {
    public Point point;
    Node parent;
    double neighborG, h, f;

    public Node(Point point){
        this.point = point;
    }
}
