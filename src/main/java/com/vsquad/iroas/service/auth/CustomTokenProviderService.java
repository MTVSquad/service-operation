package com.vsquad.iroas.service.auth;

import antlr.Token;
import com.vsquad.iroas.aggregate.dto.ResPlayerInfoDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.config.OAuth2Config;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.config.token.TokenMapping;
import com.vsquad.iroas.repository.PlayerRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomTokenProviderService {

    private final OAuth2Config oAuth2Config;

    private final CustomUserDetailService customUserDetailService;

    private final PlayerRepository playerRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public TokenMapping createToken(Authentication authentication) {
        PlayerPrincipal playerPrincipal = (PlayerPrincipal) authentication.getPrincipal();

        Date now = new Date();

        Date accessTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getAccessTokenExpirationMsec());

        String secretKey = oAuth2Config.getAuth().getTokenSecret();

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(playerPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();

        return TokenMapping.builder()
                //.playerNickname(playerPrincipal.getName())
                .accessToken(accessToken)
                .build();
    }

    public String generateToken(String identifier) {

        Player player = playerRepository.findByPlayerSteamKey(identifier)
                .orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다."));

        ResPlayerInfoDto playerDto = ResPlayerInfoDto.convertToDto(player);

        // 고정된 secretKey
        String fixedSecretKeyString = secretKey;
        byte[] secretKey = Base64.getDecoder().decode(fixedSecretKeyString);

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(identifier)
                .claim("player", playerDto)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration)) // 10시간 유효
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getPlayerIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(oAuth2Config.getAuth().getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public UsernamePasswordAuthenticationToken getAuthenticationById(String token) throws Exception {
        Long playerId = getPlayerIdFromToken(token);
        UserDetails userDetails = customUserDetailService.loadPlayerById(playerId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authentication;
    }

    public Long getExpiration(String token) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(oAuth2Config.getAuth().getTokenSecret()).build().parseClaimsJws(token).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        //시간 계산
        return (expiration.getTime() - now);
    }

    public boolean validateToken(String token) {
        try {
            //log.info("bearerToken = {} \n oAuth2Config.getAuth()={}", token, oAuth2Config.getAuth().getTokenSecret());
            Jwts.parserBuilder().setSigningKey(oAuth2Config.getAuth().getTokenSecret()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException ex) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException ex) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException ex) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
