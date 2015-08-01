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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.ineptus.dayline.tools.Logger;
import com.ineptus.dayline.tools.Prefs;

import java.util.ArrayList;
import java.util.List;


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
    private SeekBar fontScaleSeekBar;
    private EditText fontScaleEditText;

    private int fontScale;

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
        fontScaleSeekBar = (SeekBar) findViewById(R.id.seekbar_font_size);
        fontScaleEditText = (EditText) findViewById(R.id.seekbar_font_size_value);

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

        fontScale = getFontScaleValue(fontScaleSeekBar.getProgress());
        fontScaleEditText.setText(String.valueOf(fontScale));
        fontScaleSeekBar.setOnSeekBarChangeListener(getFontScaleSeekBarChangeListener());
        //fontScaleEditText.addTextChangedListener(getFontScaleValueChangeListener());
        //fontScaleEditText.setOnFocusChangeListener(getFontScaleEditTextChangeListener());

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


    private SeekBar.OnSeekBarChangeListener getFontScaleSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontScaleEditText.setText(String.valueOf(getFontScaleValue(progress)));
                fontScaleEditText.setSelection(fontScaleEditText.length());
                fontScale = getFontScaleValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private View.OnFocusChangeListener getFontScaleEditTextChangeListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(true) {
                    Editable text = fontScaleEditText.getEditableText();
                    try {
                        int userScale = Integer.decode(text.toString());
                        int progress;
                        if(userScale < 25) {
                            showToast("minimum: 25%");
                            fontScaleSeekBar.setProgress(0);
                            text.replace(0, text.length(), "25");
                            fontScale = 25;
                        } else if(userScale > 400) {
                            showToast("maximum: 400%");
                            fontScaleSeekBar.setProgress(fontScaleSeekBar.getMax());
                            text.replace(0, text.length(), "400");
                            fontScale = 400;
                        } else {
                            progress = (userScale/25)-2;
                            if(progress > fontScaleSeekBar.getMax()) {
                                progress = fontScaleSeekBar.getMax();
                            } else if (progress < 0) {
                                progress = 0;
                            }
                            fontScaleSeekBar.setProgress(progress);
                            fontScale = userScale;
                        }

                    } catch (NumberFormatException e) {}
                }
            }
        };
    }

    private TextWatcher getFontScaleValueChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable text = fontScaleEditText.getEditableText();
                try {
                    int userScale = Integer.decode(text.toString());
                    int progress;
                    if(userScale < 25) {
                        showToast("minimum: 25%");
                        fontScaleSeekBar.setProgress(0);
                        s.replace(0, s.length(), "25");
                        fontScale = 25;
                    } else if(userScale > 400) {
                        showToast("maximum: 400%");
                        fontScaleSeekBar.setProgress(fontScaleSeekBar.getMax());
                        s.replace(0, s.length(), "400");
                        fontScale = 400;
                    } else {
                        progress = (userScale/25)-2;
                        if(progress > fontScaleSeekBar.getMax()) {
                            progress = fontScaleSeekBar.getMax();
                        } else if (progress < 0) {
                            progress = 0;
                        }
                        fontScaleSeekBar.setProgress(progress);
                        fontScale = userScale;
                    }

                } catch (NumberFormatException e) {}
            }
        };

    }

    private void showToast(CharSequence message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }


    private int getFontScaleValue(int progress) {
        return progress*25+50;
    }


}



