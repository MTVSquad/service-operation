package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.entity.Ranking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    @EntityGraph(attributePaths = {"player"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Ranking> findByCreatorMapId(String creatorMapId, Pageable pageable);

    Optional<Ranking> findByPlayerAndCreatorMapId(Player player, String creatorMapId);
}
