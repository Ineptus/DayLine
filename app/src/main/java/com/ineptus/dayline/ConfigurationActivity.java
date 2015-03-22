package com.ineptus.dayline;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
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

    private final static int MIN_RANGE = 4;
    private final static int MAX_RANGE = 48;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private ArrayAdapter<Integer> spinAdapter;
    public static ArrayList<String> checkedCalendars;
    private Spinner rangeSpinner;
    //private NumberPicker rangePicker;
    private Switch buttonSwitch;
    private Switch freeLabelsSwitch;
    private Switch use12hoursSwitch;
    private Switch mirror;
    private Switch showAllDay;

    public ConfigurationActivity() {
        super();
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

        List<Integer> rangeList = new ArrayList<Integer>();
        for (int i = MIN_RANGE; i <= MAX_RANGE; i++) {
            rangeList.add(i);
        }
        spinAdapter = new ArrayAdapter<Integer>(context,
                R.layout.range_spinner, rangeList);
        spinAdapter
                .setDropDownViewResource(R.layout.range_spinner_item);

        rangeSpinner.setAdapter(spinAdapter);
        rangeSpinner.setSelection(12);

/*        String[] nums = new String[20];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i);

        rangePicker.setMinValue(1);
        rangePicker.setMaxValue(20);
        rangePicker.setWrapSelectorWheel(true);
        rangePicker.setDisplayedValues(nums);
        rangePicker.setValue(8);*/


        findViewById(R.id.choose_calendars_button).setOnClickListener(chooseCalendarsListener);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

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
        actionBar.setTitle("DayLine Configuration");

        findViewById(R.id.choose_calendars_button).performClick();
    }


    final View.OnClickListener chooseCalendarsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
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
    };


    final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
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
    };




}



