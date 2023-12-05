package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.dto.rent.CreateRentDto;
import com.innowise.carmicroservice.dto.rent.RentDto;
import com.innowise.carmicroservice.dto.rent.UpdateRentDto;
import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.entity.RentEntity;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.mapper.RentMapperImpl;
import com.innowise.carmicroservice.repository.*;
import com.innowise.carmicroservice.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public List<RentDto> getRents() {
        return RentMapperImpl.INSTANCE.toRentDtosList(rentRepository.findAll());
    }

    @Override
    public RentDto getRentById(Long rentId) throws NotFoundException {
        Optional<RentEntity> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty()) {
            throw new NotFoundException("Rent with id = " + rentId + " not found");
        }
        return RentMapperImpl.INSTANCE.rentEntityToRentDto(optionalRent.get());
    }

    @Override
    public boolean isHasRentOrReservation(Long clientId) {
        boolean hasRent = rentRepository.existsByClientId(clientId);
        boolean hasReservation = reservationRepository.existsByClientId(clientId);
        if(!hasRent && !hasReservation) {
            return true;
        } else {
            return false;
        }
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

        return RentMapperImpl.INSTANCE.rentEntityToRentDto(savedRent);
    }

    @Override
    public RentDto updateRentById(Long rentId, UpdateRentDto updateRentDto) throws NotFoundException {
        Optional<RentEntity> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty()) {
            throw new NotFoundException("Rent with id = " + rentId + " not found");
        }
        RentEntity rent = optionalRent.get();
        return RentMapperImpl.INSTANCE.rentEntityToRentDto(rentRepository.save(RentMapperImpl.INSTANCE.updateRentDtoToRentEntity(updateRentDto, rent)));
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

        return "Rent with id = " + rentId + " was successfully deleted";
    }
}
