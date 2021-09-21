package com.svvc.hackernews.controllers.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.svvc.hackernews.common.AuthAwareRestController;
import com.svvc.hackernews.controllers.base.BaseRestController;
import com.svvc.hackernews.model.AuthUser;
import com.svvc.hackernews.model.Contribution;
import com.svvc.hackernews.model.restoutput.ContributionRestOutput;
import com.svvc.hackernews.model.restoutput.PageListResponse;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.contributions.mapper.ContributionsMapper;
import com.svvc.hackernews.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@AuthAwareRestController
@RequestMapping("/api")
@Api(tags = "Lists")
@CrossOrigin()
public class MainRestController extends BaseRestController
{
    final ContributionsService contributionsService;
    final UserService userService;

    @Autowired
    public MainRestController(final ContributionsService contributionsService,
                              final UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @ApiOperation(value = "Retrieves the main contributions regardless of what is their type", produces = "application/json")
    @GetMapping("/main")
    public PageListResponse<ContributionRestOutput> index(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getMainContributions(page, getIdUser(user)));
    }

    @ApiOperation(value = "Retrieves contributions regardless of what is their type sorted by creation date in desc order, the newest are first",
        produces = "application/json")
    @GetMapping("/newest")
    public PageListResponse<ContributionRestOutput> newest(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getNewestContributions(page, getIdUser(user)));
    }

    @ApiOperation(value = "Retrieves contributions comments or reply of a given user", produces = "application/json")
    @GetMapping("/threads")
    public PageListResponse<ContributionRestOutput> threads(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(name = "user", defaultValue = "") String idUser,
                                                            final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getAllThreads(page, idUser));
    }

    @ApiOperation(value = "Retrieves contributions of type URL or ASK of a given user", produces = "application/json")
    @GetMapping("/submissions")
    public PageListResponse<ContributionRestOutput> submissions(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(name = "user", defaultValue = "") String idUser,
                                                                final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getContributions(page, idUser, getIdUser(user)));
    }

    @ApiOperation(value = "Retrieves contributions of type URL sorted by points, the highest are first",
        produces = "application/json")
    @GetMapping("/urls")
    public PageListResponse<ContributionRestOutput> urls(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getUrlsContributions(page, getIdUser(user)));
    }

    @ApiOperation(value = "Retrieves contributions of type ASK sorted by points, the highest are first",
        produces = "application/json")
    @GetMapping("/ask")
    public PageListResponse<ContributionRestOutput> ask(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                        final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getAskContributions(page, getIdUser(user)));
    }

    private PageListResponse<ContributionRestOutput> getPageRequest(int page, List<Contribution> list)
    {
        return PageListResponse.of(page, list.stream()
            .map(ContributionsMapper::mapContributionToContributionRestOutput)
            .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Retrieves contributions voted by the user", produces = "application/json")
    @GetMapping("/upVotedSubmissions")
    public PageListResponse<ContributionRestOutput> upVotedSubmissions(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                       final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getExternalVotedContributions(page, user.id));
    }

    @ApiOperation(value = "Retrieves contributions that are comments or replies voted by the user", produces = "application/json")
    @GetMapping("/upVotedComments")
    public PageListResponse<ContributionRestOutput> upVotedComments(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                    final AuthUser user)
    {
        return getPageRequest(page, contributionsService.getExternalVotedComments(page, user.id));
    }

}
