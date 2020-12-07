package com.hasawi.sears.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    public static final String FORMAT_Y_M_D_H_m_S = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_Y_M_D_H_m = "yyyy-MM-dd HH:mm";
    public static final String FORMATYMDHmS_noSpace = "yyyyMMddHHmmss";
    public static final String FORMAT_Y_M_D_T_H_m_s_S = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String FORMAT_Y_M_D_T_H_m_s_S_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String FORMAT_D_M_Y = "dd-MM-yyyy";
    public static final String FORMAT_M_D = "MMM dd";
    public static final String FORMAT_ORDER_STATUS = "EEE dd MMM YYYY";
    public static final String FORMAT_D_M_Y_ = "dd MMM yyyy";
    public static final String FORMAT_H_m_s = "HH:mm:ss";
    public static final String FORMAT_H_m__am = "h:mm a";
    public static final String FORMAT_D_MMM_YYYY = "dd-MMM-yyyy";
    public static final String FORMAT_Y_M_D_H_m_a = "yyyy-MM-dd h:mm a";
    public static final String FORMAT_D_M_Y_H_m_a = "dd-MM-yyyy h:mm a";
    public static final String FORMAT_H_m_am_D_MMM_YYYY = "h:mm a, dd MMM yyyy";
    public static final String TIME_ZONE_DUBAI = "GMT+5";
    public static final Locale localeEnglish = Locale.ENGLISH;
    private static final String TIME_ZONE = "Asia/Dubai";

    public static String getCurrentDateTime(String pattern) {
        String formattedDate;
        try {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat sdfDate = new SimpleDateFormat(pattern,
                    localeEnglish); //date "dd-MM-yyyy" time "hh:mm aa" formats

            formattedDate = sdfDate.format(c);
        } catch (Exception e) {
            formattedDate = "";
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Calendar getCurrentTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
    }

    public static String getStringTime(String format, Calendar calendar) {
        SimpleDateFormat format1 = new SimpleDateFormat(format, localeEnglish);
        format1.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return format1.format(calendar.getTime());

    }

    public static Calendar getCalenderFromStringTime(String format, String stringTime) {
        SimpleDateFormat format1 = new SimpleDateFormat(format, localeEnglish);
        format1.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

        try {
            calendar.setTime(format1.parse(stringTime));
        } catch (ParseException e) {
            e.printStackTrace();
            calendar = null;
        }
        return calendar;
    }

    public static String getCurrentStringDateTime() {
        return getStringTime(FORMAT_Y_M_D_H_m_S, getCurrentTime());
    }

    public static String getCurrentStringDateTime(String format) {
        String s = "";
        try {
            s = getStringTime(format, getCurrentTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String changeDateFormat(String currentFormat, String neededFormat, String date) {

        String formatted = "";
        try {
            Calendar calendar = getCalenderFromStringTime(currentFormat, date);
            formatted = getStringTime(neededFormat, calendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatted;
    }

    public static String getFormattedDateString(Date dateToFormat, String neededFormat) {
        String formattedDate = "";
        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat(neededFormat);
            formattedDate = simpleFormat.format(dateToFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;

    }

    public static Date getFormattedDate(String date, String format) {
        Date formattedDate = null;
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            formattedDate = f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String minsAgoFromCurrentDate(String firstDate) {
        String result = "";
        try {
            Date currentDate = null;
            Date previousDate = null;
            try {
                currentDate = new SimpleDateFormat(FORMAT_Y_M_D_H_m_S, localeEnglish).parse(getCurrentStringDateTime(FORMAT_Y_M_D_H_m_S));
                previousDate = new SimpleDateFormat(FORMAT_Y_M_D_H_m, localeEnglish).parse(changeDateFormat(FORMAT_Y_M_D_H_m, FORMAT_Y_M_D_H_m_S, firstDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentDate != null && previousDate != null) {
                long diff = currentDate.getTime() - previousDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                if (minutes < 60) {
                    result = String.valueOf(minutes);
                    result = result + " minutes ago";
                } else {
                    if (hours < 24) {
                        result = String.valueOf(hours);
                        result = result + " hours ago";
                    } else {
                        result = String.valueOf(days);
                        result = result + " days ago";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String r = String.valueOf(result.charAt(0));
        if (r.equals("-")) {
            result = "";
        }

        return result;
    }

    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", localeEnglish);
        return dateFormat.format(timeInMillis);
    }

    public static String formatTimeWithMarker(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", localeEnglish);
        return dateFormat.format(timeInMillis);
    }

    public static int getHourOfDay(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("H", localeEnglish);
        return Integer.valueOf(dateFormat.format(timeInMillis));
    }

    public static int getMinute(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("m", localeEnglish);
        return Integer.valueOf(dateFormat.format(timeInMillis));
    }

    /**
     * If the given time is of a different date, display the date.
     * If it is of the same date, display the time.
     *
     * @param timeInMillis The time to convert, in milliseconds.
     * @return The time or date.
     */
    public static String formatDateTime(long timeInMillis) {
        if (isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else {
            return formatDate(timeInMillis);
        }
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    public static String formatDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", localeEnglish);
        return dateFormat.format(timeInMillis);
    }

    /*
     * Formats timezone date  to date-month-year, time format
     */
    public static String formateTimeZoneDate(String given_date, String needed_format, String timeZone) {
        String formattedDate = "";
        long epoch = 0;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", localeEnglish);
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date;
        try {
            date = df.parse(given_date);
            epoch = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Converting epoch milliseconds to human understandable date
        formattedDate = new java.text.SimpleDateFormat(needed_format, localeEnglish).format(new java.util.Date(epoch * 1000));
        return formattedDate;

    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    public static boolean isToday(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", localeEnglish);
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(System.currentTimeMillis()));
    }

    /**
     * Checks if two dates are of the same day.
     *
     * @param millisFirst  The time in milliseconds of the first date.
     * @param millisSecond The time in milliseconds of the second date.
     * @return Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    public static boolean hasSameDate(long millisFirst, long millisSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", localeEnglish);
        return dateFormat.format(millisFirst).equals(dateFormat.format(millisSecond));
    }

    public static boolean isThisDateWithinCertainRange(String dateToValidate, Date startDate, Date endDate) {

        dateToValidate = changeDateFormat(FORMAT_Y_M_D_T_H_m_s_S_Z, FORMAT_D_M_Y, dateToValidate);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_D_M_Y);
        sdf.setLenient(false);
        try {

            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            if (date.before(endDate) && date.after(startDate))
                return true;
            else
                return false;

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

    }

    public String convert24hrTo12hrFormat(String time) {
        String time_12hr = "";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", localeEnglish);
            final Date dateObj = sdf.parse(time);
            time_12hr = new SimpleDateFormat("hh:mm:aa", localeEnglish).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time_12hr;
    }


}
