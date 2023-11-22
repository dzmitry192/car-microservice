package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.entity.PriceEntity;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.repository.PriceRepository;
import com.innowise.carmicroservice.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public List<PriceEntity> getPrices() {
        return priceRepository.findAll();
    }

    @Override
    public PriceEntity getPriceById(Long id) throws NotFoundException {
        Optional<PriceEntity> price = priceRepository.findById(id);
        if(price.isPresent()) {
            return price.get();
        } else {
            throw new NotFoundException("Price with id = " + id + " not found");
        }
    }

    @Override
    public PriceEntity createPrice(PriceEntity price) throws AlreadyExistsException {
        if(priceRepository.findByHourAndDay(price.getHour(), price.getDay()).isPresent()) {
            throw new AlreadyExistsException("Price with hour = " + price.getHour() + ", day = " + price.getDay() + " is already exists");
        } else {
            return priceRepository.save(price);
        }
    }

    @Override
    public PriceEntity updatePriceById(Long priceId, PriceEntity priceEntity) throws NotFoundException {
        Optional<PriceEntity> price = priceRepository.findById(priceId);

        if(price.isPresent()) {
            price.get().setHour(priceEntity.getHour());
            price.get().setDay(priceEntity.getDay());
            return priceRepository.save(price.get());
        } else {
            throw new NotFoundException("Price with id = " + priceId + " not found");
        }
    }

    @Override
    public String deletePriceById(Long id) throws BadRequestException, NotFoundException {
        Optional<PriceEntity> optionalPrice = priceRepository.findById(id);
        if(optionalPrice.isPresent()) {
            PriceEntity price = optionalPrice.get();
            if(price.isUsed()) {
                throw new BadRequestException("Cannot remove price with id = " + id + " because it is in use");
            }
            priceRepository.delete(price);
            return "Прайс с id = " + id + " был успешно удалён";
        } else {
            throw new NotFoundException("Price with id = " + id + " not found");
        }
    }
}