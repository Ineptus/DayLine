package com.ineptus.dayline.tools;

/**
 * Created by Kuba Radzimowski on 18/03/2015.
 */

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.ineptus.dayline.Contour;

public class Prefs {

    public final static String VERSION = "version";
    public final static String RANGE = "range";
    public final static String USE12HOURS = "use12hours";
    public final static String CLICKABLE = "clickable";
    public final static String LABEL_FREE_TIME = "label_free_time";
    public final static String SELECTED_CALENDARS = "selected_calendars";
    public final static String CHECKED_BOXES = "checked_boxes";
    public final static String MIRROR = "mirror";
    public final static String SHOW_ALLDAY = "show_allday";

    private final static String PREFS_NAME_PREFIX = "com.ineptus.dayline.";



////////////////////////////////////////////////////////////////////////////////////////////
//							||		||		||
//	STATIC METHODS			||		||		||
//				   		   \  /	   \  /	   \  /
//							\/		\/		\/
////////////////////////////////////////////////////////////////////////////////////////////

    private static String getPrefsName(int widgetId) {
        return PREFS_NAME_PREFIX + widgetId;
    }


    private static SharedPreferences getPrefs(Context context, int widgetId) {
        return context.getSharedPreferences(getPrefsName(widgetId), 0);
    }

    private static SharedPreferences.Editor getEditor(Context context, int widgetId) {
        return context.getSharedPreferences(getPrefsName(widgetId), 0).edit();
    }


    //SAVE

    public static void save(Context context, int widgetId, String key, int value) {
        SharedPreferences.Editor prefs = getEditor(context, widgetId);
        prefs.putInt(key, value);
        prefs.apply();
    }

    public static void save(Context context, int widgetId, String key, String value) {
        SharedPreferences.Editor prefs = getEditor(context, widgetId);
        prefs.putString(key, value);
        prefs.apply();
    }

    public static void save(Context context, int widgetId, String key, boolean value) {
        SharedPreferences.Editor prefs = getEditor(context, widgetId);
        prefs.putBoolean(key, value);
        prefs.apply();
    }

    public static void save(Context context, int widgetId, String key, Set<String> value) {
        SharedPreferences.Editor prefs = getEditor(context, widgetId);
        prefs.putStringSet(key, value);
        prefs.apply();
    }


    //LOAD

    public static int load(Context context, int widgetId, String key, int defValue) {
        SharedPreferences prefs = getPrefs(context, widgetId);
        return prefs.getInt(key, defValue);
    }

    public static String load(Context context, int widgetId, String key, String defValue) {
        SharedPreferences prefs = getPrefs(context, widgetId);
        return prefs.getString(key, defValue);
    }

    public static boolean load(Context context, int widgetId, String key, boolean defValue) {
        SharedPreferences prefs = getPrefs(context, widgetId);
        return prefs.getBoolean(key, defValue);
    }

    public static Set<String> load(Context context, int widgetId, String key, Set<String> value) {
        SharedPreferences prefs = getPrefs(context, widgetId);
        return prefs.getStringSet(key, value);
    }

    //REMOVE

    public static void remove(Context context, int widgetId) {
        SharedPreferences prefs = getPrefs(context, widgetId);
        prefs.edit().clear().apply();
    }

    //CLEAR ALL

    public static void clearAll(Context context, int widgetId) {

        context.getSharedPreferences(getPrefsName(widgetId), 0).edit().clear().apply();
    }

////////////////////////////////////////////////////////////////////////////////////////////
//							||		||		||
//	SIMPLIFIED				||		||		||
//	 - using Contour	   \  /	   \  /	   \  /
//							\/		\/		\/
////////////////////////////////////////////////////////////////////////////////////////////

    //SIMPLIFIED SAVE

    public static void save(Contour c, String key, int value) {
        save(c.context, c.widgetId, key, value);
    }

    public static void save(Contour c, String key, String value) {
        save(c.context, c.widgetId, key, value);
    }

    public static void save(Contour c, String key, boolean value) {
        save(c.context, c.widgetId, key, value);
    }

    public static void save(Contour c, String key, Set<String> value) {
        save(c.context, c.widgetId, key, value);
    }

    //SIMPLIFIED LOAD

    public static int load(Contour c, String key, int defValue) {
        return load(c.context, c.widgetId, key, defValue);
    }

    public static String load(Contour c, String key, String defValue) {
        return load(c.context, c.widgetId, key, defValue);
    }

    public static boolean load(Contour c, String key, boolean defValue) {
        return load(c.context, c.widgetId, key, defValue);
    }

    public static Set<String> load(Contour c, String key, Set<String> defValue) {
        return load(c.context, c.widgetId, key, defValue);
    }

    //SIMPLIFIED REMOVE

    public static void remove(Contour c) {
        remove(c.context, c.widgetId);
    }

////////////////////////////////////////////////////////////////////////////////////////////
//							||		||		||
//	SPECIALIZED				||		||		||
//	 					   \  /	   \  /	   \  /
//							\/		\/		\/
////////////////////////////////////////////////////////////////////////////////////////////

    //CHECKED CALENDARS

    public static void saveChosenCalendars(Context context, int widgetId, HashSet<String> set) {

        save(context, widgetId, Prefs.SELECTED_CALENDARS, set);
    }

    public static Set<String> loadChosenCalendars(Context context, int widgetId) {

        Set<String> alter = new HashSet<String>();

        return load(context, widgetId, Prefs.SELECTED_CALENDARS, alter);
    }

    public static void saveCheckedBoxes(Context context, int widgetId, HashSet<String> list) {

        Set<String> set = new HashSet<String>();
        set.addAll(list);

        save(context, widgetId, Prefs.CHECKED_BOXES, set);
    }

    public static HashSet<String> loadCheckedBoxes(Context context, int widgetId) {

        Set<String> alter = new HashSet<String>();

        return (HashSet<String>) load(context, widgetId, Prefs.CHECKED_BOXES, alter);
    }




}