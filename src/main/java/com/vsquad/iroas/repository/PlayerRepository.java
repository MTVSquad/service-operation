package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByPlayerSteamKey(String playerSteamKey);

    Player findByNickname(Nickname nickname);
}
