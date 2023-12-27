package com.innowise.carmicroservice.service.impl;

import avro.ClientActionRequest;
import com.innowise.carmicroservice.dto.price.CreatePriceDto;
import com.innowise.carmicroservice.dto.price.PriceDto;
import com.innowise.carmicroservice.dto.price.UpdatePriceDto;
import com.innowise.carmicroservice.entity.PriceEntity;
import com.innowise.carmicroservice.enums.ActionEnum;
import com.innowise.carmicroservice.enums.ActionTypeEnum;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.kafka.KafkaProducers;
import com.innowise.carmicroservice.mapper.PriceMapperImpl;
import com.innowise.carmicroservice.repository.PriceRepository;
import com.innowise.carmicroservice.repository.RentRepository;
import com.innowise.carmicroservice.security.service.CustomUserDetails;
import com.innowise.carmicroservice.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final RentRepository rentRepository;
    private final KafkaProducers kafkaProducers;

    @Override
    public List<PriceDto> getPrices() {
        List<PriceDto> priceDtoList = PriceMapperImpl.INSTANCE.toPriceDtosList(priceRepository.findAll());
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_PRICES.getAction(),
                ActionTypeEnum.GET_PRICES_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return priceDtoList;
    }

    @Override
    public PriceDto getPriceById(Long id) throws NotFoundException {
        Optional<PriceEntity> price = priceRepository.findById(id);
        if (price.isPresent()) {
            PriceDto priceDto = PriceMapperImpl.INSTANCE.priceEntityToPriceDto(price.get());

            kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                    ActionEnum.GET_PRICE.getAction() + priceDto.getId(),
                    ActionTypeEnum.GET_PRICES_TYPE.name(),
                    LocalDateTime.now().toString()
            ));

            return priceDto;
        } else {
            throw new NotFoundException("Price with id = " + id + " not found");
        }
    }

    @Override
    public PriceDto createPrice(CreatePriceDto createPriceDto) throws AlreadyExistsException {
        if (priceRepository.findByHourAndDay(createPriceDto.getHour(), createPriceDto.getDay()).isPresent()) {
            throw new AlreadyExistsException("Price with hour = " + createPriceDto.getHour() + ", day = " + createPriceDto.getDay() + " is already exists");
        } else {
            PriceDto priceDto = PriceMapperImpl.INSTANCE.priceEntityToPriceDto(priceRepository.save(PriceMapperImpl.INSTANCE.createPriceDtoToPriceEntity(createPriceDto)));

            kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                    ActionEnum.CREATE_PRICE.getAction() + priceDto.getId(),
                    ActionTypeEnum.CREATE_PRICE_TYPE.name(),
                    LocalDateTime.now().toString()
            ));

            return priceDto;
        }
    }

    @Override
    public PriceDto updatePriceById(Long priceId, UpdatePriceDto updatePriceDto) throws NotFoundException {
        Optional<PriceEntity> optionalPrice = priceRepository.findById(priceId);
        if (optionalPrice.isPresent()) {
            PriceEntity price = optionalPrice.get();
            PriceDto priceDto = PriceMapperImpl.INSTANCE.priceEntityToPriceDto(priceRepository.save(PriceMapperImpl.INSTANCE.updatePriceDtoToPriceEntity(updatePriceDto, price)));

            kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                    ActionEnum.CREATE_PRICE.getAction() + priceDto.getId(),
                    ActionTypeEnum.CREATE_PRICE_TYPE.name(),
                    LocalDateTime.now().toString()
            ));

            return priceDto;
        } else {
            throw new NotFoundException("Price with id = " + priceId + " not found");
        }
    }

    @Override
    public String deletePriceById(Long priceId) throws BadRequestException, NotFoundException {
        Optional<PriceEntity> optionalPrice = priceRepository.findById(priceId);
        if (optionalPrice.isPresent()) {
            PriceEntity price = optionalPrice.get();
            if (rentRepository.existsByPriceId(price.getId())) {
                throw new BadRequestException("Cannot remove price with id = " + priceId + " because it is in use");
            }
            priceRepository.delete(price);

            kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                    ActionEnum.CREATE_PRICE.getAction() + priceId,
                    ActionTypeEnum.CREATE_PRICE_TYPE.name(),
                    LocalDateTime.now().toString()
            ));

            return "Price with id = " + priceId + " was successfully deleted";
        } else {
            throw new NotFoundException("Price with id = " + priceId + " not found");
        }
    }
}