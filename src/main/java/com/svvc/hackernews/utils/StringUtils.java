package com.svvc.hackernews.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtils
{

    private static final String hostExtractorRegexString =
        "(?:https?://)?(?:www\\.)?(.+\\.)(com|au\\.uk|co\\.in|be|in|uk|org\\.in|org|net|edu|gov|mil|ly|es|cat|eu)";
    private static final Pattern hostExtractorRegexPattern = Pattern
        .compile(hostExtractorRegexString);

    /**
     * Extracts the domain name of a given url.
     *
     * @param url       The url to extract the domain
     * @return The domain obtained, in case it was successful
     */
    public static String getDomainName(String url)
    {
        if (url == null)
        {
            return null;
        }
        url = url.trim();
        Matcher m = hostExtractorRegexPattern.matcher(url);
        if (m.find() && m.groupCount() == 2)
        {
            return m.group(1) + m.group(2);
        }
        return null;
    }

    public static boolean isURL(String url)
    {
        return hostExtractorRegexPattern.matcher(url).find();
    }
}
