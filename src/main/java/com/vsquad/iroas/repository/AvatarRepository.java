package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByPlayerId(Long playerId);

}
