package com.jchartoire.mareu.tools;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    private Date date;

    public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener, Date preSelectedDate) {
        this.date = preSelectedDate;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour;
        int minute;
        if (date != null) {
            // Use the current time as the default values for the picker
            c.setTime(date);
        }

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(requireActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
