package com.vsquad.iroas.service.auth;

import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.config.OAuth2Config;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.config.token.TokenMapping;
import com.vsquad.iroas.repository.PlayerRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

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

    public String generateToken(PlayerDto player) {

        String identifier = player.getPlayerSteamKey();

        // 고정된 secretKey
        String fixedSecretKeyString = secretKey;
        byte[] secretKey = Base64.getDecoder().decode(fixedSecretKeyString);

        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(identifier)
                .claim("player", player)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration)) // 10시간 유효
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPlayerIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().toString();
    }

    public UsernamePasswordAuthenticationToken getAuthenticationById(String token) throws Exception {
        String steamId = getPlayerIdFromToken(token);
        UserDetails userDetails = customUserDetailService.loadPlayerById(steamId);
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

    public Authentication getAuthentication(String token) {
        // JWT에서 사용자 이름 추출
        String username = getUsernameFromToken(token);

        // 여기서는 간단한 예로 username을 기반으로 Authentication 객체를 생성합니다.
        // 실제 구현에서는 이 사용자 이름을 이용하여 사용자의 세부 정보를 불러오는 과정이 필요할 수 있습니다.
        return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_PLAYER")));
    }

    // JWT에서 사용자 이름을 추출하는 메소드
    private String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // JWT에서 특정 클레임을 추출하는 범용 메소드
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // JWT에서 모든 클레임을 추출하는 메소드
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
