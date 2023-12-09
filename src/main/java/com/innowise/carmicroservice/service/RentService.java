package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.dto.rent.CreateRentDto;
import com.innowise.carmicroservice.dto.rent.RentDto;
import com.innowise.carmicroservice.dto.rent.UpdateRentDto;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RentService {
    List<RentDto> getRents();

    RentDto getRentById(Long rentId) throws NotFoundException;
    boolean isHasRentOrReservation(Long clientId);

    RentDto createRent(Long carId, Long clientId, Long priceId, CreateRentDto createRentDto) throws NotFoundException, BadRequestException;

    RentDto updateRentById(Long rentId, UpdateRentDto updateRentDto) throws NotFoundException;

    String deleteRentById(Long rentId) throws NotFoundException;
}
