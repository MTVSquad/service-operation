package com.vsquad.iroas.config.token;

import com.vsquad.iroas.aggregate.entity.Player;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PlayerPrincipal implements OAuth2User, UserDetails {

    private Long id;
    private String nickname;

    public PlayerPrincipal(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static PlayerPrincipal create(Player player) {

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(player.getPlayerRole()));

        return new PlayerPrincipal(
            player.getPlayerId(),
            player.getNickname().getPlayerNickname()
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return nickname;
    }
}
