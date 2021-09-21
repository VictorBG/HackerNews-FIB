package com.svvc.hackernews.service.contributions.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.svvc.hackernews.model.CommentForm;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.model.ContributionDao;
import com.svvc.hackernews.model.ContributionForm;
import com.svvc.hackernews.model.Likes;
import com.svvc.hackernews.model.User;
import com.svvc.hackernews.repository.ContributionsRepository;
import com.svvc.hackernews.repository.LikesRepository;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.contributions.mapper.ContributionsMapper;
import com.svvc.hackernews.service.user.UserService;
import com.svvc.hackernews.utils.StringUtils;


/**
 * TODO: Technical debt: Principal should be decoupled from this service
 */
@Service
public class ContributionsServiceImpl implements ContributionsService
{

    public static final Integer PAGE_QUANTITY = 30;

    final ContributionsRepository contributionsRepository;
    final UserService userService;
    final LikesRepository likesRepository;

    public ContributionsServiceImpl(final ContributionsRepository contributionsRepository,
                                    UserService userService, LikesRepository likesRepository)
    {
        this.contributionsRepository = contributionsRepository;
        this.userService = userService;
        this.likesRepository = likesRepository;

    }

    /**
     * @see ContributionsService#getMainContributions(Integer, String)
     */
    @Override
    public List<Contribution> getMainContributions(final Integer page, String userName)
    {
        return getPageableContributions(page, userName, contributionsRepository::findAllByOrderByPointsDesc);
    }

    /**
     * @see ContributionsService#getNewestContributions(Integer, String)
     */
    @Override
    public List<Contribution> getNewestContributions(final Integer page, String userName)
    {
        return getPageableContributions(page, userName, contributionsRepository::findAllByOrderByCreatedAtDesc);
    }

    /**
     * @see ContributionsService#getAskContributions(Integer, String)
     */
    @Override
    public List<Contribution> getAskContributions(final Integer page, String userName)
    {
        return getPageableContributions(page, userName, contributionsRepository::getAllAsk);
    }

    /**
     * @see ContributionsService#getUrlsContributions(Integer, String)
     */
    @Override
    public List<Contribution> getUrlsContributions(final Integer page, String userName)
    {
        return getPageableContributions(page, userName, contributionsRepository::getAllUrl);
    }

    /**
     * @see ContributionsService#getAllThreads(Integer, String)
     */
    @Override
    public List<Contribution> getAllThreads(final Integer page, String userName)
    {
        return getPageableContributions(page, userName, pageRequest -> contributionsRepository.findAllThreadsByUser(userName, pageRequest));
    }

    /**
     * @see ContributionsService#getContributions(Integer, String, String)
     */
    @Override
    public List<Contribution> getContributions(Integer page, String userName, String loggedUser)
    {
        return getPageableContributions(page, loggedUser, pageRequest -> contributionsRepository.findAllContributionsById(userName));
    }

    /**
     * @see ContributionsService#getVotedContributions(Integer, String)
     */
    @Override
    public List<Contribution> getVotedContributions(Integer page, String userName)
    {
        return getPageableContributions(page, userName, pageRequest -> contributionsRepository.findAllContributionsVotedById(userName));
    }

    /**
     * @see ContributionsService#getExternalVotedContributions(Integer, String)
     */
    @Override
    public List<Contribution> getExternalVotedContributions(Integer page, String userName)
    {
        return getExternalVotedContributions(page, userName, contributionDao -> contributionDao.getContributionIdRef() == 0);
    }

    /**
     * @see ContributionsService#getExternalVotedComments(Integer, String)
     */
    @Override
    public List<Contribution> getExternalVotedComments(Integer page, String userName)
    {
        return getExternalVotedContributions(page, userName, contributionDao -> contributionDao.getContributionIdRef() != 0);
    }

    /**
     * @see ContributionsService#getContributionById(Long, String)
     */
    @Override
    public Optional<Contribution> getContributionById(Long contributionId, String userName)
    {
        List<Long> ids = Collections.singletonList(contributionId);
        return contributionsRepository.findAllById(ids)
            .stream()
            .map(contributionDao -> {
                Contribution contribution = ContributionsMapper.mapDaoToContribution(contributionDao);
                contribution.setCanLike(canLikeContribution(contribution.getContributionId(), userName));
                return contribution;
            })
            .findFirst();
    }

    /**
     * @see ContributionsService#getContributionsCommentsAndReplies(Long, String)
     */
    @Override
    public List<Contribution> getContributionsCommentsAndReplies(Long contributionId, String user)
    {
        List<Contribution> commentsOrdered = new ArrayList<>();
        // Search all comments (if contributionId is an Ask or Url) or all replies (if contributionId is a comment/reply)
        List<Contribution> commentsOrRepliesOfContribution = contributionsRepository
            .findAllByContributionIdRefOrderByPointsDesc(contributionId)
            .stream()
            .map(contributionDao -> {
                Contribution contribution = ContributionsMapper.mapDaoToContribution(contributionDao);
                contribution.setCanLike(canLikeContribution(contributionDao.getId(), user));
                return contribution;
            }).collect(Collectors.toList());

        // Iterate the comments to order them correctly.
        for (Contribution comment : commentsOrRepliesOfContribution)
        {
            // If the comment has any newline, replace it with <br> to show it in the html.
            String contentConSaltosDeLinea = comment.getContent();
            String contentConBR = contentConSaltosDeLinea.replace("\n", "<br>");
            comment.setContent(contentConBR);
            commentsOrdered.add(comment);
            // Once the comment is added, we search the possible replies of that comment recursively,
            // so we can finally get all of them ordered correctly.
            commentsOrdered.addAll(getContributionsCommentsAndReplies(comment.getContributionId(), user));
        }

        return commentsOrdered;
    }

    /**
     * @see ContributionsService#getWidthsOfComments(List)
     */
    public HashMap<Long, Integer> getWidthsOfComments(List<Contribution> comments)
    {
        HashMap<Long, Integer> commentsWidths = new HashMap<>();
        for (Contribution comment : comments)
        {
            int width = 0;
            // Check if already exists a parent comment for this contribution.
            if (commentsWidths.containsKey(comment.getContributionIdRef()))
            {
                width = commentsWidths.get(comment.getContributionIdRef()) + 40;
            }
            // Add the comment to the map because maybe we can find another reply of that comment
            // and we will need to apply the propper width.
            commentsWidths.put(comment.getContributionId(), width);
        }
        return commentsWidths;
    }

    /**
     * @see ContributionsService#createPost(ContributionForm, String)
     */
    @Override
    public Contribution createPost(final ContributionForm input, final String idUser)
    {
        if (input.getTitle().trim().isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must not be null");
        }

        if (input.getUrl().trim().isEmpty() && input.getText().trim().isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url or text must not be null");
        }

        if (!input.getUrl().trim().isEmpty() && !input.getText().trim().isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fill only url OR text");
        }

        if (input.getTitle().length() > 80)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title too long, max 80 chars");
        }

        if (!input.getUrl().trim().isEmpty() && !StringUtils.isURL(input.getUrl()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Url field does not have a valid url");
        }

        if (!input.getUrl().trim().isEmpty())
        {
            ContributionDao contributionDao = contributionsRepository.findContributionDaoByURL(input.getUrl());
            if (contributionDao != null)
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.valueOf(contributionDao.getId()));
            }
        }

        return userService.getUser(idUser)
            .map(user -> {
                ContributionDao saved = contributionsRepository.insertPost(ContributionsMapper.mapContributionInputToContributionDao(input), user);
                return ContributionsMapper.mapDaoToContribution(saved);
            }).orElse(null);
    }

    /**
     * @see ContributionsService#createComment(CommentForm, String)
     */
    @Override
    public String createComment(final CommentForm input, String idUser)
    {
        if (input.getText().trim().isEmpty())
        {
            return "Text must not be null";
        }

        userService.getUser(idUser).ifPresent(user ->
            contributionsRepository.insertPost(ContributionsMapper.mapCommentInputToContributionDao(input), user));
        return null;
    }

    /**
     * @see ContributionsService#likeContribution(String, Long)
     */
    @Override
    public int likeContribution(@NonNull String userName, Long contributionId)
    {
        int error_manager = 0;
        Optional<Likes> l = likesRepository.findIfLikeExist(userName, contributionId);
        if (!l.isPresent())
        {
            Optional<User> user = userService.getUser(userName);
            if (user.isPresent())
            {
                final Likes newLike = new Likes();
                newLike.setUserId(user.get());
                newLike.setContributionId(contributionsRepository.findContributionDaoById(contributionId));
                newLike.setLikeCreatedAt(System.currentTimeMillis());
                likesRepository.save(newLike);

                /* The points are added after creating the like to be consistent between data, if a fail
                 occurs when creating a new like we do not want to be inconsistent showing a number of
                 likes that are not correct, therefore if something fails above, the following code
                 will never be executed*/
                ContributionDao contributionToLike = contributionsRepository.findContributionDaoById(contributionId);
                if (contributionToLike == null)
                {
                    error_manager = 1;
                }
                else
                {
                    contributionToLike.addVote();
                    contributionsRepository.save(contributionToLike);
                }
            }
            return error_manager;
        }
        return 2;
    }

    /**
     * @see ContributionsService#deleteLike(String, Long)
     */
    @Override
    public int deleteLike(String idUser, Long contributionId)
    {
        int error_manage = 0;
        Optional<Likes> l = likesRepository.findIfLikeExist(idUser, contributionId);
        if (l.isPresent())
        {
            likesRepository.deleteById(l.get().getLikeId());

            // The same as likeContribution, if something fails when removing the like, we want to
            // be consistent with the displayed data
            ContributionDao aux = contributionsRepository.findContributionDaoById(contributionId);
            if (aux == null)
            {
                error_manage = 1;
            }
            else
            {
                aux.removeVote();
                contributionsRepository.save(aux);
            }
            return error_manage;
        }
        return 1;
    }

    /**
     * Returns the given contributions through the {@param supplier} and maps them from {@link ContributionDao} (which are the contributions
     * obtained directly from database) to {@link Contribution}, so the applications can use them properly.
     *
     * It also adds some useful properties to each contributions, such as if it can be liked, based on the {@param userName}, the number of comments
     * it has and applies a {@link PageRequest} to the query to segment the result in pages (by default size = {@link #PAGE_QUANTITY})
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    private List<Contribution> getPageableContributions(Integer page, String userName, Function<PageRequest, List<ContributionDao>> supplier)
    {
        return supplier.apply(PageRequest.of(page, PAGE_QUANTITY))
            .stream()
            .map(contributionDao -> {
                Contribution contribution = ContributionsMapper.mapDaoToContribution(contributionDao);
                contribution.setCanLike(canLikeContribution(contribution.getContributionId(), userName));
                contribution.setNumberOfComments(contributionsRepository.getNumberOfComments(contributionDao.getId()));
                if (contribution.getContributionTopParentId() != null)
                {
                    contribution.setContributionTopParentTitle(contributionsRepository
                        .getOne(contribution.getContributionTopParentId()).getTitle());
                }
                return contribution;
            }).collect(Collectors.toList());
    }

    /**
     * Returns a boolean based on if the user can like the given contribution.
     *
     * If no user is given, it returns true; if a user is given, it returns a boolean based on if a like exists on the database for the given
     * user and the contributions, if yes, it returns false, true otherwise.
     *
     * @param contributionId        The contribution to which check if it can be liked by the user
     * @param userName              The user that wants to know if it can like the given contribution
     * @return Boolean
     */
    private boolean canLikeContribution(@NonNull Long contributionId, String userName)
    {
        return Optional.ofNullable(userName).map(user -> !likesRepository.findIfLikeExist(userName, contributionId).isPresent()).orElse(true);
    }

    /**
     * Returns a list of contributions that have been voted by the user {@param userName} and applies the given filter to them.
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @param filter    Filter to apply to the result list
     * @return A list of {@link Contribution}s
     */
    private List<Contribution> getExternalVotedContributions(final Integer page,
                                                             final String userName,
                                                             final Function<ContributionDao, Boolean> filter)
    {
        return getPageableContributions(page,
            userName,
            pageRequest -> likesRepository.findAllContributionsVotedById(userName)
                .stream()
                .map(Likes::getContributionId)
                .filter(filter::apply)
                .collect(Collectors.toList()));
    }
}
