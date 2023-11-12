package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.CreatorMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorMapRepository extends JpaRepository<CreatorMap, String> {

}
