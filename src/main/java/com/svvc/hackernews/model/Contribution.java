package com.svvc.hackernews.model;

import com.svvc.hackernews.utils.StringUtils;
import com.svvc.hackernews.utils.TimeFormat;


public class Contribution
{

    private Long contributionId;
    private String title;
    private String link;
    private String content;
    private String textLink;
    private String username;
    private String points;
    private String userId;
    private Long createdAt;
    private Long contributionIdRef;
    private Long contributionTopParentId;
    private String contributionTopParentTitle;
    private boolean canLike = true;
    private int numberOfComments = 0;

    public Long getContributionId()
    {
        return contributionId;
    }

    public void setContributionId(final Long contributionId)
    {
        this.contributionId = contributionId;
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

    public String getContent()
    {
        return content;
    }

    public void setContent(final String content)
    {
        this.content = content;
    }

    public String getTextLink()
    {
        return textLink;
    }

    public void setTextLink(final String textLink)
    {
        this.textLink = textLink;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public String getPoints()
    {
        return points;
    }

    public void setPoints(final String points)
    {
        this.points = points;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(final String userId)
    {
        this.userId = userId;
    }

    public String getTimeText()
    {
        return TimeFormat.formatTimeAgo(createdAt);
    }

    public String getDomainName()
    {
        return StringUtils.getDomainName(link);
    }

    public Long getContributionIdRef()
    {
        return contributionIdRef;
    }

    public void setContributionIdRef(Long contributionIdRef)
    {
        this.contributionIdRef = contributionIdRef;
    }

    public boolean getCanLike()
    {
        return canLike;
    }

    public void setCanLike(boolean b)
    {
        this.canLike = b;
    }

    public int getNumberOfComments()
    {
        return numberOfComments;
    }

    public void setNumberOfComments(final int numberOfComments)
    {
        this.numberOfComments = numberOfComments;
    }

    public Long getContributionTopParentId()
    {
        return contributionTopParentId;
    }

    public void setContributionTopParentId(Long contributionTopParentId)
    {
        this.contributionTopParentId = contributionTopParentId;
    }

    public String getContributionTopParentTitle()
    {
        return contributionTopParentTitle;
    }

    public void setContributionTopParentTitle(final String contributionTopParentTitle)
    {
        this.contributionTopParentTitle = contributionTopParentTitle;
    }

    public Long getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(final Long createdAt)
    {
        this.createdAt = createdAt;
    }
}
