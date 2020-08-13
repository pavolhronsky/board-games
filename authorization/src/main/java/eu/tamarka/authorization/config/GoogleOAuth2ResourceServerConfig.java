package eu.tamarka.authorization.config;

import eu.tamarka.authorization.config.properties.OAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class GoogleOAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

  private OAuthProperties oAuthProperties;

  @Autowired
  public GoogleOAuth2ResourceServerConfig(OAuthProperties oAuthProperties) {
    this.oAuthProperties = oAuthProperties;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .cors()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests().anyRequest().authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(oAuthProperties.getClientId());
  }
}