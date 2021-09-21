package com.svvc.hackernews.controllers.base;

import java.security.Principal;
import java.util.Optional;


public abstract class BaseController
{
    protected String getIdUser(Principal principal)
    {
        return Optional.ofNullable(principal).map(Principal::getName).orElse(null);
    }
}
