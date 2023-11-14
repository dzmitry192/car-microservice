package com.innowise.carmicroservice.repository;

import com.innowise.carmicroservice.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
}
