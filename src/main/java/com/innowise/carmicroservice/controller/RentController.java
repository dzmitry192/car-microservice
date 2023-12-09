package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.dto.rent.CreateRentDto;
import com.innowise.carmicroservice.dto.rent.RentDto;
import com.innowise.carmicroservice.dto.rent.UpdateRentDto;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.RentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rent")
@RequiredArgsConstructor
public class RentController {

    private final RentServiceImpl rentService;

    @GetMapping("/")
    public ResponseEntity<List<RentDto>> getRents() {
        return ResponseEntity.ok().body(rentService.getRents());
    }

    @GetMapping("/{rentId}")
    public ResponseEntity<RentDto> getRentById(@PathVariable Long rentId) throws NotFoundException {
        return ResponseEntity.ok().body(rentService.getRentById(rentId));
    }

    @PostMapping("/{clientId}/{carId}/{priceId}")
    public ResponseEntity<RentDto> createRent(
            @PathVariable Long clientId,
            @PathVariable Long carId,
            @PathVariable Long priceId,
            @Valid @RequestBody CreateRentDto createRentDto
    ) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok().body(rentService.createRent(carId, clientId, priceId, createRentDto));
    }

    @PatchMapping("/{rentId}")
    public ResponseEntity<RentDto> updateRentById(@PathVariable Long rentId, @Valid @RequestBody UpdateRentDto updateRentDto) throws NotFoundException {
        return ResponseEntity.ok().body(rentService.updateRentById(rentId, updateRentDto));
    }

    @DeleteMapping("/{rentId}")
    public ResponseEntity<String> deleteRentById(@PathVariable Long rentId) throws NotFoundException {
        return ResponseEntity.ok().body(rentService.deleteRentById(rentId));
    }
}
