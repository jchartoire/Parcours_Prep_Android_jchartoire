package com.jchartoire.mareu;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Objects;

import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    // Detail layout content editText view
    TextView tvClickableStartHour, tvClickableEndHour;
    String pickerTag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pickerTag = this.getTag();
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        tvClickableEndHour = Objects.requireNonNull(getActivity()).findViewById(R.id.tv_clickable_end_hour);
        tvClickableStartHour = Objects.requireNonNull(getActivity()).findViewById(R.id.tv_clickable_start_hour);
        if (pickerTag == "timePicker_start") {
            tvClickableStartHour.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
            tvClickableEndHour.setText(String.format("%02d", hourOfDay + 1) + ":" + String.format("%02d", minute));
        } else if (pickerTag == "timePicker_end") {
            tvClickableEndHour.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
        }
    }
}
