package com.svvc.hackernews.service.contributions;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.web.server.ResponseStatusException;

import com.svvc.hackernews.model.CommentForm;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.model.ContributionForm;


public interface ContributionsService
{
    /**
     * Returns the main contributions that are all sorted by points
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getMainContributions(Integer page, String userName);

    /**
     * Returns the newest contributions that are all sorted by created date desc
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getNewestContributions(Integer page, String userName);

    /**
     * Returns the most liked {@link Contribution}s of type ask ({@link Contribution#getLink() == null}
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getAskContributions(Integer page, String userName);

    /**
     * Returns the most liked {@link Contribution}s of type url ({@link Contribution#getLink() != null}
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getUrlsContributions(Integer page, String userName);

    /**
     * Returns the threads from a specific user
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getAllThreads(Integer page, String userName);

    /**
     * Returns the contributions created from a specific user
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getContributions(Integer page, String userName, String loggedUser);

    /**
     * Returns the contributions created from a specific user that has points
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getVotedContributions(Integer page, String userName);

    /**
     * Returns the contributions that the user has voted
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getExternalVotedContributions(Integer page, String userName);

    /**
     * Returns the comments that the user has voted
     *
     * @param page      The current page of the pageable request
     * @param userName  Name of the user
     * @return A list of {@link Contribution}s
     */
    List<Contribution> getExternalVotedComments(Integer page, String userName);

    /**
     * Returns a contributions given an id.
     *
     * @param contributionId    The id of the desired contribution
     * @return The contribution associated with the id
     */
    Optional<Contribution> getContributionById(Long contributionId, String userName);

    /**
     *
     * Search all the comments and replies of a given contribution.
     * Its a recursive function that once it finds the first comment, she calls itself with the comment found
     * to search the replies. Then does exactly the same for the replies found...
     * Finally, it returns an ordered tree of comments and replies.
     *
     * @param contributionId The id of the desired contribution to get the comments and replies.
     * @param user  The user
     * @return a List of Contribution ordered by comments and replies.
     */
    List<Contribution> getContributionsCommentsAndReplies(Long contributionId, String user);

    /**
     * Given a List of Contributions the function returns a HashMap that contains the
     * Given a List of Contributions the function returns a HashMap that contains the
     * id of the Contribution and his width to be applied in the html.
     * The width is calculated adding +40 to the parent width comment (if any).
     *
     * @param comments the comments to calculate the width.
     * @return A map containing the width for every Contribution
     */
    HashMap<Long, Integer> getWidthsOfComments(List<Contribution> comments);

    /**
     * Creates a post with the given {@param input} and the given {@param String} (which is the user).
     *
     * It validates the input and returns an error message in case some of the inputs are not correct.
     *
     * Once validated, the input is inserted into the database.
     *
     * @param input         The input to insert into database
     * @param idUser     The user who is creating the post
     * @return The id of the contribution
     */
    Contribution createPost(ContributionForm input, String idUser) throws ResponseStatusException;

    /**
     * Creates a comment. Similar to {@link #createPost(ContributionForm, String)}
     *
     * @param input         The input to insert into database
     * @param idUser     The user who is creating the post
     * @return Error string if there is an error on the validation part or null otherwise
     */
    String createComment(CommentForm input, String idUser);

    /**
     * Likes a contribution for the given user({@param idUser})
     * @param idUser         The user that likes
     * @param contributionId    The id of the contribution liked
     * @return 0 if the operation was successful, 1 if the provided contribution does not exist and 2 if the like could not be added due it exists
     */
    int likeContribution(String idUser, Long contributionId);

    /**
     * Removes a like.
     * @param idUser         The user that liked
     * @param contributionId    The id of the contribution liked
     * @return 0 if the operation was successful, 1 if the provided contribution does not exist and 2 if the like could not be removed due it was non
     * existent
     */
    int deleteLike(String idUser, Long contributionId);

}
