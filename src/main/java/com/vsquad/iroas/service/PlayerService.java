package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.entity.Avatar;
import com.vsquad.iroas.aggregate.entity.Player;
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
    public void changePlayerAvatar(Long playerId, String maskColor) {

        Avatar foundAvatar = avatarRepository.findByPlayerId(playerId).orElseThrow(() -> {
            throw new NoSuchElementException("아바타 정보를 찾을 수 없습니다.");
        });

        foundAvatar.setMask(maskColor);
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
