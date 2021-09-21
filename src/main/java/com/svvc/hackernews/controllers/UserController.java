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

import com.svvc.hackernews.model.UserProfileForm;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.user.UserService;
import com.svvc.hackernews.utils.NumberUtils;


@Controller
@RequestMapping("/user")
public class UserController
{
    final ContributionsService contributionsService;
    final UserService userService;

    @Autowired
    public UserController(
        ContributionsService contributionsService, UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @GetMapping()
    public String user(Model model, @RequestParam(name = "id", defaultValue = "-1") String id, Principal principal)
    {
        if (!NumberUtils.isNumeric(id))
        {
            return "redirect:/";
        }

        model.addAttribute("user", userService.getUser(id).get());
        if (principal != null && principal.getName().equals(id))
        {
            return "userprofile";
        }
        else if (userService.getUser(id).isPresent())
        {
            if (principal != null)
            {
                model.addAttribute("myUser", userService.getUser(principal.getName()).get());
            }
            else
            {
                model.addAttribute("myUser", null);
            }
            return "externaluserprofile";
        }
        else
        {
            return "redirect:/";
        }
    }

    @PostMapping()
    public String updateProfile(@Valid UserProfileForm input, BindingResult bindingResult, Principal principal)
    {
        if (!bindingResult.hasErrors())
        {
            userService.updateProfile(input, principal.getName());
        }
        return "redirect:/user?id=" + principal.getName();
    }

    @GetMapping("/likes")
    public String vote(@RequestParam(name = "id", defaultValue = "0") Long id,
                       @RequestParam(name = "goto", defaultValue = "/") String goTo,
                       Principal principal)
    {
        Optional.ofNullable(principal).ifPresent(p -> contributionsService.likeContribution(p.getName(), id));
        return "redirect:" + goTo;
    }

    @GetMapping("/unlikes")
    public String unvote(@RequestParam(name = "id", defaultValue = "0") Long id,
                         @RequestParam(name = "goto", defaultValue = "/") String goTo,
                         Principal principal)
    {
        contributionsService.deleteLike(principal.getName(), id);
        return "redirect:" + goTo;
    }

}
