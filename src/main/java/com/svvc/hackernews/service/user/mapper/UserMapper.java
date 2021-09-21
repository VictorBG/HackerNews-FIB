package com.svvc.hackernews.service.user.mapper;

import com.svvc.hackernews.model.AuthUser;
import com.svvc.hackernews.model.User;
import com.svvc.hackernews.model.UserProfileForm;
import com.svvc.hackernews.model.restoutput.CompleteUserRestOutput;
import com.svvc.hackernews.model.restoutput.ProfileUpdateRestInput;
import com.svvc.hackernews.model.restoutput.UserRestOutput;


public class UserMapper
{

    public static CompleteUserRestOutput mapUserToUserRestOutput(final User user)
    {
        CompleteUserRestOutput output = new CompleteUserRestOutput();

        output.setAbout(user.getAbout());
        output.setCreatedAt(user.getCreatedAt());
        output.setKarma(user.getKarma());
        output.setUsername(user.getUsername());
        output.setDelay(user.getDelay());
        output.setEmail(user.getEmail());
        output.setMaxVisit(user.getMaxVisit());
        output.setMinAway(user.getMinAway());
        output.setNoprocast(user.isNoprocast());
        output.setShowDead(user.isShowDead());
        output.setId(user.getId());
        output.setToken(user.getToken());

        return output;
    }

    public static UserRestOutput mapExternalUserToExternalUserRestOutput(final User user)
    {
        UserRestOutput output = new UserRestOutput();
        output.setAbout(user.getAbout());
        output.setCreatedAt(user.getCreatedAt());
        output.setKarma(user.getKarma());
        output.setUsername(user.getUsername());

        return output;
    }

    public static UserProfileForm mapUpdateInputToUserForm(ProfileUpdateRestInput input)
    {
        UserProfileForm form = new UserProfileForm();
        form.setAbout(input.getAbout());
        form.setDelay(String.valueOf(input.getDelay()));
        form.setMaxv(String.valueOf(input.getMaxv()));
        form.setMina(String.valueOf(input.getMina()));
        form.setNopro(input.getNopro() ? "yes" : "no");
        form.setShowd(input.getShowd() ? "yes" : "no");
        form.setUemail(input.getUemail());
        return form;
    }

    public static AuthUser mapToAuthUser(final User user)
    {
        return new AuthUser(user.getId(), user.getUsername());
    }
}
