package com.svvc.hackernews.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;


@Entity
@Table(name = "likes")
public class Likes
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long likeId;
    @ManyToOne(targetEntity = User.class)
    private User userId;
    @ManyToOne(targetEntity = ContributionDao.class)
    private ContributionDao contributionId;
    @CreatedDate
    private long likeCreatedAt;

    public Long getLikeId()
    {
        return likeId;
    }

    public void setLikeId(Long likeId)
    {
        this.likeId = likeId;
    }

    public User getUserId()
    {
        return userId;
    }

    public void setUserId(User userId)
    {
        this.userId = userId;
    }

    public ContributionDao getContributionId()
    {
        return contributionId;
    }

    public void setContributionId(ContributionDao contributionId)
    {
        this.contributionId = contributionId;
    }

    public long getLikeCreatedAt()
    {
        return likeCreatedAt;
    }

    public void setLikeCreatedAt(long likeCreatedAt)
    {
        this.likeCreatedAt = likeCreatedAt;
    }

}
