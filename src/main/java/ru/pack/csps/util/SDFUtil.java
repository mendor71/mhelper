package ru.pack.csps.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDFUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static Date parseDate(String str) throws ParseException {
        return sdfDate.parse(str);
    }

    public static Date parseDateTime(String str) throws ParseException {
        return sdf.parse(str);
    }
}
