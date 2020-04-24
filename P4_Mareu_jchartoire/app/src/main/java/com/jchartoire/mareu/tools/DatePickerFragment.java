package com.jchartoire.mareu.tools;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private Date date;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener, Date preSelectedDate) {
        this.date = preSelectedDate;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year;
        int month;
        int day;

        if (date != null) {
            c.setTime(date);
        }
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), listener, year, month, day);
        dialog.getDatePicker().setMinDate(new Date().getTime());
        return dialog;
    }
}
