package com.b96software.schoolplannerapp.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


import com.b96software.schoolplannerapp.R;

import java.util.Calendar;

/**
 * Created by boyd on 12/5/2017.
 */

public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener{


    OnDatePickListener activityCallback;

    public interface OnDatePickListener
    {
        void onDatePick(int day, int month, int year);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCallback = (OnDatePickListener) getTargetFragment();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new android.app.DatePickerDialog(getActivity(), R.style.TimeDialogTheme, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        activityCallback.onDatePick(day, month, year);
    }
}
