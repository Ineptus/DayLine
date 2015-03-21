package com.ineptus.dayline.tools;

import android.util.Log;

public final class Logger {

    public static void log(String tag, int level, String log) {

        String[] bannedTags = {};

        for(String s : bannedTags) {
            if(s == tag) {
                return;
            }
        }

        String prefix = "";

        for(int i=0; i<level; i++) {
            prefix += "- ";
        }

        Log.d(prefix+tag, log);

    }

}
