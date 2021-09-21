package com.svvc.hackernews.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;


@Entity
@Table(name = "contributions")
public class ContributionDao
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    @Column(columnDefinition = "text")
    private String content = null;

    private String link = null;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(columnDefinition = "integer default '0'")
    private Integer points;

    @CreatedDate
    private Long createdAt;

    private long contributionIdRef;

    private Long contributionTopParentId;

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(final String content)
    {
        this.content = content;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(final User user)
    {
        this.user = user;
    }

    public Integer getPoints()
    {
        return points;
    }

    public void setPoints(final Integer points)
    {
        this.points = points;
    }

    public void addVote()
    {
        addVote(1);
    }

    public void removeVote()
    {
        addVote(-1);
    }

    private void addVote(Integer numToAdd)
    {
        this.points = this.points + numToAdd;
    }

    public Long getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(final Long createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(final String link)
    {
        this.link = link;
    }

    public long getContributionIdRef()
    {
        return contributionIdRef;
    }

    public void setContributionIdRef(long contributionIdRef)
    {
        this.contributionIdRef = contributionIdRef;
    }

    public Long getContributionTopParentId()
    {
        return contributionTopParentId;
    }

    public void setContributionTopParentId(Long contributionTopParentId)
    {
        this.contributionTopParentId = contributionTopParentId;
    }
}


