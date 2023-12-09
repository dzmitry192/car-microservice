package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.dto.reservation.CreateReservationDto;
import com.innowise.carmicroservice.dto.reservation.ReservationDto;
import com.innowise.carmicroservice.dto.reservation.UpdateReservationDto;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;

import java.util.List;

public interface ReservationService {
    List<ReservationDto> getReservations();

    ReservationDto getReservationById(Long reservationId) throws NotFoundException;

    ReservationDto createReservation(Long carId, Long clientId, CreateReservationDto createReservationDto) throws NotFoundException, BadRequestException;

    ReservationDto updateReservationById(Long reservationId, UpdateReservationDto updateReservationDto) throws NotFoundException;

    String deleteReservationById(Long reservationId) throws NotFoundException;
}