package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.entity.ReservationEntity;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.ReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationServiceImpl reservationService;

    @GetMapping("/")
    public ResponseEntity<List<ReservationEntity>> getReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations());
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable Long reservationId) throws NotFoundException {
        return ResponseEntity.ok().body(reservationService.getReservationById(reservationId));
    }

    @PostMapping("/{clientId}/{carId}")
    public ResponseEntity<ReservationEntity> createReservation(@PathVariable Long clientId, @PathVariable Long carId, @Valid @RequestBody ReservationEntity reservationEntity) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok().body(reservationService.createReservation(carId, clientId, reservationEntity));
    }

    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationEntity> updateReservationById(@PathVariable Long reservationId, @Valid @RequestBody ReservationEntity reservationEntity) throws NotFoundException {
        return ResponseEntity.ok().body(reservationService.updateReservationById(reservationId, reservationEntity));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> deleteReservationById(@PathVariable Long reservationId) throws NotFoundException {
        return ResponseEntity.ok().body(reservationService.deleteReservationById(reservationId));
    }

}
