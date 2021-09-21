package com.svvc.hackernews.model.restoutput;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.svvc.hackernews.model.AuthUser;

import io.swagger.annotations.ApiModelProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContributionRestOutput
{
    @ApiModelProperty(value = "The id of the contribution", required = true)
    private Long id;

    @ApiModelProperty(value = "The title if the contribution")
    private String title;

    @ApiModelProperty(value = "The content of the contribution, it can be an URL or a simple text", required = true)
    private String content;

    @ApiModelProperty(value = "The likes the contribution has", required = true)
    private String points;

    @ApiModelProperty(value = "The user that created the contribution", required = true)
    private AuthUser user;

    @ApiModelProperty(value = "The timestamp in ms of the contribution creation date", required = true)
    private Long createdAt;
    /**
     * IMPORTANT: This is displayed as an empty list in the swagger ui due to a bug in the librare swagger-ui
     * used: https://github.com/swagger-api/swagger-ui/issues/3325
     */
    @ApiModelProperty(value = "List of comments related to the contribution, it is a list of ContributionRestOutput")
    private List<ContributionRestOutput> comments;

    @ApiModelProperty(value = "Length of the comments of the contribution")
    private Integer commentsLength;

    @ApiModelProperty(value = "Indicates if the contribution has been liked by the user, if any")
    private Boolean liked;

    @ApiModelProperty(value = "Indicates the top parent of this contribution")
    private Long contributionTopParentId;

    @ApiModelProperty(value = "Indicates the position in a tree of comments")
    private int treeLength;

    @ApiModelProperty(value = "The id of the parent contribution")
    private Long contributionParentId;

    @ApiModelProperty(value = "The title of the top parent contribution")
    private String contributionTopParentTitle;

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(final String content)
    {
        this.content = content;
    }

    public String getPoints()
    {
        return points;
    }

    public void setPoints(final String points)
    {
        this.points = points;
    }

    public AuthUser getUser()
    {
        return user;
    }

    public void setUser(final AuthUser user)
    {
        this.user = user;
    }

    public Long getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(final Long createdAt)
    {
        this.createdAt = createdAt;
    }

    public List<ContributionRestOutput> getComments()
    {
        return comments;
    }

    public void setComments(List<ContributionRestOutput> comments)
    {
        this.comments = comments;
    }

    public Integer getCommentsLength()
    {
        return commentsLength;
    }

    public void setCommentsLength(final Integer commentsLength)
    {
        this.commentsLength = commentsLength;
    }

    public Boolean getLiked()
    {
        return liked;
    }

    public void setLiked(final Boolean liked)
    {
        this.liked = liked;
    }


    public Long getContributionTopParentId() { return contributionTopParentId; }

    public void setContributionTopParentId(Long contributionTopParentId) { this.contributionTopParentId = contributionTopParentId; }

    public int getTreeLength() { return treeLength; }

    public void setTreeLength(int treeLength) { this.treeLength = treeLength; }

    public Long getContributionParentId() {
        return contributionParentId;
    }

    public void setContributionParentId(Long contributionParentId) {
        this.contributionParentId = contributionParentId;
    }

    public String getContributionTopParentTitle() {
        return contributionTopParentTitle;
    }

    public void setContributionTopParentTitle(String contributionTopParentTitle) {
        this.contributionTopParentTitle = contributionTopParentTitle;
    }
}
