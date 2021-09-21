package com.svvc.hackernews.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svvc.hackernews.model.FacebookObject;
import com.svvc.hackernews.model.restoutput.CompleteUserRestOutput;
import com.svvc.hackernews.service.user.UserService;
import com.svvc.hackernews.service.user.mapper.UserMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api")
@Api(tags = "Login")
@CrossOrigin()
public class LoginRestController
{

    final UserService userService;

    @Autowired
    public LoginRestController(UserService userService)
    {
        this.userService = userService;
    }

    @ApiOperation(value = "Logs in the user or creates the user", produces = "application/json")
    @PostMapping("/user")
    public CompleteUserRestOutput createUser(@RequestBody FacebookObject object)
    {
        userService.createUser(object);
        return userService.getUser(object.getId()).map(UserMapper::mapUserToUserRestOutput).orElse(null);
    }
}
