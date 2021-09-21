package com.svvc.hackernews.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.svvc.hackernews.controllers.base.BaseController;
import com.svvc.hackernews.model.CommentForm;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.user.UserService;


@Controller
@RequestMapping("/item")
public class CommentsController extends BaseController
{

    final ContributionsService contributionsService;
    final UserService userService;

    @Autowired
    public CommentsController(final ContributionsService contributionsService,
                              final UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @GetMapping()
    public String create(@RequestParam(name = "id", defaultValue = "0") Long id,
                         @RequestParam(name = "error", defaultValue = "") String error,
                         Model model,
                         Principal principal)
    {
        if (Objects.equals(error, "text"))
        {
            error = "Text must not be null";
        }
        return getComponents(id, model, error, getIdUser(principal));
    }

    @PostMapping("/create")
    public String createComment(@RequestParam(name = "id", defaultValue = "0") Long id,
                                @Valid CommentForm input,
                                BindingResult bindingResult,
                                Principal principal)
    {
        input.setContributionIdRef(id);
        input.setContributionTopParentId(id);
        if (bindingResult.hasErrors())
        {
            return "item";
        }
        else
        {
            String errorText = contributionsService.createComment(input, getIdUser(principal));
            if (errorText == null)
            {
                return "redirect:/item?id=" + id;
            }
            else
            {
                return "redirect:/item?id=" + id + "&error=text";
            }
        }
    }

    private String getComponents(Long id, Model model, String errorText, String userName)
    {
        // Contribution main
        Contribution contribution = contributionsService.getContributionById(id, userName).get();
        model.addAttribute("contribution", contribution);

        // Top parent contribution
        Contribution topParentContribution = null;
        if (contribution.getContributionIdRef() != 0)
        {
            topParentContribution = contributionsService.getContributionById(contribution.getContributionTopParentId(), userName).get();
        }
        model.addAttribute("topParentContribution", topParentContribution);

        // Comments of the main contribution
        List<Contribution> comments = contributionsService.getContributionsCommentsAndReplies(id, userName);
        model.addAttribute("contributionComments", comments);

        // Indents of the comments
        model.addAttribute("commentsWidths", contributionsService.getWidthsOfComments(comments));

        // Other attributes
        model.addAttribute("pageName", "item");
        if (errorText != null && !errorText.isEmpty())
        {
            model.addAttribute("errorText", errorText);
        }
        model.addAttribute("user", Optional.ofNullable(userName).map(userService::getUser).map(Optional::get).orElse(null));
        return "item";
    }
}
