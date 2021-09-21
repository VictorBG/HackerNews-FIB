package com.svvc.hackernews.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import com.svvc.hackernews.service.user.mapper.UserMapper;
import com.svvc.hackernews.service.user.UserService;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;


/**
 * Extracts the user from the Authorization HTTP header
 */
@ControllerAdvice(annotations = AuthAwareRestController.class)
public class AuthHandler
{
    final UserService userService;

    public AuthHandler(final UserService userService)
    {
        this.userService = userService;
    }

    @ModelAttribute
    public void validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, final Model model)
    {
        try
        {
            userService.validateToken(token).map(UserMapper::mapToAuthUser).ifPresent(model::addAttribute);
        }
        catch (UnsupportedJwtException unsupportedJwtException)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header value does not represent a Claims JWS");
        }
        catch (MalformedJwtException malformedJwtException)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header value is not a valid JWS");
        }
        catch (IllegalArgumentException signatureException)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header value is null or empty or only whitespace");
        }
        catch (SignatureException signatureException)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Authorization header value is not from a trusted source and therefore cannot be validated");
        }
        catch (Exception exception)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
        }
    }
}
