package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.entity.Enemy;
import com.vsquad.iroas.repository.EnemyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EnemyServiceTest {

    @Autowired
    private EnemyRepository enemyRepository;

    private Enemy enemy;

    @Test
    @DisplayName("근접 에너미 추가")
    void addEnemy() {
        // given
        enemy = new Enemy("close_range_1", "근거리1", "close range", 100L, null, 10L, 1, "map1");
        enemyRepository.save(enemy);

        // when
        Enemy enemy = enemyRepository.findByEnemyCodeAndCreatorMap("close_range_1", "map1")
                .orElseThrow(() -> new IllegalArgumentException("해당 에너미가 없습니다."));

        // then
        assertNotNull(enemy);
    }
}