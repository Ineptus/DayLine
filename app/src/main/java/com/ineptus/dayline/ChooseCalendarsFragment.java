package com.ineptus.dayline;

import java.util.HashSet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;

import com.ineptus.dayline.tools.CalendarDrainer;
import com.ineptus.dayline.tools.Prefs;

public class ChooseCalendarsFragment extends DialogFragment {
    
	Context context;
	HashSet<String> checkedBoxes = new HashSet<>();
	HashSet<String> chosenCalendars = new HashSet<>();
	private int widgetId;
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void setWidgetId(int widgetId) {
		this.widgetId = widgetId;
	}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(context == null) {
            context = getActivity().getApplicationContext();
        }


		// Use the Builder class for convenient dialog construction
		CharSequence[] items = CalendarDrainer.getCalendarsNames(context);
		boolean[] checks = CalendarDrainer.getChecksArray(context);
		for(String s : Prefs.loadCheckedBoxes(context, widgetId)) checks[Integer.valueOf(s)] = true;
		checkedBoxes = (HashSet<String>) Prefs.load(context, widgetId, Prefs.CHECKED_BOXES, checkedBoxes);
		chosenCalendars = (HashSet<String>) Prefs.loadChosenCalendars(context, widgetId);
		
		final String[] CALENDAR_IDS = CalendarDrainer.getCalendarIds(context);
		
		OnMultiChoiceClickListener listener = new OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if(isChecked) {
					checkedBoxes.add(String.valueOf(which));
					chosenCalendars.add(CALENDAR_IDS[which]);
				} else if(checkedBoxes.remove(String.valueOf(which))) {
					chosenCalendars.remove(CALENDAR_IDS[which]);
				}
			}
		};
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_calendars);
        builder.setMultiChoiceItems(items, checks, listener)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Prefs.saveCheckedBoxes(context, widgetId, checkedBoxes);
                	   Prefs.saveChosenCalendars(context, widgetId, chosenCalendars);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
