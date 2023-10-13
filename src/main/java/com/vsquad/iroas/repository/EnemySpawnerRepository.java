package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemySpawnerRepository extends JpaRepository<Integer, EnemySpawner> {
}
