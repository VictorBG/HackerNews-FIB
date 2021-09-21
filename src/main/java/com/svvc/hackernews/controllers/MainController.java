package com.svvc.hackernews.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.svvc.hackernews.controllers.base.BaseController;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.model.User;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.user.UserService;


@Controller
public class MainController extends BaseController
{

    final ContributionsService contributionsService;
    final UserService userService;

    @Autowired
    public MainController(final ContributionsService contributionsService,
                          UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "0") Integer page,
                        Principal p,
                        Model model,
                        OAuth2Authentication authentication)
    {
        Optional.ofNullable(authentication).ifPresent(userService::createUser);

        return getPage("", page, model, authentication,
            () -> contributionsService.getMainContributions(page, getIdUser(p)));
    }

    @GetMapping("/newest")
    public String newest(@RequestParam(name = "page", defaultValue = "0") Integer page,
                         Model model,
                         Principal principal)
    {
        return getPage("/newest", page, model, principal,
            () -> contributionsService.getNewestContributions(page, getIdUser(principal)));
    }

    @GetMapping("/urls")
    public String urls(@RequestParam(name = "page", defaultValue = "0") Integer page,
                       Model model,
                       Principal principal)
    {
        return getPage("/urls", page, model, principal,
            () -> contributionsService.getUrlsContributions(page, getIdUser(principal)));
    }

    @GetMapping("/ask")
    public String ask(@RequestParam(name = "page", defaultValue = "0") Integer page,
                      Model model,
                      Principal principal)
    {
        return getPage("/ask", page, model, principal,
            () -> contributionsService.getAskContributions(page, getIdUser(principal)));
    }

    @GetMapping("/threads")
    public String threads(@RequestParam(name = "page", defaultValue = "0") Integer page,
                          @RequestParam(name = "user", defaultValue = "") String idUser,
                          Model model,
                          Principal principal)
    {
        getPage("/threads", page, model, principal,
            () -> contributionsService.getAllThreads(page, Objects.equals(idUser, "") ? getIdUser(principal) : idUser));
        return "threads";
    }

    @GetMapping("/submissions")
    public String allSubmissions(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "user", defaultValue = "") String idUser,
                                 Model model,
                                 Principal principal)
    {

        String userId = Objects.equals(idUser, "") ? getIdUser(principal) : idUser;
        userService.getUser(userId).map(User::getUsername).ifPresent(userName -> model.addAttribute("submissionsUsername", userName));
        userService.getUser(userId).map(User::getId).ifPresent(id -> model.addAttribute("submissionsUserId", id));

        return getPage("/submissions", page, model, principal,
            () -> contributionsService.getContributions(page, userId, getIdUser(principal)));
    }

    @GetMapping("/upVotedSubmissions")
    public String allUpVotedSubmissions(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                        Model model,
                                        Principal principal)
    {
        getPage("/upVotedSubmissions", page, model, principal,
            () -> contributionsService.getExternalVotedContributions(page, getIdUser(principal)));
        return "upvotedsubmissions";
    }

    @GetMapping("/upVotedComments")
    public String allupVotedComments(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     Model model,
                                     Principal principal)
    {
        getPage("/upVotedComments", page, model, principal,
            () -> contributionsService.getExternalVotedComments(page, getIdUser(principal)));
        return "upvotedcomments";
    }

    private String getPage(String pageName, Integer page, Model model, Principal principal, Supplier<List<Contribution>> supplier)
    {
        List<Contribution> contributions = supplier.get();
        model.addAttribute("contributions", contributions);
        Map<Long, Integer> contributionsAndComments = contributions.stream()
            .collect(Collectors.toMap(Contribution::getContributionId, Contribution::getNumberOfComments));
        model.addAttribute("contributionsComments", contributionsAndComments);
        model.addAttribute("page", page);
        model.addAttribute("pageName", pageName);
        if (principal != null)
        {
            model.addAttribute("user", userService.getUser(principal.getName()).get());
        }
        return "main";
    }
}
