package com.jchartoire.mareu.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    private DateUtils() {
    }

    public static Date getDateFor(int year, int month, int day, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hourOfDay, minute, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static boolean DateInRange(Date testStart, Date testEnd, Date actualStart, Date actualEnd) {
        return ((actualStart.after(testStart) || actualStart.equals(testStart)) && (actualStart.before(testEnd) || actualStart.equals(testEnd)))
                || ((actualEnd.after(testStart) || actualEnd.equals(testStart)) && (actualEnd.before(testEnd) || actualEnd.equals(testEnd)))
                || ((actualStart.before(testStart) || actualStart.equals(testStart)) && (actualEnd.after(testEnd) || actualEnd.equals(testEnd)));
    }
}