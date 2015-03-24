package com.ineptus.dayline;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;
import android.view.WindowManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Surface;

import com.ineptus.dayline.tools.Prefs;
import com.ineptus.dayline.draw.OriginalDrawer;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ConfigurationActivity WidgetProviderConfigureActivity}
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] widgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = widgetIds.length;
        for (int widgetId : widgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), WidgetProvider.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onDeleted(Context context, int[] widgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = widgetIds.length;
        for (int widgetId : widgetIds) {
            Prefs.clearAll(context, widgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        //	CHECK IF NEED TO REPLACE WIDGETS BECAUSE OF UPDATE
        if(Prefs.load(context, widgetId, Prefs.VERSION, 0) < 8) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_error);
            appWidgetManager.updateAppWidget(widgetId, views);
            return; }

        //	INIT
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Resources resources = context.getResources();
        int orientation = getDeviceDefaultOrientation(context);

        //	GET OPTIONS
        int maxHeight =  appWidgetManager.getAppWidgetOptions(widgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        int maxWidth =  appWidgetManager.getAppWidgetOptions(widgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int minHeight =  appWidgetManager.getAppWidgetOptions(widgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int minWidth =  appWidgetManager.getAppWidgetOptions(widgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        //	DEFINE NEW DIMENSIONS
        int newHeight = maxHeight;
        int newWidth = minWidth;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newHeight = minHeight;
            newWidth = maxWidth; }
        //		ESPECIALLY FOR NOVA LAUNCHER 3.3 :)
        if(newHeight>resources.getDisplayMetrics().heightPixels) newHeight = minHeight;
        if(newWidth>resources.getDisplayMetrics().widthPixels) newWidth = minWidth;

        //	CREATE CONTOUR AND DRAW
        Contour contour = createWidgetContour(context, widgetId, newHeight, newWidth);
        OriginalDrawer.draw(contour, views);

        //	SET LISTENER TO OPEN CALENDAR IF CHOSEN TO
        if(Prefs.load(contour, Prefs.CLICKABLE, false)) {
            //OPEN CALENDAR

            Uri uri = Uri.parse("content://com.android.calendar/time/" + contour.now.toMillis(true));

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener to the button
            views.setOnClickPendingIntent(R.id.dayline_image, pendingIntent);
        }


        // Tell the AppWidgetManager to perform an update on the current App Widget
        appWidgetManager.updateAppWidget(widgetId, views);
    }




    static Contour createWidgetContour(Context context, int widgetId, int newHeight, int newWidth) {

        int width = 260;
        int height = 360;
        if(newWidth > 0 && newHeight > 0) {
            width = dpToPx(context, newWidth);
            height = dpToPx(context, newHeight);
        }

        return new Contour(context,
                widgetId,
                width,
                height);
    }

    public static int dpToPx(Context context, int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    private static int getDeviceDefaultOrientation(Context context) {
        WindowManager windowManager =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Configuration config = context.getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if ( ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }
}


