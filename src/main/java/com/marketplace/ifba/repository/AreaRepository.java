package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID> {
}
