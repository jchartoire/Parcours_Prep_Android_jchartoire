package com.jchartoire.mareu.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private DateUtils() {
    }

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/YYYY", Locale.FRANCE);
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.FRANCE);

    public static boolean DateInRange(Date testStart , Date testEnd, Date actualStart, Date actualEnd) {
        return ((actualStart.after(testStart) || actualStart.equals(testStart)) && (actualStart.before(testEnd) || actualStart.equals(testEnd)))
                || ((actualEnd.after(testStart) || actualEnd.equals(testStart)) && (actualEnd.before(testEnd) || actualEnd.equals(testEnd)))
                || ((actualStart.before(testStart) || actualStart.equals(testStart)) && (actualEnd.after(testEnd) || actualEnd.equals(testEnd)));
    }
}