package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findByCreatorMapId(String creatorMapId);

    Optional<Ranking> findByPlayerIdAndCreatorMapId(Long playerId, String creatorMapId);
}
