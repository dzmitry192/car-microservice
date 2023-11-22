package com.innowise.carmicroservice.repository;

import com.innowise.carmicroservice.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
    Optional<PriceEntity> findByHourAndDay(int hour, int day);
}
