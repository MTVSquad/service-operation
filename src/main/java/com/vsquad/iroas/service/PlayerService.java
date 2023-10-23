package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.AvatarDto;
import com.vsquad.iroas.aggregate.dto.ResPlayerInfoDto;
import com.vsquad.iroas.aggregate.entity.Avatar;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.repository.AvatarRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final AvatarRepository avatarRepository;

    @Transactional
    public void addPlayer(String steamKey, String nickname) {

        // 스팀 키 중복 체크
        playerRepository.findByPlayerSteamKey(steamKey).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 스팀 키");
        });

        // 닉네임 중복 체크
        Nickname newNickname = new Nickname(nickname);
        playerRepository.findByNickname(newNickname).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 닉네임");
        });

        Player newPlayer = new Player(steamKey, nickname);
        playerRepository.save(newPlayer);
    }

    @Transactional
    public void addPlayerAvatar(Long playerId, String maskColor) {
        Avatar avatar = new Avatar(playerId, maskColor);
        Avatar addedAvatar = avatarRepository.save(avatar);

         Player player = playerRepository.findById(playerId).orElseThrow(() -> {
             throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
         });

        player.setPlayerAvatar(addedAvatar.getAvatarId());
    }

    @Transactional
    public ResPlayerInfoDto changePlayerAvatar(Long playerId, String maskColor) {

        Avatar foundAvatar = avatarRepository.findByPlayerId(playerId).orElseThrow(() -> {
            throw new NoSuchElementException("아바타 정보를 찾을 수 없습니다.");
        });

        foundAvatar.setMask(maskColor);

        return readPlayerInfo(playerId);
    }

    @Transactional
    public ResPlayerInfoDto changePlayerNickname(Long playerId, String nickname) {

        Nickname newNickname = new Nickname(nickname);

        playerRepository.findByNickname(newNickname).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 닉네임");
        });

        Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> {
            throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
        });

        foundPlayer.setNickname(newNickname);

        ResPlayerInfoDto resPlayerDto = new ResPlayerInfoDto(foundPlayer.getPlayerId(), foundPlayer.getNickname().getPlayerNickname(), foundPlayer.getPlayerSteamKey(), null);

        return resPlayerDto;
    }

    public ResPlayerInfoDto readPlayerInfo(Long playerId) {

        Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> {
            throw new NoSuchElementException("저장된 플레이어가 없습니다.");
        });

        Long foundPlayerId = foundPlayer.getPlayerId();

        Avatar foundAvatar = avatarRepository.findByPlayerId(foundPlayerId).orElseThrow(() -> {
            throw new NoSuchElementException("저장된 아바타가 없습니다.");
        });

        String resPlayerNickname = foundPlayer.getNickname().getPlayerNickname();
        String resPlayerSteamKey = foundPlayer.getPlayerSteamKey();
        AvatarDto resPlayerAvatar = new AvatarDto(foundAvatar.getAvatarId(), foundAvatar.getMask());

        // player 정보 반환 하기 위해 dto로 변환
        ResPlayerInfoDto resPlayerDto = new ResPlayerInfoDto(foundPlayerId, resPlayerNickname, resPlayerSteamKey, resPlayerAvatar);

        return resPlayerDto;
    }

    @Transactional
    public void resetPlayerInfo(Long playerId) {
        Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> {
            throw new NoSuchElementException("저장된 플레이어가 없습니다.");
        });

        foundPlayer.setPlayerItems(null);
        foundPlayer.setPlayerMoney(0L);
        foundPlayer.setPlayerAvatar(null);
    }
}
