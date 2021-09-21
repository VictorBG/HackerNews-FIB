package com.svvc.hackernews.controllers.rest;

import com.svvc.hackernews.model.FacebookObject;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.svvc.hackernews.common.AuthAwareRestController;
import com.svvc.hackernews.controllers.base.BaseRestController;
import com.svvc.hackernews.model.AuthUser;
import com.svvc.hackernews.model.restoutput.ProfileUpdateRestInput;
import com.svvc.hackernews.model.restoutput.UserRestOutput;
import com.svvc.hackernews.service.contributions.ContributionsService;
import com.svvc.hackernews.service.user.UserService;
import com.svvc.hackernews.service.user.mapper.UserMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@AuthAwareRestController
@RequestMapping("/api")
@Api(tags = "User")
@CrossOrigin()
public class UserRestController extends BaseRestController
{

    final ContributionsService contributionsService;
    final UserService userService;

    @Autowired
    public UserRestController(
        ContributionsService contributionsService, UserService userService)
    {
        this.contributionsService = contributionsService;
        this.userService = userService;
    }

    @ApiOperation(
        value = "Retrieves the user profile from a userid, if the userid is equal to the current user, it shows their profile, in the other case it shows the profile of the user provided",
        produces = "application/json")
    @GetMapping("/user/{idUser}")
    public UserRestOutput getUser(@PathVariable(name = "idUser") String idUser,
                                  final AuthUser user)
    {
        if (user != null && Objects.equals(user.id, idUser))
        {
            return userService.getUser(idUser).map(UserMapper::mapUserToUserRestOutput).orElse(null);
        }
        else
        {
            UserRestOutput response = userService.getUser(idUser).map(UserMapper::mapExternalUserToExternalUserRestOutput).orElse(null);
            if (response == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            else
            {
                return response;
            }
        }

    }

    @ApiOperation(value = "Updates all the profile values provided in the form", produces = "application/json")
    @PutMapping("/user")
    public UserRestOutput updateUser(@RequestBody ProfileUpdateRestInput userProfileForm, final AuthUser user)
    {
        userService.updateProfile(UserMapper.mapUpdateInputToUserForm(userProfileForm), user.id);
        return userService.getUser(user.id).map(UserMapper::mapUserToUserRestOutput).orElse(null);
    }
}
