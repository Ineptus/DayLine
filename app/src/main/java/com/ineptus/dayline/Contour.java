package com.ineptus.dayline;

/**
 * Created by Kuba Radzimowski on 18/03/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.format.Time;

import com.ineptus.dayline.containers.BoxedLine;
import com.ineptus.dayline.containers.Event;
import com.ineptus.dayline.containers.EventsList;
import com.ineptus.dayline.containers.LineBox;
import com.ineptus.dayline.containers.SimpleLabelsManager;
import com.ineptus.dayline.tools.CalendarDrainer;
import com.ineptus.dayline.tools.Prefs;

public class Contour {

    //GENERAL
    public int widgetId;
    public Context context;
    public int version;

    public Bitmap bitmap;
    public Canvas canvas;


    //DIMENSIONS
    public int width;
    public int height;

    //USER SETTINGS
    public String[] chosenCalendars;
    public int range;
    public boolean use12hours;
    public boolean mirror;


    //AXIS
    public int axisWidth;
    public int axisHeight;

    public int axisX;
    public int marginTop = 15;
    public int marginBottom = 15;
    public int marginRight;

    public int eventWidth;

    //LABELS
    public int labelHeight;

    //CALENDAR & TIME
    public long start;
    public long end;

    public double pxPerMili;
    public double pxPerSecond;
    public double pxPerMinute;
    public double pxPerHour;
    public int textSize = 30;
    public int textVertSpace = 5;
    public int labelsMargin = 50;


    public Time now = new Time();

    //CONTAINERS
    public EventsList events;
    public BoxedLine line;
    public BoxedLine sleep;
    public SimpleLabelsManager labels;

    //BUILDING TEMPS
    public EventsList antiStriped = new EventsList();
    public EventsList usedEvents = new EventsList();


    public Contour(	Context context,
                       int widgetId,
                       int newWidth,
                       int newHeight) {

        this.context = context;
        this.widgetId = widgetId;
        this.width = newWidth;
        this.height = newHeight;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        //LOAD USER PREFS
        use12hours = Prefs.load(this, Prefs.USE12HOURS, false);
        range = Prefs.load(this, Prefs.RANGE, 12);
        mirror = Prefs.load(this, Prefs.MIRROR, false);

        //INIT CONTEXT DEPENDENT VALUES:
        if(mirror){
            axisX = dpToPx(40);
        } else {
            axisX = width - dpToPx(40);
        }
        eventWidth = dpToPx(24);
        axisWidth = dpToPx(7);

        axisHeight = height - marginTop - marginBottom;

        textSize = dpToPx(16);
        textVertSpace = dpToPx(3);
        labelsMargin = dpToPx(26);
        labelHeight = textSize + textVertSpace;

        //TIMES
        now.setToNow();

        start = now.toMillis(true);
        end = start + range*60*60*1000;

        pxPerMili = this.axisHeight/(range*60f*60d*1000f);
        pxPerSecond = this.axisHeight/(range*60f*60d);
        pxPerMinute = pxPerMili*60000;
        pxPerHour = pxPerMili*3600000;

        //INIT CONTAINERS:
        events = new EventsList();
        labels = new SimpleLabelsManager(this);

        //DRAIN CALENDAR WHEN EVERYTHING INITIATED
        CalendarDrainer.drain(this);

        //BUILD BOXED LINE
        line = new BoxedLine(this, events);
    }


    public int getEventCenterY(Event event) {

        int start = (int) ((event.start-now.toMillis(true))*pxPerMili);
        int duration = (int) (event.getDuration()*pxPerMili);

        return start+duration/2;
    }

    public float getEventStartY(Event event) {
        return (float) ((event.start-now.toMillis(true))*pxPerMili); }

    public float getEventEndY(Event event) {
        return (float) ((event.end-now.toMillis(true))*pxPerMili); }

    public float getBoxCenterY(LineBox box) {
        float start =  (float) (box.getRelStart()*pxPerMili);
        float duration = (float) (box.getDuration()*pxPerMili);

        return start+duration/2;
    }

    public float getEventBoxLeft() {
        return axisX-eventWidth/2;
    }

    public float getEventBoxRight() {
        return axisX+eventWidth/2;
    }

    public float getLabelsWidth() {
        if(mirror) {
            return width-axisX-labelsMargin;
        } else {
            return axisX-labelsMargin;
        }
    }

    public int getLabelsPosX() {
        if(mirror) {
            return axisX+labelsMargin;
        } else {
            return axisX-labelsMargin;
        }
    }



    //TOOLS:

    public int dpToPx(int dp) {
        int px = Math.round(dp * context.getResources().getDisplayMetrics().density);
        return px;
    }

    public int spToPx(int dp) {
        int px = Math.round(dp * context.getResources().getDisplayMetrics().scaledDensity);
        return px;
    }


}