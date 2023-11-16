package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.PlayData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayDataRepository extends JpaRepository<PlayData, Long> {
}
