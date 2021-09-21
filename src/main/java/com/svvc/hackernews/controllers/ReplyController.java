package com.svvc.hackernews.controllers;

import java.security.Principal;
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
@RequestMapping("/reply")
public class ReplyController extends BaseController
{

    final ContributionsService contributionsService;
    final UserService userService;
    private Long gotoId;
    private Contribution parent;
    private Contribution topParent;

    @Autowired
    public ReplyController(final ContributionsService contributionsService,
                           final UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create(@RequestParam(name = "id", defaultValue = "0") Long id,
                         @RequestParam(name = "goto", defaultValue = "0") Long itemId,
                         CommentForm commentForm,
                         Model model,
                         Principal principal)
    {
        String userName = getIdUser(principal);
        this.gotoId = itemId;
        this.parent = contributionsService.getContributionById(itemId, userName).get();
        this.topParent = contributionsService.getContributionById(parent.getContributionTopParentId(), userName).get();
        model.addAttribute("user",
            userService.getUser(principal != null ? principal.getName() : "").orElse(null));
        return getComponents(id, model, null, userName);
    }

    @PostMapping()
    public String createComment(@RequestParam(name = "id", defaultValue = "0") Long id,
                                @Valid CommentForm input,
                                BindingResult bindingResult,
                                Model model,
                                Principal principal)
    {
        input.setContributionIdRef(gotoId);
        Contribution parent = contributionsService.getContributionById(gotoId, null).get();
        Contribution topParent = contributionsService.getContributionById(parent.getContributionTopParentId(), null).get();
        input.setContributionTopParentId(topParent.getContributionId());
        if (bindingResult.hasErrors())
        {
            return "reply";
        }
        else
        {
            String errorText = contributionsService.createComment(input, getIdUser(principal));
            if (errorText == null)
            {
                return "redirect:/item?id=" + id + "#" + gotoId;
            }
            else
            {
                return getComponents(id, model, errorText, getIdUser(principal));
            }
        }
    }

    private String getComponents(Long id, Model model, String errorText, String userName)
    {
        model.addAttribute("contribution", contributionsService.getContributionById(id, userName).get());
        model.addAttribute("parentContribution", this.parent);
        model.addAttribute("topParentContribution", this.topParent);
        model.addAttribute("originalGoto", this.gotoId);
        model.addAttribute("user", Optional.ofNullable(userName).map(userService::getUser).map(Optional::get).orElse(null));
        if (errorText != null)
        {
            model.addAttribute("errorText", errorText);
        }
        return "reply";
    }
}
