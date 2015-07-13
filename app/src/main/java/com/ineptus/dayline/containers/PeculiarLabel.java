package com.ineptus.dayline.containers;


import com.ineptus.dayline.Contour;

public class PeculiarLabel {

    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 2;

    private Contour c;

    public String text;
    private int originY;
    public int y;
    public int color;
    private LineBox box;
    private Event event;
    private int priority;

    public int steps = 0;

    public boolean hitTop = false;

    public PeculiarLabel prev;
    public PeculiarLabel next;



    /////////////////
    // CONSTRUCTORS
    //////////////

    //Create from event
    public PeculiarLabel(Contour contour, Event event, int y, int priority) {

        this.c = contour;

        this.event = event;
        this.text = event.title;
        this.originY = y;
        this.y = y;
        this.priority = priority;
        this.color = event.color;

    }

    //Create custom
    public PeculiarLabel(Contour contour, String text, int color, int y, int priority) {

        this.c = contour;

        this.text = text;
        this.originY = y;
        this.y = y;
        this.priority = priority;
        this.color = color;

    }

    /////////////////
    //Recursion


    public void moveClusterUp(int shift) {
        if(prev != null && (y-prev.y == c.labelHeight)) {
            prev.moveClusterUp(shift);
        }
        y = y-shift;
    }

    public void moveAllBelowUp(int shift) {
        if(next != null) {
            next.moveAllBelowUp(shift);
        }
        y = y-shift;
    }

    public void moveAboveUp(int shift){
        if(shift <= 0 || prev == null) {
            return;
        }

        prev.moveAboveUp(shift - (y - prev.y - c.labelHeight));
        y -= shift;
        return;
    }


    /////////////////
    //Forget this label

    public void remove() {
        if(prev != null) {
            prev.next = next;
        }
        if(next != null) {
            next.prev = prev;
        }
    }




    ////////////////////////
    //Getters and setters
    //////////////////////

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public LineBox getBox() {
        return box;
    }

    public void setBox(LineBox box) {
        this.box = box;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
