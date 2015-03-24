package com.ineptus.dayline;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import com.ineptus.dayline.tools.Logger;
import com.ineptus.dayline.tools.Prefs;


/**
 * The configuration screen for the {@link WidgetProvider WidgetProvider} AppWidget.
 */
public class ConfigurationActivity extends ActionBarActivity {

    private final static String CALENDARS_CHOSEN_KEY = "calendars_chosen";
    private final static String PREFS_CLEARED_KEY = "prefs_cleared";

    private final static int MIN_RANGE = 4;
    private final static int MAX_RANGE = 48;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public static ArrayList<String> checkedCalendars;
    private Spinner rangeSpinner;
    //private NumberPicker rangePicker;
    private Switch buttonSwitch;
    private Switch freeLabelsSwitch;
    private Switch use12hoursSwitch;
    private Switch mirror;
    private Switch showAllDay;

    private boolean calendarsChosen = false;
    private boolean prefsCleared = false;

    public ConfigurationActivity() {
        super();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CALENDARS_CHOSEN_KEY, calendarsChosen);
        outState.putBoolean(PREFS_CLEARED_KEY, prefsCleared);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calendarsChosen = savedInstanceState.getBoolean(CALENDARS_CHOSEN_KEY, false);
        prefsCleared = savedInstanceState.getBoolean(PREFS_CLEARED_KEY, false);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_activity_configuration);
        Context context = getApplicationContext();

        rangeSpinner = (Spinner) findViewById(R.id.range_spinner);
        //rangePicker = (NumberPicker) findViewById(R.id.range_picker);
        buttonSwitch = (Switch) findViewById(R.id.button_switch);
        freeLabelsSwitch = (Switch) findViewById(R.id.switch_free_labels);
        use12hoursSwitch = (Switch) findViewById(R.id.switch_12_hours);
        mirror = (Switch) findViewById(R.id.switch_mirror);
        showAllDay = (Switch) findViewById(R.id.switch_show_allday);

        List<Integer> rangeList = new ArrayList<>();
        for (int i = MIN_RANGE; i <= MAX_RANGE; i++) {
            rangeList.add(i);
        }
        ArrayAdapter<Integer> spinAdapter = new ArrayAdapter<>(context,
                R.layout.range_spinner, rangeList);
        spinAdapter
                .setDropDownViewResource(R.layout.range_spinner_item);

        rangeSpinner.setAdapter(spinAdapter);
        rangeSpinner.setSelection(12);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.configuration_actionbar_title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!prefsCleared) {
            prefsCleared = true;
            Prefs.clearAll(getApplicationContext(), mAppWidgetId);
        }

        if(!calendarsChosen) {
            calendarsChosen = true;
            showCalendarsChoice(findViewById(R.id.choose_calendars_button));
        }
    }


    public void showCalendarsChoice(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ChooseCalendarsFragment dialogF = new ChooseCalendarsFragment();

        dialogF.setContext(getApplicationContext());
        dialogF.setWidgetId(mAppWidgetId);
        dialogF.show(ft, "dialog");
    }

    public void addWidget(View view) {
            final Context context = ConfigurationActivity.this;

            int version;
            try {
                version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            } catch (NameNotFoundException e) {
                version = 0;
            }
            Prefs.save(context, mAppWidgetId, Prefs.VERSION, version);

            Prefs.save(context, mAppWidgetId, Prefs.RANGE, rangeSpinner.getSelectedItemPosition() + MIN_RANGE);
            Prefs.save(context, mAppWidgetId, Prefs.CLICKABLE, buttonSwitch.isChecked());
            Prefs.save(context, mAppWidgetId, Prefs.LABEL_FREE_TIME, freeLabelsSwitch.isChecked());
            Prefs.save(context, mAppWidgetId, Prefs.USE12HOURS, use12hoursSwitch.isChecked());
            Prefs.save(context, mAppWidgetId, Prefs.MIRROR, mirror.isChecked());
            Prefs.save(context, mAppWidgetId, Prefs.SHOW_ALLDAY, showAllDay.isChecked());


            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            WidgetProvider.updateWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
    }




    public void clickReceiver(View view) {
        RelativeLayout l = (RelativeLayout) view;
        View child = l.getChildAt(l.getChildCount()-1);
        child.performClick();
    }


}



