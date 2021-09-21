package com.svvc.hackernews.controllers.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.svvc.hackernews.common.AuthAwareRestController;
import com.svvc.hackernews.controllers.base.BaseRestController;
import com.svvc.hackernews.model.AuthUser;
import com.svvc.hackernews.model.CommentForm;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.model.ContributionForm;
import com.svvc.hackernews.model.restoutput.CommentRest;
import com.svvc.hackernews.model.restoutput.ContributionRestOutput;
import com.svvc.hackernews.model.restoutput.InsertResult;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.contributions.mapper.ContributionsMapper;
import com.svvc.hackernews.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@AuthAwareRestController
@RequestMapping("/api")
@Api(tags = "Comments")
@CrossOrigin()
public class CommentsRestController extends BaseRestController
{

    final ContributionsService contributionsService;
    final UserService userService;

    @Autowired
    public CommentsRestController(final ContributionsService contributionsService,
                                  final UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @ApiOperation(value = "Retrieves a contribution by itemid. If the itemid does not exist, an empty json is returned",
        produces = "application/json")
    @GetMapping("/item")
    public ContributionRestOutput getItem(@RequestParam(name = "itemid", defaultValue = "0") Long id,
                                          final AuthUser user)
    {
        ContributionRestOutput result = contributionsService.getContributionById(id, getIdUser(user))
            .map(c -> {
                ContributionRestOutput contributionRest = ContributionsMapper.mapContributionToContributionRestOutput(c);
                // Find all comments
                List<Contribution> comments = contributionsService.getContributionsCommentsAndReplies(id, getIdUser(user));
                // Find all tree lengths of comments
                HashMap<Long, Integer> contributionAndWidths = contributionsService.getWidthsOfComments(comments);
                // Set comments to the contribution rest
                contributionRest.setComments(contributionsService.getContributionsCommentsAndReplies(id, getIdUser(user)).stream().map(
                    ContributionsMapper::mapContributionToContributionRestOutput).collect(Collectors.toList()));
                // Apply tree length to every comment
                for (int i = 0; i < contributionRest.getComments().size(); i++)
                {
                    contributionRest.getComments().get(i).setTreeLength(contributionAndWidths.get(contributionRest.getComments().get(i).getId()));
                }
                return contributionRest;
            }).orElse(null);

        if (result != null)
        {
            return result;
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found");
        }
    }

    @ApiOperation(value = "Creates an item", produces = "application/json")
    @PostMapping("/item")
    public InsertResult<ContributionRestOutput> createItem(@RequestBody ContributionForm contribution,
                                                           final AuthUser user)
    {
        if (contribution.getTitle() == null)
        {
            contribution.setTitle("");
        }

        if (contribution.getUrl() == null)
        {
            contribution.setUrl("");
        }

        if (contribution.getText() == null)
        {
            contribution.setText("");
        }
        try
        {
            Contribution c = contributionsService.createPost(contribution, getIdUser(user));
            return Optional.ofNullable(c)
                .map(ContributionsMapper::mapContributionToContributionRestOutput)
                .map(InsertResult::successful)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "There has been an error creating the new contribution"));

        }
        catch (ResponseStatusException exception)
        {
            if (exception.getStatus() == HttpStatus.CONFLICT)
            {
                return InsertResult.failed(getItem(Long.valueOf(Objects.requireNonNull(exception.getReason())), user));
            }
            else
            {
                throw exception;
            }
        }
    }

    @ApiOperation(value = "Creates a comment in a contribution", produces = "application/json")
    @PostMapping("/item/comment")
    public ContributionRestOutput createComment(@RequestBody CommentRest comment,
                                                final AuthUser user)
    {
        Optional<Contribution> parent = contributionsService.getContributionById(comment.getParentId(), getIdUser(user));
        if (parent.isPresent())
        {
            CommentForm cf = new CommentForm();
            // If top parent id is not null, we have to do a reply.
            if (parent.get().getContributionTopParentId() != null)
            {
                cf.setContributionTopParentId(parent.get().getContributionTopParentId());
                // Contribution does not have a top parent id, so we have to do a comment.
            }
            else
            {
                cf.setContributionTopParentId(comment.getParentId());
            }
            cf.setContributionIdRef(comment.getParentId());
            cf.setText(comment.getText());

            String error = contributionsService.createComment(cf, getIdUser(user));
            if (error != null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
            }
            return getItem(comment.getParentId(), user);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The contribution parent does not exists.");
        }
    }

    @ApiOperation(value = "Upvote a contribution of any kind", produces = "application/json")
    @PostMapping("/item/{id}/like")
    public ContributionRestOutput vote(@PathVariable(name = "id") Long id,
                                       final AuthUser user)
    {
        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "You must be logged to upvote Contributions");
        }
        int result = contributionsService.likeContribution(user.id, id);
        if (result == 0)
        {
            return contributionsService.getContributionById(id, user.id)
                .map(ContributionsMapper::mapContributionToContributionRestOutput).orElse(null);
        }
        else if (result == 1)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Contribution you want to upvote does not exist");
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "The Contribution was already voted");
        }
    }

    @ApiOperation(value = "Unvote a contribution of any kind", produces = "application/json")
    @DeleteMapping("/item/{id}/like")
    public ContributionRestOutput unvote(@PathVariable(name = "id") Long id,
                                         final AuthUser user)
    {
        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "You must be logged to unvote Contributions");
        }
        int result = contributionsService.deleteLike(user.id, id);
        if (result == 0)
        {
            return contributionsService.getContributionById(id, user.id)
                .map(ContributionsMapper::mapContributionToContributionRestOutput).orElse(null);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found");
        }

    }
}
