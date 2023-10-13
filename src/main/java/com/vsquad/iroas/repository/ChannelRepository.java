package com.vsquad.iroas.repository;

import com.vsquad.iroas.aggregate.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Integer, Channel> {
}
