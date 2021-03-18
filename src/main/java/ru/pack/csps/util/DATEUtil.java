package ru.pack.csps.util;

import java.util.Calendar;
import java.util.Date;

public class DATEUtil {

    private static Calendar calendar = Calendar.getInstance();

    public static Date addDays(Date date, int addDays) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, addDays);
        return calendar.getTime();
    }
}
