package com.ineptus.dayline.containers;


import com.ineptus.dayline.Contour;

import java.util.ArrayList;

public class ExperimentalLabelsManager {

    ArrayList<Label> list;
    Contour c;

    public ExperimentalLabelsManager(Contour contour) {
        list = new ArrayList();
        c = contour;
    }

    public void addFromEvent(Event event, int priority) {
        if(event.title == null || event.title.equals("")) {
            return;
        }

        Label label = new Label(event, c.getEventCenterY(event), priority);

        putLabel(label);
    }

    private void putLabel(Label label) {

        if(aboveTop(label)) {
            if(list.size() != 0) {
                label.touchAbove = list.get(list.size());
            }
            label.y = c.labelsTop + (label.countTouchingAbove() * c.labelHeight);
            label.reachedTop = true;
        }

        if(belowBottom(label)) {
            if(label.reachedTop) {
                Label worst = getWorstInCluster(label);
                if(worst == label) return;
                removeLabelAndMoveUp(worst);
            }
        }



    }


    private boolean aboveTop(Label label) {
        return label.y < c.labelsTop;
    }

    private boolean belowBottom(Label label) {
        return label.y > c.labelsBottom;
    }

    private ArrayList<Label> getClusterAbove(Label label) {
        ArrayList<Label> list = new ArrayList<>();
        label.populateClusterAbove(list);
        return list;
    }

    public Label getWorstInCluster(Label label) {
        ArrayList<Label> cluster = getClusterAbove(label);
        Label worst = label;
        for(Label other:cluster) {
            if (other.priority < worst.priority) {
                worst = other;
            }
        }
        return worst;
    }

    public void removeLabelAndMoveUp(Label removed) {
        list.remove(removed);
        if(removed.touchAbove != null) {
            removed.touchAbove.touchBelow = removed.touchBelow;
        }
        if(removed.touchBelow != null) {
            removed.touchBelow.touchAbove = removed.touchAbove;
            removed.touchBelow.moveUpClusterBelow(removed);
        }
    }




}
