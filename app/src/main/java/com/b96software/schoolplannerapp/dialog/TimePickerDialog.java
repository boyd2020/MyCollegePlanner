package com.b96software.schoolplannerapp.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.b96software.schoolplannerapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by boyd on 12/5/2017.
 */

public class TimePickerDialog extends DialogFragment
        implements android.app.TimePickerDialog.OnTimeSetListener {

    private OnTimeSelectedListener activityCallback;

    public interface OnTimeSelectedListener
    {
        void onTimeSelected(int hourOfDay, int minute);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = new GregorianCalendar();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new android.app.TimePickerDialog(getActivity(), R.style.TimeDialogTheme, this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCallback = (OnTimeSelectedListener) getTargetFragment();

    }



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        activityCallback.onTimeSelected(hourOfDay, minute);
        dismiss();
    }
}