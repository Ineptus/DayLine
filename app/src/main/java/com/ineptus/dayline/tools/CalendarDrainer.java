package com.ineptus.dayline.tools;

import java.util.ArrayList;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Instances;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.Event;

public class CalendarDrainer {

    public static final int EVENT_ID = 0;
    public static final int TITLE = 1;
    public static final int BEGIN = 2;
    public static final int END = 3;
    public static final int CALENDAR_ID = 4;
    public static final int EVENT_COLOR = 5;
    public static final int CALENDAR_COLOR = 6;
    public static final int ALL_DAY = 7;
    public static final int AVAILABILITY = 8;
    public static final int SELF_ATTENDEE_STATUS  = 9;
    public static final int DTSTART = 10;
    public static final int DTEND = 11;
    public static final int EVENT_TIMEZONE = 12;



    public static void drain(Contour c) {

        ArrayList<Event> instances = getInstances(c);

        //POPULATE EVENTS LIST
        for(int i = 0; i < instances.size(); i++) {

            Event event = instances.get(i);

            //IF NOT A SPECIAL EVENT
            if(!event.isSpecial()) {
                if(!Prefs.load(c, Prefs.SHOW_ALLDAY, true)) {
                    if(event.allDay) continue;
                }
                c.events.add(event);
            } else {
                //HANDLE SPECIAL EVENTS
                //SOMEDAY
            }
        }
    }



    private static ArrayList<Event> getInstances(Contour c) {


        ArrayList<Event> list = new ArrayList<Event>();

        Cursor cursor;
        ContentResolver cr = c.context.getContentResolver();

        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, c.start);
        ContentUris.appendId(builder, c.end);

        final String[] INSTANCE_PROJECTION = new String[] {
                Instances.EVENT_ID,
                Instances.TITLE,
                Instances.BEGIN,
                Instances.END,
                Instances.CALENDAR_ID,
                Instances.EVENT_COLOR,
                Instances.CALENDAR_COLOR,
                Instances.ALL_DAY,
                Instances.AVAILABILITY,
                Instances.SELF_ATTENDEE_STATUS,
                Instances.DTSTART,
                Instances.DTEND,
                Instances.EVENT_TIMEZONE
        };


        final String selection = "calendar_id = ?";

        final Set<String> chosenCalendars = Prefs.loadChosenCalendars(c.context, c.widgetId);

        for(String cal : chosenCalendars) {

            String[] selectionArgs = {cal};
            cursor =  cr.query(builder.build(),
                    INSTANCE_PROJECTION,
                    selection,
                    selectionArgs,
                    null);

            if (cursor.moveToFirst()) {
                do {
                    list.add(new Event(c, cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();

        }
        return list;

    }


    public static CharSequence[] getCalendarsNames(Context context) {

        Cursor cursor = getCalendars(context);

        String[] names = new String[cursor.getCount()];

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                names[i] = cursor.getString(2);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return names;
    }

    public static boolean[] getChecksArray(Context context) {

        Cursor cursor = getCalendars(context);

        return new boolean[cursor.getCount()];

    }

    private static Cursor getCalendars(Context context) {

        Cursor cursor;
        String[] my_proj = {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        cursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, my_proj, null, null, null);
        return(cursor);
    }

    public static String[] getCalendarIds(Context context) {

        Cursor cursor = getCalendars(context);

        String[] ids = new String[cursor.getCount()];

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                ids[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }



}