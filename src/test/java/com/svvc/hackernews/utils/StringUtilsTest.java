package com.svvc.hackernews.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class StringUtilsTest
{

    @Test
    void isURL()
    {
        assertFalse(StringUtils.isURL("asdas"));
        assertTrue(StringUtils.isURL("https://www.reddit.com"));
        assertTrue(StringUtils.isURL("https://www.reddit.es"));
        assertTrue(StringUtils.isURL("https://www.reddit.cat"));
        assertTrue(StringUtils.isURL("https://www.reddit.ly"));
        assertTrue(StringUtils.isURL("https://www.reddit.cat/loquesea"));
        assertFalse(StringUtils.isURL("https://www.reddit.cta/loquesea"));
    }
}
