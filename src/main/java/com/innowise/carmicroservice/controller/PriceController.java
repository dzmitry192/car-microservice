package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.dto.price.CreatePriceDto;
import com.innowise.carmicroservice.dto.price.PriceDto;
import com.innowise.carmicroservice.dto.price.UpdatePriceDto;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.PriceServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
@Validated
public class PriceController {

    private final PriceServiceImpl priceService;

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<PriceDto>> getPrices() {
        return ResponseEntity.ok().body(priceService.getPrices());
    }

    @GetMapping("/{priceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<PriceDto> getPriceById(@PathVariable Long priceId) throws NotFoundException {
        return ResponseEntity.ok().body(priceService.getPriceById(priceId));
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<PriceDto> createPrice(@Valid @RequestBody CreatePriceDto createPriceDto, BindingResult bindingResult) throws AlreadyExistsException {
        System.out.println(bindingResult.getFieldErrors());
        return ResponseEntity.ok().body(priceService.createPrice(createPriceDto));
    }

    @PatchMapping("/{priceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<PriceDto> updatePriceById(@PathVariable Long priceId, @Valid @RequestBody UpdatePriceDto updatePriceDto) throws NotFoundException {
        return ResponseEntity.ok().body(priceService.updatePriceById(priceId, updatePriceDto));
    }

    @DeleteMapping("/{priceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<String> deletePriceById(@PathVariable Long priceId) throws BadRequestException, NotFoundException {
        return ResponseEntity.ok().body(priceService.deletePriceById(priceId));
    }
}
