package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.entity.PriceEntity;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.PriceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
public class PriceController {

    private final PriceServiceImpl priceService;

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<PriceEntity>> getPrices() {
        return ResponseEntity.ok().body(priceService.getPrices());
    }

    @GetMapping("/{priceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<PriceEntity> getPriceById(@PathVariable Long priceId) throws NotFoundException {
        return ResponseEntity.ok().body(priceService.getPriceById(priceId));
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<PriceEntity> createPrice(@Valid @RequestBody PriceEntity priceEntity) throws AlreadyExistsException {
        return ResponseEntity.ok().body(priceService.createPrice(priceEntity));
    }

    @PatchMapping("/{priceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<PriceEntity> updatePriceById(@PathVariable Long priceId, @Valid @RequestBody PriceEntity priceEntity) throws NotFoundException {
        return ResponseEntity.ok().body(priceService.updatePriceById(priceId, priceEntity));
    }

    @DeleteMapping("/{priceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<String> deletePriceById(@PathVariable Long priceId) throws BadRequestException, NotFoundException {
        return ResponseEntity.ok().body(priceService.deletePriceById(priceId));
    }
}
