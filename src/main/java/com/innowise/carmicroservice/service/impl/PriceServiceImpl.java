package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.dto.price.CreatePriceDto;
import com.innowise.carmicroservice.dto.price.PriceDto;
import com.innowise.carmicroservice.dto.price.UpdatePriceDto;
import com.innowise.carmicroservice.entity.PriceEntity;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.mapper.PriceMapperImpl;
import com.innowise.carmicroservice.repository.PriceRepository;
import com.innowise.carmicroservice.repository.RentRepository;
import com.innowise.carmicroservice.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final RentRepository rentRepository;

    @Override
    public List<PriceDto> getPrices() {
        return PriceMapperImpl.INSTANCE.toPriceDtosList(priceRepository.findAll());
    }

    @Override
    public PriceDto getPriceById(Long id) throws NotFoundException {
        Optional<PriceEntity> price = priceRepository.findById(id);
        if (price.isPresent()) {
            return PriceMapperImpl.INSTANCE.priceEntityToPriceDto(price.get());
        } else {
            throw new NotFoundException("Price with id = " + id + " not found");
        }
    }

    @Override
    public PriceDto createPrice(CreatePriceDto createPriceDto) throws AlreadyExistsException {
        if (priceRepository.findByHourAndDay(createPriceDto.getHour(), createPriceDto.getDay()).isPresent()) {
            throw new AlreadyExistsException("Price with hour = " + createPriceDto.getHour() + ", day = " + createPriceDto.getDay() + " is already exists");
        } else {
            return PriceMapperImpl.INSTANCE.priceEntityToPriceDto(priceRepository.save(PriceMapperImpl.INSTANCE.createPriceDtoToPriceEntity(createPriceDto)));
        }
    }

    @Override
    public PriceDto updatePriceById(Long priceId, UpdatePriceDto updatePriceDto) throws NotFoundException {
        Optional<PriceEntity> optionalPrice = priceRepository.findById(priceId);
        if (optionalPrice.isPresent()) {
            PriceEntity price = optionalPrice.get();
            return PriceMapperImpl.INSTANCE.priceEntityToPriceDto(priceRepository.save(PriceMapperImpl.INSTANCE.updatePriceDtoToPriceEntity(updatePriceDto, price)));
        } else {
            throw new NotFoundException("Price with id = " + priceId + " not found");
        }
    }

    @Override
    public String deletePriceById(Long id) throws BadRequestException, NotFoundException {
        Optional<PriceEntity> optionalPrice = priceRepository.findById(id);
        if (optionalPrice.isPresent()) {
            PriceEntity price = optionalPrice.get();
            if (rentRepository.existsByPriceId(price.getId())) {
                throw new BadRequestException("Cannot remove price with id = " + id + " because it is in use");
            }
            priceRepository.delete(price);
            return "Price with id = " + id + " was successfully deleted";
        } else {
            throw new NotFoundException("Price with id = " + id + " not found");
        }
    }
}