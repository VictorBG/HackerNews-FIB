package com.svvc.hackernews.service.user;

import com.svvc.hackernews.model.FacebookObject;
import java.util.Optional;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.svvc.hackernews.model.User;
import com.svvc.hackernews.model.UserProfileForm;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;


public interface UserService
{

    /**
     * Returns a {@link User} with the given {@param id} as ID.
     *
     * @param id The ID of the user
     * @return  {@link User}
     */
    Optional<User> getUser(String id);

    /**
     * Given an {@link OAuth2Authentication}, creates an user.
     *
     * Usually it is called when the user has performed a login and then checks if it should created
     * and creates if not.
     *
     * @param authentication {@link OAuth2Authentication} of the login process
     */
    void createUser(OAuth2Authentication authentication);

    void createUser(FacebookObject object);

    /**
     * Updates a profile for a given user {@param principal}
     *
     * @param form          Form with the new data of the user
     * @param userId         The user idus
     */
    void updateProfile(UserProfileForm form, String userId);


    /**
     * Updates all tokens from all users.
     *
     * For testing purposes only.
     */
    void updateAllTokens();

    /**
     * Returns the user associated with the given token.
     *
     * If no user is associated it returns null. If token is malformed it throws an exception.
     *
     * @param token Token to retrieve the user
     * @return User associated with the token, if any
     */
    Optional<User> validateToken(String token)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException;
}
