package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnemyRepository extends JpaRepository<Enemy, Long> {

    Optional<Enemy> findByEnemyCodeAndCreatorMapId(String enemyCode, String creatorMapId);
}
