package com.svvc.hackernews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableOAuth2Sso
public class HackernewsApplication extends WebSecurityConfigurerAdapter
{

    public static void main(String[] args)
    {
        SpringApplication.run(HackernewsApplication.class, args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // endpoints to be authenticated
        http.csrf().disable().antMatcher("/**").authorizeRequests()
            .antMatchers("/login",
                "/create",
                "/item/create",
                "/reply/create",
                "/user/likes")
            .authenticated().and().logout().logoutSuccessUrl("/").permitAll();

    }
}
