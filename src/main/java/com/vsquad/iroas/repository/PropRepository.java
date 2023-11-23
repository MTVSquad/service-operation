package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Prop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropRepository extends JpaRepository<Prop, Long> {
}
