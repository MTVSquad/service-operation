package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByKeyAndType(String key, String type);

    Optional<Player> findByKey(String key);

    Optional<Player> findByNickname(Nickname nickname);

    // select count(nickname) from player where nickname like "@_%";
    @Query(value =
            "SELECT \n" +
            "    MAX(SUBSTRING_INDEX(p.player_nickname, '#', -1)) AS base_nickname \n" +
            "FROM \n" +
            "    (SELECT \n" +
            "            player_nickname as player_nickname \n" +
            "     FROM tb_player \n" +
            "     WHERE SUBSTRING_INDEX(player_nickname , '#', 1) = :nickname) as p", nativeQuery = true)
    Integer maxNumberByNickname(@Param("nickname") String nickname);

    int countByNicknameLike(Nickname nickname);
}
