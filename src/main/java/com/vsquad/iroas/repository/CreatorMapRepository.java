package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.CreatorMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorMapRepository extends JpaRepository<CreatorMap, String> {
    Page<CreatorMap> findAll(Pageable pageable);

    void deleteCreatorMapByCreatorMapIdAndCreator(String creatorMapId, String creator);

    Optional<CreatorMap> findByCreatorMapIdAndCreator(String creatorMapId, String creator);
}
