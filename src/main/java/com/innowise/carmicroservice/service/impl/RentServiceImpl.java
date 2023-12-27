package com.innowise.carmicroservice.service.impl;

import avro.ClientActionRequest;
import avro.NotificationRequest;
import com.innowise.carmicroservice.dto.rent.CreateRentDto;
import com.innowise.carmicroservice.dto.rent.RentDto;
import com.innowise.carmicroservice.dto.rent.UpdateRentDto;
import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.entity.RentEntity;
import com.innowise.carmicroservice.enums.ActionEnum;
import com.innowise.carmicroservice.enums.ActionTypeEnum;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.kafka.KafkaProducers;
import com.innowise.carmicroservice.mapper.RentMapperImpl;
import com.innowise.carmicroservice.repository.*;
import com.innowise.carmicroservice.security.service.CustomUserDetails;
import com.innowise.carmicroservice.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final CarRepository carRepository;
    private final PriceRepository priceRepository;
    private final KafkaProducers kafkaProducers;

    @Override
    public List<RentDto> getRents() {
        List<RentDto> rentDtosList = RentMapperImpl.INSTANCE.toRentDtosList(rentRepository.findAll());

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_RENTS.getAction(),
                ActionTypeEnum.GET_RENTS_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return rentDtosList;
    }

    @Override
    public RentDto getRentById(Long rentId) throws NotFoundException {
        Optional<RentEntity> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty()) {
            throw new NotFoundException("Rent with id = " + rentId + " not found");
        }
        RentDto rentDto = RentMapperImpl.INSTANCE.rentEntityToRentDto(optionalRent.get());

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_RENT.getAction() + rentDto.getId(),
                ActionTypeEnum.GET_RENTS_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return rentDto;
    }

    @Override
    public boolean isHasRentOrReservation(Long clientId) {
        boolean hasRent = rentRepository.existsByClientId(clientId);
        boolean hasReservation = reservationRepository.existsByClientId(clientId);
        return !hasRent && !hasReservation;
    }

    @Override
    public RentDto createRent(Long carId, Long clientId, Long priceId, CreateRentDto createRentDto) throws NotFoundException, BadRequestException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        CarEntity car = optionalCar.get();
        if (car.isUsed()) {
            throw new BadRequestException("Car with id = " + carId + " is already in use");
        }
        if (priceRepository.findById(priceId).isEmpty()) {
            throw new NotFoundException("Price with id = " + priceId + " not found");
        }

        RentEntity rent = RentMapperImpl.INSTANCE.createRentDtoToRentEntity(createRentDto);
        rent.setCarId(carId);
        rent.setClientId(clientId);
        rent.setPriceId(priceId);
        RentEntity savedRent = rentRepository.save(rent);

        car.setUsed(true);
        car.setRentId(savedRent.getId());
        carRepository.save(car);

        RentDto rentDto = RentMapperImpl.INSTANCE.rentEntityToRentDto(savedRent);

        kafkaProducers.sendNotificationRequest(new NotificationRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getEmail(),
                "Аренда авто",
                "Вы успешно арендовали авто с номером - " + car.getLicensePlate() + " на период с " + rent.getRentalStartDate() + " до " + rent.getRentalEndDate() + " с суммой оплаты - " + rent.getRentalAmount()
        ));

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.CREATE_RENT.getAction() + rentDto.getId(),
                ActionTypeEnum.CREATE_RENT_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return rentDto;
    }

    @Override
    public RentDto updateRentById(Long rentId, UpdateRentDto updateRentDto) throws NotFoundException {
        Optional<RentEntity> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty()) {
            throw new NotFoundException("Rent with id = " + rentId + " not found");
        }
        RentEntity rent = optionalRent.get();
        RentDto rentDto = RentMapperImpl.INSTANCE.rentEntityToRentDto(rentRepository.save(RentMapperImpl.INSTANCE.updateRentDtoToRentEntity(updateRentDto, rent)));

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.UPDATE_RENT.getAction() + rentDto.getId(),
                ActionTypeEnum.UPDATE_RENT_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return rentDto;
    }

    @Override
    public String deleteRentById(Long rentId) throws NotFoundException {
        Optional<RentEntity> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty()) {
            throw new NotFoundException("Rent with id = " + rentId + " not found");
        }
        RentEntity rent = optionalRent.get();

        CarEntity car = carRepository.findById(rent.getCarId()).get();
        car.setUsed(false);
        car.setRentId(null);
        carRepository.save(car);

        rentRepository.delete(rent);

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.DELETE_RENT.getAction() + rentId,
                ActionTypeEnum.DELETE_RENT_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return "Rent with id = " + rentId + " was successfully deleted";
    }
}
