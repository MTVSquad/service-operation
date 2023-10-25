package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
}
