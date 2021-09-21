package com.svvc.hackernews.model;

public class ContributionForm
{

    private String title;
    private String url;
    private String text;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public String getText()
    {
        return text;
    }

    public void setText(final String text)
    {
        this.text = text;
    }
}
