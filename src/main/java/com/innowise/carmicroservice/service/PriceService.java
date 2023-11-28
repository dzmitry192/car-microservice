package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.dto.price.CreatePriceDto;
import com.innowise.carmicroservice.dto.price.PriceDto;
import com.innowise.carmicroservice.dto.price.UpdatePriceDto;
import com.innowise.carmicroservice.entity.PriceEntity;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;

import java.util.List;

public interface PriceService {
    List<PriceDto> getPrices();
    PriceDto getPriceById(Long id) throws NotFoundException;
    PriceDto createPrice(CreatePriceDto createPriceDto) throws AlreadyExistsException;
    PriceDto updatePriceById(Long priceId, UpdatePriceDto updatePriceDto) throws NotFoundException;
    String deletePriceById(Long id) throws BadRequestException, NotFoundException;
}
