package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.entity.PriceEntity;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;

import java.util.List;

public interface PriceService {
    List<PriceEntity> getPrices();
    PriceEntity getPriceById(Long id) throws NotFoundException;
    PriceEntity createPrice(PriceEntity priceEntity) throws AlreadyExistsException;
    PriceEntity updatePriceById(Long priceId, PriceEntity priceEntity) throws NotFoundException;
    String deletePriceById(Long id) throws BadRequestException, NotFoundException;
}
