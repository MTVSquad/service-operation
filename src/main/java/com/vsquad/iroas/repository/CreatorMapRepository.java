package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.CreatorMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorMapRepository extends JpaRepository<CreatorMap, Long> {
    Page<CreatorMap> findAll(Pageable pageable);
    Page<CreatorMap> findByCreator(Long creator, Pageable pageable);

    void deleteCreatorMapByCreatorMapIdAndCreator(Long creatorMapId, Long creator);

    Optional<CreatorMap> findByCreatorMapIdAndCreator(Long creatorMapId, Long creator);

    @Query(value =
            "SELECT\n" +
            "    MAX(SUBSTRING_INDEX(tcm.creator_tool_name, '_', -1)) AS base_nickname\n" +
            "FROM\n" +
            "    (SELECT\n" +
            "         creator_tool_name as creator_tool_name\n" +
            "     FROM tb_creator_map\n" +
            "     WHERE SUBSTRING_INDEX(creator_tool_name , '_', 1) = :creatorMapName) as tcm;", nativeQuery = true)
    Integer maxNumberByCreatorMapName(@Param("creatorMapName") String creatorMapName);
}
