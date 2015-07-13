package com.ineptus.dayline.containers;


import com.ineptus.dayline.Contour;
import com.ineptus.dayline.tools.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PeculiarLabelsManager {

    Contour c;

    private ArrayList<PeculiarLabel> labels = new ArrayList<>();

    private int bottom;

    private PeculiarLabel first;
    private PeculiarLabel last;


    /////////////////
    // CONSTRUCTOR
    //////////////

    public PeculiarLabelsManager(Contour contour) {
        c = contour;
    }

    public void addFromEvent(Event event, int priority) {
        if(event.title == null || event.title.equals("")) {
            return;
        }

        PeculiarLabel label = new PeculiarLabel(c, event, c.getEventCenterY(event), priority);

        labels.add(label);
    }

    public void addCustom(String text, int color, int y, int priority) {
        PeculiarLabel label = new PeculiarLabel(c, text, color, y, priority);

        labels.add(label);


    }

    ///////////////////////
    // PUBLIC TRIGGER

    public void fit() {

        //Sort labels by location
        sort();

        if(labels.size()>0) {

            //Put first label
            putFirstLabel(labels.get(0));

            //..and the others
            for(int i = 1; i<labels.size(); i++) {
                putLabel(labels.get(i));
            }
            PeculiarLabel temp = first;
            while(temp != last){
                Logger.log("CHECK Y", 1, "Y of " + temp.text + ": " + temp.y);
                temp = temp.next;
            }
        }

    }

    /////////////////
    // CORE
    /////////////////

    private void putLabel(PeculiarLabel label) {

        Logger.log("PUT LABEL", 0, "Putting " + label.text);

        //Check if label is doomed somehow
        //Safety check - for now at least
        if(label == null || label.steps > 10) {
            Logger.log("PUT LABEL", 0, "---10+--STEPS----\n  CRITICAL ERROR  \n-----------------");
            return;
        }
        label.steps++;

        //Check if label collides with top and if so - move it to top or below others
        if(aboveTop(label)) {
            solveAboveTop(label);
            return;
        }

        //Check if label collides with others, move it accordingly
        if(collides(label)) {
            if(!solveCollision(label)) {
                //Not solved so cancel this putting
                return;
            }
        }


        //Check if collides with bottom - try to move others up or remove the least important
        if(belowBottom(label)) {
            if(!solveBelowBottom(label)){
                return;
            }
        }


        //CHECK IF SHIFT WAS BIG ENOUGH AND REMOVE A LOW PRIORITY LABEL IF POSSIBLE
        int shiftCaliber = (label.y-label.getOriginY())/c.labelHeight;
        if(shiftCaliber >= 1) {
            if(label.getPriority() <= PeculiarLabel.PRIORITY_LOW) {
                //Current label is low priority - cancel putting
                return;
            }
            add(label);
            removeUnnecessaryLabelInCluster(last);
        } else {
            add(label);
        }

        Logger.log("PUT LABEL", 0, "FINISHED");
        PeculiarLabel temp = first;
        do {
            Logger.log("STATE REPORT", 5, temp.text + " Y: " + temp.y);
            temp = temp.next;
        } while (temp != null);

    }

    //Solve if ABOVE TOP
    private void solveAboveTop(PeculiarLabel label) {
        //It can't be first above top, so just place it after the last
        label.y = last.y + c.labelHeight;

        //Check if it doesn't hit the bottom now - cancel
        if(belowBottom(label)){
            return;
        }

        add(label);



        //TODO: (OPTIONAL) Check if it leaves its box and try to switch it with other
    }



    //Solve collision
    //Return true if solved
    private boolean solveCollision(PeculiarLabel label) {

        Logger.log("SOLVE COLLISION", 1, label.text);

        //Check how many labels are involved and what is above the cluster

        //Check how many labels are in clustered and which is on the top
        PeculiarLabel top = getTopInCluster(last);
        int clusterSize = getClusterSize(last);
        Logger.log("CLUSTER SIZE", 2, clusterSize+ " WITH TOP: " + top.text);


        //Calculate desired shift of label(s) above

        //Consider situation when collision is just partial
        int collision = last.y+c.labelHeight - label.y;
        if(collision > c.labelHeight) {
            collision = c.labelHeight;
        }
        int desiredShift = collision/2/ clusterSize;

        //Check what is above top

        //Check if top collides
        if(top.prev == null) {
            Logger.log("SOLVE COLLISION", 2, "ALL IN SAME CLUSTER");
            int distanceToTop = top.y - c.labelsTop;

            //There is no place on top, move only current label
            if(distanceToTop == 0) {
                label.y = last.y+c.labelHeight;
                if(belowBottom(label)){
                    return false;
                }
                return true;
            }

            //There is not enough place to the top - move cluster as much as possible
            if(distanceToTop < desiredShift) {
                desiredShift = distanceToTop;
            }

        } else {
            Logger.log("SOLVE COLLISION", 2, "THERE IS CLUSTER ABOVE CLUSTER");
            //top.prev != null, so check if there is enough space to it
            int distanceToPrev = top.y - top.prev.y - c.labelHeight;
            Logger.log("DISTANCE FROM " + top.text + " TO " + top.prev.text, 4, String.valueOf(distanceToPrev));

            //Not enough place - move as much as possible and repeat putting
            if(distanceToPrev < desiredShift) {
                last.moveClusterUp(distanceToPrev);
                Logger.log("SOLVE COLLISION", 3, "Moved cluster " + distanceToPrev);
                label.y = label.y + distanceToPrev*clusterSize;
                Logger.log("LABEL Y", 3, "Y of "+label.text+ ": "+label.y);
                putLabel(label);
                return false;
            }
        }

        last.moveClusterUp(desiredShift);
        Logger.log("LAST Y", 1, "Y of " + last.text + ": " + last.y);
        label.y = last.y + c.labelHeight;
        Logger.log("LABEL Y", 1, "Y of "+label.text+ ": "+label.y);

        return true;
    }


    private void removeUnnecessaryLabelInCluster(PeculiarLabel label) {
        if(label.getPriority() <= PeculiarLabel.PRIORITY_LOW) {
            Logger.log("PUT LABEL", 1, "Removing unnecessary: " + label.text);
            removeAndMoveAllBelowUp(label);
            return;
        }
        if(label.prev != null) {
            removeUnnecessaryLabelInCluster(label.prev);
        }
    }

    private void removeAndMoveAllBelowUp(PeculiarLabel label) {
        if(last == label) {
            last = label.prev;
            label.prev.next = null;
            return;
        }
        if(first == label) {
            first = label.next;
        }
        PeculiarLabel next = label.next;
        label.remove();
        if(next != null) {
            next.moveAllBelowUp(c.labelHeight);
        }

    }


    //Solve below bottom
    private boolean solveBelowBottom(PeculiarLabel label) {
        Logger.log("SOLVE BELOW BOTTOM", 1, label.text);
        int spaceOnTheBottom = c.labelsBottom - last.y;

        //Check if there is place on the bottom
        if(spaceOnTheBottom >= c.labelHeight) {
            //Just put on the bottom then
            label.y = c.labelsBottom;
            return true;
        }

        //Check if there is no space to move labels and cancel if so
        if(!findSpace(label, c.labelHeight-spaceOnTheBottom)) {
            return false;
        }

        //Set desired shift
        int desiredShift = c.labelHeight - spaceOnTheBottom;
        last.moveAboveUp(desiredShift);
        Logger.log("LAST Y", 2, "Y of " + last.text + ": " + last.y);
        label.y = c.labelsBottom;
        Logger.log("LABEL Y", 2, "Y of "+label.text+ ": "+label.y);
        return true;



    }


    //////////////////////////
    //Recursive cluster info

    private PeculiarLabel getTopInCluster(PeculiarLabel label) {
        if(label.prev == null) {
            return label;
        } else if(label.prev.y < label.y-c.labelHeight) {
            return label;
        } else {
            return getTopInCluster(label.prev);
        }
    }

    private int getClusterSize(PeculiarLabel label){
        if(label.prev == null) {
            return 1;
        } else if(label.prev.y < label.y-c.labelHeight) {
            return 1;
        } else {
            return 1+getClusterSize(label.prev);
        }
    }

    ////////////////////////////////////
    //Recursive check for space above
    private  boolean findSpace(PeculiarLabel label, int space) {

        if(label.prev == null) {
            return (label.y >= space);
        }

        if(label.y - label.prev.y - c.labelHeight >= space) {
            return true;
        } else {
            return findSpace(label.prev, space - (label.y-label.prev.y-c.labelHeight));
        }
    }

    ////////////////////////////////////
    //Special handling of FIRST LABEL
    private void putFirstLabel(PeculiarLabel label) {
        if(aboveTop(label)) {
            label.y = c.labelsTop;
            label.hitTop = true;
        } else if(belowBottom(label)) {
            label.y = c.labelsBottom;
        }
        first = label;
        last = label;
    }

    //////////////////////
    //Add label to list
    private void add(PeculiarLabel label) {
        last.next = label;
        label.prev = last;
        last = label;
    }



    private boolean aboveTop(PeculiarLabel label) {
        return label.y < c.labelsTop;
    }

    private boolean belowBottom(PeculiarLabel label) {
        return label.y > c.labelsBottom;
    }

    private boolean collides(PeculiarLabel label) {
        return label.y < last.y+c.labelHeight;
    }


    ////////////////////
    //ITERATOR

    PeculiarLabel current;

    public boolean hasNext() {
        if(current == null) {
            if(first == null) {
                return false;
            } else {
                return true;
            }
        }
        return current.next != null;
    }

    public PeculiarLabel next () {
        if(current == null) {
            current = first;
            return current;
        }
        current = current.next;
        return current;
    }



    //SORTING
    private void sort() {

        Collections.sort(labels, new Comparator<PeculiarLabel>() {

            public int compare(PeculiarLabel label_1, PeculiarLabel label_2) {
                return label_1.y - label_2.y;
            }
        });
    }


}
