package com.svvc.hackernews.controllers.base;

import java.util.Optional;

import org.springframework.lang.Nullable;

import com.svvc.hackernews.model.AuthUser;


public abstract class BaseRestController
{
    protected String getIdUser(@Nullable AuthUser user)
    {
        return Optional.ofNullable(user).map(u -> u.id).orElse(null);
    }
}
