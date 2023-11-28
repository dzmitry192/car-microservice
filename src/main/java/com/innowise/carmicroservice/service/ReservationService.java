package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.entity.ReservationEntity;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;

import java.util.List;

public interface ReservationService {
    List<ReservationEntity> getReservations();
    ReservationEntity getReservationById(Long reservationId) throws NotFoundException;
    ReservationEntity createReservation(Long carId, Long clientId, ReservationEntity reservationEntity) throws NotFoundException, BadRequestException;
    ReservationEntity updateReservationById(Long reservationId, ReservationEntity reservationEntity) throws NotFoundException;
    String deleteReservationById(Long reservationId) throws NotFoundException;
}