package com.ineptus.dayline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.format.Time;

import com.ineptus.dayline.containers.BoxedLine;
import com.ineptus.dayline.containers.Event;
import com.ineptus.dayline.containers.EventsList;
import com.ineptus.dayline.containers.LineBox;
import com.ineptus.dayline.containers.PeculiarLabelsManager;
import com.ineptus.dayline.tools.CalendarDrainer;
import com.ineptus.dayline.tools.Prefs;

public class Contour {

    //GENERAL
    public final int widgetId;
    public final Context context;
    public int version;

    public Bitmap bitmap;
    public Canvas canvas;


    //DIMENSIONS
    public final int width;
    public final int height;

    //USER SETTINGS
    public String[] chosenCalendars;
    public final int range;
    public final boolean use12hours;
    public final boolean mirror;
    public final boolean labelFreeTime;


    //AXIS
    public final int axisWidth;
    public final int axisHeight;

    public final int axisTop;
    public final int axisBottom;

    public final int labelsTop;
    public final int labelsBottom;

    public final int axisX;
    public final int marginTop = 15;
    public final int marginBottom = 15;
    public int marginRight;

    public final int eventWidth;

    //LABELS
    public final int labelHeight;
    public final int labelHalfHeight;

    //CALENDAR & TIME
    public final long start;
    public final long end;

    public final double pxPerMili;
    public final double pxPerSecond;
    public final double pxPerMinute;
    public final double pxPerHour;
    public int textSize = 30;
    public int textVertPadding = 5;
    public int labelsMargin = 50;


    public final Time now = new Time();

    //CONTAINERS
    public final EventsList events;
    public final BoxedLine line;
    public BoxedLine sleep;
    //public final SimpleLabelsManager labels;
    public final PeculiarLabelsManager labels;

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
        labelFreeTime = Prefs.load(this, Prefs.LABEL_FREE_TIME, true);

        //INIT CONTEXT DEPENDENT VALUES:
        if(mirror){
            axisX = dpToPx(40);
        } else {
            axisX = width - dpToPx(40);
        }
        eventWidth = dpToPx(24);
        axisWidth = dpToPx(7);

        axisHeight = height - marginTop - marginBottom;
        axisTop = marginTop;
        axisBottom = height - marginBottom;

        textSize = dpToPx(16);
        textVertPadding = dpToPx(3);
        labelsMargin = dpToPx(26);
        labelHeight = textSize + textVertPadding;
        labelHalfHeight = labelHeight/2;

        labelsTop = labelHalfHeight;
        labelsBottom = height-labelHeight;

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
        //labels = new SimpleLabelsManager(this);
        labels = new PeculiarLabelsManager(this);

        //DRAIN CALENDAR WHEN EVERYTHING INITIATED
        CalendarDrainer.drain(this);

        //BUILD BOXED LINE
        line = new BoxedLine(this, events);
    }


    public int getEventCenterY(Event event) {

        int start = (int) ((event.start-this.start)*pxPerMili);
        int duration = (int) (event.getDuration()*pxPerMili);

        return start+duration/2;
    }

    public float getEventStartY(Event event) {
        return (float) ((event.start-now.toMillis(true))*pxPerMili); }

    public float getEventEndY(Event event) {
        return (float) ((event.end-now.toMillis(true))*pxPerMili); }

    public int getBoxCenterY(LineBox box) {
        int start = (int) (box.getRelStart()*pxPerMili);
        int duration = (int) (box.getDuration()*pxPerMili);

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
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    public int spToPx(int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().scaledDensity);
    }


}