package com.ineptus.dayline.containers;

import com.ineptus.dayline.Contour;

import java.util.ArrayList;

public class NewLabelsManager extends LabelsManager {

    private ArrayList<Label> list;
    private Contour c;

    public NewLabelsManager(Contour contour) {
        setList(new ArrayList<Label>());
        list = getList();
        c = contour;
    }


    @Override
    void addFromEvent(Event event, int priority) {
        if(event.title == null || event.title.equals("")) {
            return;
        }

        Label label = new Label(event, c.getEventCenterY(event), priority);

        putLabel(label);
    }

    @Override
    void addCustom(String text, int color, int y, int priority) {

    }


    private void putLabel(Label label) {

        if(label.reachedBottom && label.reachedTop) {
            return;
        }

        //CHECK IF POSSIBLE
        if(collidesWithOther(label)) {

        } else {

        }

    }

    private boolean collidesWithOther(Label label) {
        for(Label other:list) {
            if(areColliding(label, other)) {
                return true;
            }
        }
        return false;
    }

    private boolean areColliding(Label label, Label other) {
        return !(other.y >= label.y + c.textSize + c.textVertPadding || other.y <= label.y - c.textSize - c.textVertPadding);
    }



}


