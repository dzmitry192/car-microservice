package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.dto.car.CarDto;
import com.innowise.carmicroservice.dto.car.CreateCarDto;
import com.innowise.carmicroservice.dto.car.UpdateCarDto;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.CarServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {

    private final CarServiceImpl carService;

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<CarDto>> getCars() {
        return ResponseEntity.ok(carService.getCars());
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId) throws NotFoundException {
        return ResponseEntity.ok().body(carService.getCarById(carId));
    }

    @PostMapping("/")
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CreateCarDto createCarDto) throws AlreadyExistsException {
        return ResponseEntity.ok().body(carService.createCar(createCarDto));
    }

    @PatchMapping("/{carId}")
    public ResponseEntity<CarDto> updateCarById(@PathVariable Long carId, @Valid @RequestBody UpdateCarDto updateCarDto) throws NotFoundException {
        return ResponseEntity.ok().body(carService.updateCarById(carId, updateCarDto));
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteCarById(@PathVariable Long carId) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok().body(carService.deleteCarById(carId));
    }
}