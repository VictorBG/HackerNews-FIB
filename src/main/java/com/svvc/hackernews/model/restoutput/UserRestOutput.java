package com.svvc.hackernews.model.restoutput;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRestOutput
{

    public String username;
    public String about;
    public long createdAt;
    public int karma = 1;

    public int getKarma()
    {
        return karma;
    }

    public void setKarma(int karma)
    {
        this.karma = karma;
    }

    public long getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(long createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
