package com.svvc.hackernews.utils;

import java.util.Arrays;
import java.util.List;


public final class TimeFormat
{
    /**
     *  Returns a string representation of the difference between now and the given time.
     *
     * @param createdTime      Time to compare from now
     * @return The string representation with the difference from now to {@param createdTime}
     */
    public static String formatTimeAgo(Long createdTime)
    {
        if (createdTime == null)
        {
            return "";
        }

        long millis = System.currentTimeMillis() - createdTime;

        String[] ids = new String[] {"second", "minute", "hour", "day", "month", "year"};

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = months / 12;

        List<Long> times = Arrays.asList(years, months, days, hours, minutes, seconds);

        for (int i = 0; i < times.size(); i++)
        {
            if (times.get(i) != 0)
            {
                long value = times.get(i).intValue();

                return value + " " + ids[ids.length - 1 - i] + (value == 1 ? "" : "s") + " ago";
            }
        }
        return "0 seconds ago";
    }
}
