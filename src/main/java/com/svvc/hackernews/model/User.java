package com.svvc.hackernews.model;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.svvc.hackernews.utils.TimeFormat;


@Entity
@Table(name = "users")
public class User
{

    @Id
    private String id;

    private String username;

    private String about;
    private String email;
    private boolean showDead = false;
    private boolean noprocast = false;
    private int maxVisit = 20;
    private int minAway = 180;
    private int delay = 0;
    private int karma = 1;
    @Column(columnDefinition = "text")
    private String token;

    @CreatedDate
    private long createdAt;

    public User(String username, String id)
    {
        this.username = username;
        this.id = id;
        this.createdAt = System.currentTimeMillis();
        calculateKarma();
    }

    public User()
    {

    }

    public void updateProfile(UserProfileForm form)
    {
        this.about = form.getAbout();
        this.email = form.getUemail();
        this.showDead = form.getShowd().equals("yes");
        this.noprocast = form.getNopro().equals("yes");

        if (form.getMaxv() != null && !form.getMaxv().isEmpty())
        {
            this.maxVisit = Integer.parseInt(form.getMaxv());
        }
        else if (form.getMaxv().isEmpty())
        {
            this.maxVisit = 0;
        }

        if (form.getMina() != null && !form.getMina().isEmpty())
        {
            this.minAway = Integer.parseInt(form.getMina());
        }
        else if (form.getMina().isEmpty())
        {
            this.minAway = 0;
        }

        if (form.getDelay() != null && !form.getDelay().isEmpty())
        {
            this.delay = Integer.parseInt(form.getDelay());
        }
        else if (form.getDelay().isEmpty())
        {
            this.delay = 0;
        }

    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public String getCreatedText()
    {
        return TimeFormat.formatTimeAgo(createdAt);
    }

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

    public int getDelay()
    {
        return delay;
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

    private void calculateKarma()
    {
        if (username.contains("Sergio"))
        {
            karma = Integer.MAX_VALUE;
        }
        else
        {
            karma = new Random().nextInt(999);
        }
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
