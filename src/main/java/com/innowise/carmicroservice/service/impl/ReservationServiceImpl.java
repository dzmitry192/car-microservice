package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.dto.reservation.CreateReservationDto;
import com.innowise.carmicroservice.dto.reservation.ReservationDto;
import com.innowise.carmicroservice.dto.reservation.UpdateReservationDto;
import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.entity.ReservationEntity;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.mapper.ReservationMapperImpl;
import com.innowise.carmicroservice.repository.CarRepository;
import com.innowise.carmicroservice.repository.ReservationRepository;
import com.innowise.carmicroservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationDto> getReservations() {
        return ReservationMapperImpl.INSTANCE.toReservationDtosList(reservationRepository.findAll());
    }

    @Override
    public ReservationDto getReservationById(Long reservationId) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            return ReservationMapperImpl.INSTANCE.reservationEntityToReservationDto(optionalReservation.get());
        } else {
            throw new NotFoundException("Reservation with id = " + reservationId + " not found");
        }
    }

    @Override
    public ReservationDto createReservation(Long carId, Long clientId, CreateReservationDto createReservationDto) throws NotFoundException, BadRequestException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        CarEntity car = optionalCar.get();
        if (car.isUsed()) {
            throw new BadRequestException("Cannot set reservation because it's already in use");
        }

        ReservationEntity reservation = ReservationMapperImpl.INSTANCE.createReservationDtoToReservationEntity(createReservationDto);
        reservation.setCarId(carId);
        reservation.setClientId(clientId);

        car.setUsed(true);
        carRepository.save(car);

        return ReservationMapperImpl.INSTANCE.reservationEntityToReservationDto(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDto updateReservationById(Long reservationId, UpdateReservationDto updateReservationDto) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation with id =" + reservationId + " not found");
        }
        ReservationEntity reservation = optionalReservation.get();
        return ReservationMapperImpl.INSTANCE.reservationEntityToReservationDto(reservationRepository.save(ReservationMapperImpl.INSTANCE.updateReservationDtoToReservationEntity(updateReservationDto, reservation)));
    }

    @Override
    public String deleteReservationById(Long reservationId) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation with id =" + reservationId + " not found");
        }
        ReservationEntity reservation = optionalReservation.get();

        CarEntity car = carRepository.findById(reservation.getId()).get();
        car.setUsed(false);
        carRepository.save(car);

        reservationRepository.delete(reservation);

        return "Reservation with id = " + reservationId + " was successfully deleted";
    }
}
