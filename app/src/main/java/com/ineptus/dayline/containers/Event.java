package com.ineptus.dayline.containers;

import java.util.TimeZone;

import android.database.Cursor;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.tools.CalendarDrainer;


public class Event {

    public final String title;
    public long start;
    public long end;
    public int color;
    public final int calendarId;
    public boolean isFree;
    public boolean isMaybe;
    public boolean allDay;


    public Event(Contour c, Cursor cursor) {
        title = cursor.getString(CalendarDrainer.TITLE);
        start = cursor.getLong(CalendarDrainer.BEGIN);
        end = cursor.getLong(CalendarDrainer.END);
        calendarId = cursor.getInt(CalendarDrainer.CALENDAR_ID);
        allDay = cursor.getInt(CalendarDrainer.ALL_DAY) == 1;

        if(allDay) {
            int offset = TimeZone.getDefault().getOffset(c.now.toMillis(false));
            start = start - offset;
            end = end - offset;
        }

        String eventColor = cursor.getString(CalendarDrainer.EVENT_COLOR);
        String calendarColor = cursor.getString(CalendarDrainer.CALENDAR_COLOR);
        if(eventColor == null) {
            if(calendarColor == null) {
                color = 0xff000000;
            } else {
                color = Integer.parseInt(calendarColor);
            }
        } else {
            color = Integer.parseInt(eventColor);
        }

        int availability = cursor.getInt(CalendarDrainer.AVAILABILITY);
        int attendeeStatus = cursor.getInt(CalendarDrainer.SELF_ATTENDEE_STATUS);
        isFree = (availability == 1) || (attendeeStatus == 3) ;
        isMaybe = (availability == 2) || (attendeeStatus == 4);
    }

    public Event(String title, long start, long end, int calendarId, String evcolor,
                 String calcolor, int availability, int attendeeStatus, int allDay, String timezone_name) {

        this.title = title;
        this.start = start;
        this.end = end;
        this.isFree = (availability == 1) || (attendeeStatus == 3) ;
        this.isMaybe = (availability == 2) || (attendeeStatus == 4);
        this.calendarId  = calendarId;
        this.allDay = (allDay == 1);

        if(this.allDay) {
        }

        if(evcolor == null) {
            if(calcolor == null) {
                color = 0xff000000;
            } else {
                color = Integer.parseInt(calcolor);
            }
        } else {
            color = Integer.parseInt(evcolor);
        }

    }

    public Event(String title, long start, long end, int calendarId, int color) {

        this.title = title;
        this.start = start;
        this.end = end;
        this.calendarId  = calendarId;

        this.color = color;

    }


    public boolean collidesWith(Event other) {

        return !((start > other.end && end > other.end) || (start < other.start && end < other.start));
    }


    public boolean contains(Event other) {

        return start <= other.start && end >= other.end;
    }


    public long getDuration() {
        return end-start;
    }


    public String toString() {
        return "Event: title="+title +" start="+start + " end="+end;
    }

    public float getStartY(Contour c) {
        return (float) ((start-c.now.toMillis(true))*c.pxPerMili+c.marginTop);
    }

    public float getEndY(Contour c) {
        return (float) ((end-c.now.toMillis(true))*c.pxPerMili+c.marginTop);
    }

    public float getHeight(Contour c) {
        return (float) ((end-start)*c.pxPerMili);
    }

    public boolean coversLine(Contour c) {
        return start < c.start && end > c.end;
    }

    public boolean isSpecial() {
        if(!title.isEmpty()) {
            return false;
        } else {
            return title.substring(0, 1).equals("_");
        }
    }

    public boolean isTitled() {
        return title != null && title != "";
    }

}
