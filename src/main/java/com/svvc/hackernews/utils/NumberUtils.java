package com.svvc.hackernews.utils;

public final class NumberUtils
{
    public static boolean isNumeric(String strNum)
    {
        if (strNum == null)
        {
            return false;
        }
        try
        {
            double d = Double.parseDouble(strNum);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
