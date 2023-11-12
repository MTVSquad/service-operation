package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByKeyAndType(String key, String type);

    Optional<Player> findByKey(String key);

    Optional<Player> findByNickname(Nickname nickname);
}
