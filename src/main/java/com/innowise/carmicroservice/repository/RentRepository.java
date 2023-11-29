package com.innowise.carmicroservice.repository;

import com.innowise.carmicroservice.entity.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RentRepository extends JpaRepository<RentEntity, Long> {
    boolean existsByPriceId(Long priceId);
}
