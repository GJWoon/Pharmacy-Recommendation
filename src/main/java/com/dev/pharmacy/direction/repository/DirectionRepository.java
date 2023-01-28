package com.dev.pharmacy.direction.repository;


import com.dev.pharmacy.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction,Long> {
}
