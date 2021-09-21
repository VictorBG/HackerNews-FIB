package com.svvc.hackernews.service.contributions.mapper;

import com.svvc.hackernews.model.AuthUser;
import com.svvc.hackernews.model.CommentForm;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.model.ContributionDao;
import com.svvc.hackernews.model.ContributionForm;
import com.svvc.hackernews.model.restoutput.ContributionRestOutput;


public class ContributionsMapper
{

    /**
     * Converts the given {@link ContributionDao} into a {@link Contribution}
     *
     * @param contribution  {@link ContributionDao} to map
     * @return The {@link Contribution} mapped from the given {@param contribution}
     */
    public static Contribution mapDaoToContribution(final ContributionDao contribution)
    {
        final Contribution result = new Contribution();

        result.setPoints(String.valueOf(contribution.getPoints()));
        result.setLink(contribution.getLink());
        result.setCreatedAt(contribution.getCreatedAt());
        result.setTitle(contribution.getTitle());
        result.setContributionId(contribution.getId());
        result.setUsername(contribution.getUser().getUsername());
        result.setUserId(contribution.getUser().getId());
        result.setContributionIdRef(contribution.getContributionIdRef());
        result.setContent(contribution.getContent());
        result.setContributionTopParentId(contribution.getContributionTopParentId());
        return result;
    }

    /**
     * Converts the given {@link ContributionForm} into a {@link ContributionDao}
     *
     * @param input  {@link ContributionForm} to map
     * @return The {@link ContributionDao} mapped from the given {@param input}
     */
    public static ContributionDao mapContributionInputToContributionDao(final ContributionForm input)
    {
        final ContributionDao contributionDao = new ContributionDao();

        contributionDao.setPoints(0);
        contributionDao.setContent(input.getText());
        contributionDao.setTitle(input.getTitle());
        contributionDao.setLink(input.getUrl());
        return contributionDao;
    }

    /**
     * Converts the given {@link CommentForm} into a {@link ContributionDao}
     *
     * @param input  {@link CommentForm} to map
     * @return The {@link ContributionDao} mapped from the given {@param input}
     */
    public static ContributionDao mapCommentInputToContributionDao(final CommentForm input)
    {
        final ContributionDao contributionDao = new ContributionDao();

        contributionDao.setPoints(0);
        contributionDao.setContent(input.getText());
        contributionDao.setContributionIdRef(input.getContributionIdRef());
        contributionDao.setContributionTopParentId(input.getContributionTopParentId());
        return contributionDao;
    }

    public static ContributionRestOutput mapContributionToContributionRestOutput(final Contribution contribution)
    {
        final ContributionRestOutput contributionRestOutput = new ContributionRestOutput();

        contributionRestOutput.setId(contribution.getContributionId());
        contributionRestOutput.setContent(contribution.getContent().isEmpty() ? contribution.getLink() : contribution.getContent());
        contributionRestOutput.setTitle(contribution.getTitle());
        contributionRestOutput.setCreatedAt(contribution.getCreatedAt());
        contributionRestOutput.setUser(new AuthUser(contribution.getUserId(), contribution.getUsername()));
        contributionRestOutput.setPoints(contribution.getPoints());
        contributionRestOutput.setCommentsLength(contribution.getNumberOfComments());
        contributionRestOutput.setLiked(!contribution.getCanLike());
        if (contribution.getContributionTopParentId() == null) {
            contributionRestOutput.setContributionTopParentId(0L);
        } else {
            contributionRestOutput.setContributionTopParentId(contribution.getContributionTopParentId());
        }
        contributionRestOutput.setContributionParentId(contribution.getContributionIdRef());
        return contributionRestOutput;
    }

}
