package eu.tamarka.authorization.config.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import eu.tamarka.authorization.config.properties.OAuthProperties;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;

@Service
public class GoogleTokenServices implements ResourceServerTokenServices {

  private final Logger log = LogManager.getLogger(this.getClass());

  private AccessTokenConverter converter;
  private GoogleIdTokenVerifier verifier;

  @Autowired
  public GoogleTokenServices(OAuthProperties oAuthProperties) throws GeneralSecurityException, IOException {
    log.debug("Google client ID for this application: {}.", oAuthProperties.getClientId());
    converter = new DefaultAccessTokenConverter();
    verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
        .setAudience(Collections.singletonList(oAuthProperties.getClientId()))
        .build();
  }

  @Override
  public OAuth2Authentication loadAuthentication(String idTokenString) {
    log.info("Authentication started for idToken={}.", idTokenString);

    GoogleIdToken idToken = null;
    try {
      idToken = verifier.verify(idTokenString);
      log.debug("IdToken obtained: {}.", idToken);
    } catch (GeneralSecurityException | IOException e) {
      log.warn("Token verification thrown exception: {}.", e.getMessage(), e);
    }
    if (null == idToken) {
      throw new UnapprovedClientAuthenticationException("Token verification failed. Check expiration.");
    }

    return getAuthentication(idToken.getPayload());
  }

  private OAuth2Authentication getAuthentication(Payload payload) {
    OAuth2Request request = converter.extractAuthentication(Collections.emptyMap()).getOAuth2Request();
    Authentication authentication = getAuthenticationToken(payload);
    return new OAuth2Authentication(request, authentication);
  }

  private Authentication getAuthenticationToken(Payload payload) {
    String id = payload.getSubject();
    if (null == id) {
      throw new InternalAuthenticationServiceException("Cannot get id from payload.");
    }
    GooglePrincipal principal = new GooglePrincipal.Builder()
        .withUserId(id)
        .withEmail(payload.getEmail())
        .withName((String) payload.get("name"))
        .withGivenName((String) payload.get("given_name"))
        .withFamilyName((String) payload.get("family_name"))
        .build();

    return new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
  }

  @Override
  public OAuth2AccessToken readAccessToken(String accessToken) {
    throw new UnsupportedOperationException("Not supported: read access token.");
  }
}