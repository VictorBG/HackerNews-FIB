package com.svvc.hackernews.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.svvc.hackernews.model.ContributionForm;
import com.svvc.hackernews.service.contributions.ContributionsService;


@Controller
@RequestMapping("/create")
public class CreateController
{

    final ContributionsService contributionsService;

    @Autowired
    public CreateController(final ContributionsService contributionsService)
    {
        this.contributionsService = contributionsService;
    }

    @GetMapping()
    public String create()
    {
        return "create";
    }

    @PostMapping()
    public String createPost(@Valid ContributionForm input,
                             BindingResult bindingResult,
                             Model model,
                             Principal principal)
    {
        if (bindingResult.hasErrors())
        {
            return "create";
        }
        else
        {
            try
            {
                contributionsService.createPost(input, principal.getName());
                return "redirect:/newest";
            }
            catch (ResponseStatusException error)
            {
                String errorText = error.getReason();

                if (error.getStatus() == HttpStatus.CONFLICT)
                {
                    return "redirect:/item?id=" + errorText;
                }
                else
                {
                    model.addAttribute("errorText", errorText);
                    return "create";
                }
            }
        }
    }
}
