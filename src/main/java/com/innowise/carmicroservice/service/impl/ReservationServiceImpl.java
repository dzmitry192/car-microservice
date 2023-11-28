package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.entity.ReservationEntity;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.repository.CarRepository;
import com.innowise.carmicroservice.repository.ReservationRepository;
import com.innowise.carmicroservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationEntity> getReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public ReservationEntity getReservationById(Long reservationId) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if(optionalReservation.isPresent()) {
            return optionalReservation.get();
        } else {
            throw new NotFoundException("Reservation with id = " + reservationId + " not found");
        }
    }

    @Override
    public ReservationEntity createReservation(Long carId, Long clientId, ReservationEntity reservationEntity) throws NotFoundException, BadRequestException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if(optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        CarEntity car = optionalCar.get();
        if(car.isUsed()) {
            throw new BadRequestException("Cannot set reservation because it's already in use");
        }

        reservationEntity.setCarId(carId);
        reservationEntity.setClientId(clientId);

        return reservationRepository.save(reservationEntity);
    }

    @Override
    public ReservationEntity updateReservationById(Long reservationId, ReservationEntity reservationEntity) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if(optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation with id =" + reservationId + " not found");
        }
        ReservationEntity reservation  = optionalReservation.get();
        reservation.setReservationStartDate(reservationEntity.getReservationStartDate());
        reservation.setReservationEndDate(reservationEntity.getReservationEndDate());

        return reservationRepository.save(reservation);
    }

    @Override
    public String deleteReservationById(Long reservationId) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if(optionalReservation.isEmpty()) {
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
