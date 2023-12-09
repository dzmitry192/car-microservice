package com.innowise.carmicroservice.repository;

import com.innowise.carmicroservice.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    boolean existsByClientId(Long clientId);
}
