package com.vsquad.iroas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class OAuth2Config {

    private final Auth oAuth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Data
    public static class Auth {
        private String tokenSecret;
        private long accessTokenExpirationMsec;
        private long refreshTokenExpirationMsec;
    }

    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectsUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() {
        return oAuth;
    }

    public OAuth2 getOAuth2() {
        return oauth2;
    }
}
