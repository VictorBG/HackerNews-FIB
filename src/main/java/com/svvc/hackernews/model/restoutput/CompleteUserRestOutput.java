package com.svvc.hackernews.model.restoutput;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteUserRestOutput extends UserRestOutput
{

    private String email;
    private String id;
    private boolean showDead = false;
    private boolean noprocast = false;
    private int maxVisit = 20;
    private int minAway = 180;
    private int delay = 0;
    private String token;

    public int getDelay()
    {
        return delay;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setDelay(int delay)
    {
        this.delay = delay;
    }

    public int getMinAway()
    {
        return minAway;
    }

    public void setMinAway(int minAway)
    {
        this.minAway = minAway;
    }

    public int getMaxVisit()
    {
        return maxVisit;
    }

    public void setMaxVisit(int maxVisit)
    {
        this.maxVisit = maxVisit;
    }

    public boolean isNoprocast()
    {
        return noprocast;
    }

    public void setNoprocast(boolean noprocast)
    {
        this.noprocast = noprocast;
    }

    public boolean isShowDead()
    {
        return showDead;
    }

    public void setShowDead(boolean showDead)
    {
        this.showDead = showDead;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(final String token)
    {
        this.token = token;
    }
}
