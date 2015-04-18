package com.ineptus.dayline.containers;


import com.ineptus.dayline.Contour;

import java.util.ArrayList;

public class LabelsCluster {

    private ArrayList<Label> list;
    private Contour c;

    private int top;
    private int bottom;
    private boolean reachedTop;
    private boolean reachedBottom;

    public LabelsCluster(Contour contour, Label label) {
        c = contour;
        list = new ArrayList<>();
        top = label.originY - c.labelHalfHeight;
        bottom = label.originY + c.labelHalfHeight;
        reachedTop = label.reachedTop;
        reachedBottom = label.reachedBottom;
    }

    public void add(Label label) {
        list.add(label);
        if(reachedTop){
            bottom += c.labelHeight;
        } else if(reachedBottom) {
            top += c.labelHeight;
        } else if(label.reachedBottom) {
            bottom = c.axisBottom;
            top = bottom-list.size()*c.labelHeight;
        } else {

        }
    }

    public ArrayList<Label> getContent() {
        return list;
    }

    public int size() {
        return list.size();
    }



}
