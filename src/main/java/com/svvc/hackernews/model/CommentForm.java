package com.svvc.hackernews.model;

public class CommentForm
{

    private String text;
    private Long contributionIdRef;
    private Long contributionTopParentId;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Long getContributionIdRef()
    {
        return contributionIdRef;
    }

    public void setContributionIdRef(Long contributionIdRef)
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
