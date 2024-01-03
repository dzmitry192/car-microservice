package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.dto.reservation.CreateReservationDto;
import com.innowise.carmicroservice.dto.reservation.ReservationDto;
import com.innowise.carmicroservice.dto.reservation.UpdateReservationDto;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.ReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/car/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationServiceImpl reservationService;

    @GetMapping("/")
    public ResponseEntity<List<ReservationDto>> getReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations());
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long reservationId) throws NotFoundException {
        return ResponseEntity.ok().body(reservationService.getReservationById(reservationId));
    }

    @PostMapping("/{clientId}/{carId}")
    public ResponseEntity<ReservationDto> createReservation(@PathVariable Long clientId, @PathVariable Long carId, @Valid @RequestBody CreateReservationDto createReservationDto) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok().body(reservationService.createReservation(carId, clientId, createReservationDto));
    }

    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationDto> updateReservationById(@PathVariable Long reservationId, @Valid @RequestBody UpdateReservationDto updateReservationDto) throws NotFoundException {
        return ResponseEntity.ok().body(reservationService.updateReservationById(reservationId, updateReservationDto));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> deleteReservationById(@PathVariable Long reservationId) throws NotFoundException {
        return ResponseEntity.ok().body(reservationService.deleteReservationById(reservationId));
    }
}