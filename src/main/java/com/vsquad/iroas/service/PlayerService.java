package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.aggregate.dto.response.ResPlayerInfoDto;
import com.vsquad.iroas.aggregate.entity.Avatar;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.config.exception.PlayerNotFoundException;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.AvatarRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
        Nickname newNickname = new Nickname(nickname);
        playerRepository.findByNickname(newNickname).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 닉네임");
        });

        Player newPlayer = new Player(key, nickname, type, 0L, "ROLE_PLAYER");
        Player savedPlayer = playerRepository.save(newPlayer);

        Long playerId = savedPlayer.getPlayerId();
        String playerSteamKey = savedPlayer.getKey();
        String playerNickname = savedPlayer.getNickname().getPlayerNickname();
        String playerType = savedPlayer.getType();
        String playerRole = savedPlayer.getPlayerRole();

        // player 정보 반환 하기 위해 dto로 변환
        PlayerDto playerDto = new PlayerDto(playerId, playerSteamKey, playerType, playerNickname, playerRole);
        return playerDto;
    }

    @Transactional
    public void addPlayerAvatar(String maskColor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long playerId = userDetails.getId();

            Avatar avatar = new Avatar(playerId, maskColor);
            Avatar addedAvatar = avatarRepository.save(avatar);

            Player player = playerRepository.findById(playerId).orElseThrow(() -> {
                throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
            });

            player.setPlayerAvatar(addedAvatar.getAvatarId());

        } else {
            throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public ResPlayerInfoDto changePlayerAvatar(String maskColor) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long playerId = userDetails.getId();

            Avatar foundAvatar = avatarRepository.findByPlayerId(playerId).orElseThrow(() -> {
                throw new NoSuchElementException("아바타 정보를 찾을 수 없습니다.");
            });

            foundAvatar.setMask(maskColor);

            return readPlayerInfo();
        } else {
            throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public ResPlayerInfoDto changePlayerNickname(String nickname) {

        Nickname newNickname = new Nickname(nickname);

        playerRepository.findByNickname(newNickname).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 닉네임");
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long playerId = userDetails.getId();

            Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> {
                throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
            });

            foundPlayer.setNickname(newNickname);

            ResPlayerInfoDto resPlayerDto = new ResPlayerInfoDto(foundPlayer.getPlayerId(), foundPlayer.getKey(), foundPlayer.getNickname().getPlayerNickname(), foundPlayer.getType(), foundPlayer.getPlayerRole());

            return resPlayerDto;
        } else {
            throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public void resetPlayerInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long playerId = userDetails.getId();

            Player foundPlayer = playerRepository.findById(playerId).orElseThrow(() -> {
                throw new NoSuchElementException("저장된 플레이어가 없습니다.");
            });

            foundPlayer.setPlayerItems(null);
            foundPlayer.setPlayerMoney(0L);
            foundPlayer.setPlayerAvatar(null);

        } else {
            throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
        }
    }

    public ResPlayerInfoDto readPlayerInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long playerId = userDetails.getId();
            String key = userDetails.getUsername();
            String type = userDetails.getType();

            Player foundPlayer = playerRepository.findByKeyAndType(key, type).orElseThrow(() -> {
                throw new NoSuchElementException("저장된 플레이어가 없습니다.");
            });

            Long foundPlayerId = foundPlayer.getPlayerId();

            String resPlayerNickname = foundPlayer.getNickname().getPlayerNickname();
            String resPlayerKey = foundPlayer.getKey();
            String resPlayerType = foundPlayer.getType();
            String resPlayerRole = foundPlayer.getPlayerRole();

            // player 정보 반환 하기 위해 dto로 변환
            ResPlayerInfoDto resPlayerDto = new ResPlayerInfoDto(foundPlayerId, resPlayerKey, resPlayerNickname, resPlayerType, resPlayerRole);
            return resPlayerDto;

        } else {
            throw new NoSuchElementException("플레이어 정보를 찾을 수 없습니다.");
        }
    }

    public PlayerDto addLocalPlayer(String key, String type) {

        // 아이디 중복 체크
        playerRepository.findByKeyAndType(key, type).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 아이디");
        });

        // 닉네임 중복 체크
        Nickname newNickname = new Nickname("Local" + "#" + key);
        playerRepository.findByNickname(newNickname).ifPresent(player -> {
            throw new IllegalArgumentException("중복된 닉네임");
        });

        Player newPlayer = new Player(key, newNickname.getPlayerNickname(), type, 0L, "ROLE_PLAYER");
        Player savedPlayer = playerRepository.save(newPlayer);

        Long playerId = savedPlayer.getPlayerId();
        String playerSteamKey = savedPlayer.getKey();
        String playerNickname = savedPlayer.getNickname().getPlayerNickname();
        String playerType = savedPlayer.getType();
        String playerRole = savedPlayer.getPlayerRole();

        // player 정보 반환 하기 위해 dto로 변환
        PlayerDto playerDto = new PlayerDto(playerId, playerSteamKey, playerNickname, playerType, playerRole);
        return playerDto;
    }

    public PlayerDto readPlayer(String key, String type) {

        Player foundPlayer = playerRepository.findByKeyAndType(key, type).orElseThrow(() -> {
            throw new PlayerNotFoundException("저장된 플레이어가 없습니다.");
        });

        Long playerId = foundPlayer.getPlayerId();
        String playerSteamKey = foundPlayer.getKey();
        String playerNickname = foundPlayer.getNickname().getPlayerNickname();
        String playerType = foundPlayer.getType();
        String playerRole = foundPlayer.getPlayerRole();

        // player 정보 반환 하기 위해 dto로 변환
        PlayerDto playerDto = new PlayerDto(playerId, playerSteamKey, playerNickname, playerType, playerRole);
        return playerDto;

    }

    public void logout() throws IllegalAccessException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // 로그아웃 처리 (예: 토큰 블랙리스트에 추가, 로그 기록 등)
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            throw new IllegalAccessException("인증 정보 없음");
        }
    }
}
