package com.svvc.hackernews.service.user.impl;

import com.svvc.hackernews.model.FacebookObject;
import com.svvc.hackernews.model.User;
import com.svvc.hackernews.model.UserProfileForm;
import com.svvc.hackernews.repository.UserRepository;
import com.svvc.hackernews.service.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Base64;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

  private static final String ISSUER = "jeikernius";

  @Value("${oauthKey}")
  private String tokenKey;

  private final UserRepository userRepository;

  public UserServiceImpl(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * @see UserService#getUser(String)
   */
  @Override
  public Optional<User> getUser(final String id) {
    return userRepository.findById(id);
  }

  /**
   * @see UserService#createUser(OAuth2Authentication)
   */
  @Override
  public void createUser(OAuth2Authentication authentication) {
    Optional.ofNullable(authentication).ifPresent(a -> {
      LinkedHashMap<String, Object> details = (LinkedHashMap<String, Object>) a
          .getUserAuthentication().getDetails();
      if (!userRepository.findById((String) details.get("id")).isPresent()) {
        User user = new User((String) details.get("name"), (String) details.get("id"));
        addToken(user);
        userRepository.save(user);
      }
    });
  }

  @Override
  public void createUser(FacebookObject object) {
    Optional<User> user = getUser(object.getId());

    if (!user.isPresent()){
        User newUser = new User(object.getUserName(),object.getId());
        addToken(newUser);
        userRepository.save(newUser);
    }
  }

  /**
   * @see UserService#updateProfile(UserProfileForm, String)
   */
  public void updateProfile(UserProfileForm form, String userId) {
    User user = getUser(userId).get();
    user.updateProfile(form);
    userRepository.save(user);
  }

  /**
   * @see UserService#updateAllTokens()
   */
  public void updateAllTokens() {
    userRepository.findAll().forEach(user -> {
      addToken(user);
      userRepository.save(user);
    });
  }

  /**
   * @see UserService#validateToken(String)
   */
  @Override
  public Optional<User> validateToken(final String token)
      throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
    Jws<Claims> jws = Jwts.parserBuilder()
        .setSigningKey(getSecretKey())
        .requireIssuer(ISSUER)
        .build()
        .parseClaimsJws(token);

    return getUser(jws.getBody().getSubject());
  }

  /**
   * Creates a token and adds it to the given user.
   *
   * @param user The user to add the token to.
   */
  private void addToken(final User user) {
    user.setToken(Jwts.builder()
        .setIssuer(ISSUER)
        .setIssuedAt(Calendar.getInstance().getTime())
        .setSubject(user.getId())
        .setId(UUID.randomUUID().toString())
        .signWith(getSecretKey())
        .compact());
  }

  /**
   * Builds a {@link SecretKey} with a stored key on the resource files.
   *
   * @return The built {@link SecretKey}
   */
  private SecretKey getSecretKey() {
    byte[] decodedKey = Base64.getDecoder().decode(tokenKey);
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HMACSHA384");
  }
}
