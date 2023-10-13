package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Integer, Item> {
}
