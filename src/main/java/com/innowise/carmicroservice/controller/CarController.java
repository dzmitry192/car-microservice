package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.service.impl.CarServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
@AllArgsConstructor
public class CarController {

    private final CarServiceImpl carService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> getCars() {
        return ResponseEntity.ok(carService.getCars());
    }

}
