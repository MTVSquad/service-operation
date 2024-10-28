package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.aggregate.dto.response.ResPlayerInfoDto;
import com.vsquad.iroas.aggregate.entity.Avatar;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.config.exception.AvatarNotFoundException;
import com.vsquad.iroas.config.exception.NicknameAlreadyExistsException;
import com.vsquad.iroas.config.exception.PlayerNotFoundException;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.AvatarRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final AvatarRepository avatarRepository;

    @Transactional
    public PlayerDto addSteamPlayer(String key, String nickname, String type) {

        // 스팀 키 중복 체크
        playerRepository.findByKeyAndType(key, type).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 스팀 키");
        });

        // 닉네임 중복 체크
        Integer count = playerRepository.maxNumberByNickname(nickname);

        return generatePlayerWithIncrementedNickname(count, key, nickname, type);
    }

    @Transactional
    public PlayerDto addLocalPlayer(String key, String type) {

        // 아이디 중복 체크
        playerRepository.findByKeyAndType(key, type).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 아이디");
        });

        // 닉네임 중복 체크
        String nickname = "Local";

        Integer count = playerRepository.maxNumberByNickname(nickname);

        return generatePlayerWithIncrementedNickname(count, key, nickname, type);
    }

    private PlayerDto generatePlayerWithIncrementedNickname(Integer count, String key, String nickname, String type) {
        Player newPlayer;

        if(count != null && !count.equals(0)) {
            newPlayer = new Player(key, nickname + "#" + (count + 1), type, 0L, "ROLE_PLAYER");
        } else {
            newPlayer = new Player(key, nickname + "#" + 1, type, 0L, "ROLE_PLAYER");
        }

        Player savedPlayer = playerRepository.save(newPlayer);

        return new PlayerDto(savedPlayer);
    }

    @Transactional
    public void addPlayerAvatar(String maskColor) {
        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // UserDetails에서 사용자 정보 사용
        Long playerId = userDetails.getId();

        Avatar avatar = new Avatar(playerId, maskColor);
        Avatar addedAvatar = avatarRepository.save(avatar);

        Player player = playerRepository.findById(playerId).orElseThrow(() -> new NoSuchElementException("플레이어 정보를 찾을 수 없습니다."));

        player.setPlayerAvatar(addedAvatar.getAvatarId());
    }

    @Transactional
    public ResPlayerInfoDto changePlayerAvatar(String maskColor) {

        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long playerId = userDetails.getId();

        Avatar foundAvatar = avatarRepository.findByPlayerId(playerId).orElseThrow(() -> new AvatarNotFoundException("아바타 정보를 찾을 수 없습니다."));

        foundAvatar.setMask(maskColor);

        return readPlayerInfo();
    }

    @Transactional
    public ResPlayerInfoDto changePlayerNickname(String nickname) {

        playerRepository.findByNickname_PlayerNickname(nickname).ifPresent(player -> {
            throw new NicknameAlreadyExistsException("중복된 닉네임");
        });

        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long playerId = userDetails.getId();

        Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException("플레이어 정보를 찾을 수 없습니다."));

        foundPlayer.changeNickname(nickname);

        return new ResPlayerInfoDto(foundPlayer);
    }

    @Transactional
    public void resetPlayerInfo() {
        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // UserDetails에서 사용자 정보 사용
        Long playerId = userDetails.getId();

        Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException("저장된 플레이어가 없습니다."));

        foundPlayer.resetPlayerStatus();
    }

    public ResPlayerInfoDto readPlayerInfo() {
        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // UserDetails에서 사용자 정보 사용
        String key = userDetails.getUsername();
        String type = userDetails.getType();

        Player foundPlayer = playerRepository.findByKeyAndType(key, type).orElseThrow(() -> new PlayerNotFoundException("저장된 플레이어가 없습니다."));

        return new ResPlayerInfoDto(foundPlayer);
    }

    public PlayerDto readPlayer(String key, String type) {

        Player foundPlayer = playerRepository.findByKeyAndType(key, type).orElseThrow(() -> new PlayerNotFoundException("저장된 플레이어가 없습니다."));

        return new PlayerDto(foundPlayer);

    }
}
