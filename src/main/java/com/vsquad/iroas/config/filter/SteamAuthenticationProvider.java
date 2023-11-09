package com.vsquad.iroas.config.filter;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SteamAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String steamId = authentication.getPrincipal().toString();

        // steamId 검증 로직
        // steamId가 유효하지 않으면 null을 반환하고, 유효하면 steamId를 반환한다.


        return new UsernamePasswordAuthenticationToken(steamId, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
