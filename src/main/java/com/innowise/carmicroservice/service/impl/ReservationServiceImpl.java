package com.innowise.carmicroservice.service.impl;

import avro.ClientActionRequest;
import avro.NotificationRequest;
import avro.UserDetails;
import com.innowise.carmicroservice.dto.reservation.CreateReservationDto;
import com.innowise.carmicroservice.dto.reservation.ReservationDto;
import com.innowise.carmicroservice.dto.reservation.UpdateReservationDto;
import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.entity.ReservationEntity;
import com.innowise.carmicroservice.enums.ActionEnum;
import com.innowise.carmicroservice.enums.ActionTypeEnum;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.kafka.KafkaProducers;
import com.innowise.carmicroservice.mapper.ReservationMapperImpl;
import com.innowise.carmicroservice.repository.CarRepository;
import com.innowise.carmicroservice.repository.ReservationRepository;
import com.innowise.carmicroservice.security.service.CustomUserDetails;
import com.innowise.carmicroservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private final KafkaProducers kafkaProducers;

    @Override
    public List<ReservationDto> getReservations() {
        List<ReservationDto> reservationDtoList = ReservationMapperImpl.INSTANCE.toReservationDtosList(reservationRepository.findAll());

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_RESERVATIONS.getAction(),
                ActionTypeEnum.GET_RESERVATIONS_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return reservationDtoList;
    }

    @Override
    public ReservationDto getReservationById(Long reservationId) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            ReservationDto reservationDto = ReservationMapperImpl.INSTANCE.reservationEntityToReservationDto(optionalReservation.get());

            kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                    ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                    ActionEnum.GET_RESERVATION.getAction() + reservationDto.getId(),
                    ActionTypeEnum.GET_RESERVATIONS_TYPE.name(),
                    LocalDateTime.now().toString()
            ));

            return reservationDto;
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

        ReservationDto reservationDto = ReservationMapperImpl.INSTANCE.reservationEntityToReservationDto(reservationRepository.save(reservation));

        kafkaProducers.sendNotificationRequest(new NotificationRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getEmail(),
                "Бронирование авто",
                "Вы успешно забронировали авто с номером - " + car.getLicensePlate() + " на период с " + reservation.getReservationStartDate() + " до " + reservation.getReservationEndDate()
        ));

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.CREATE_RESERVATION.getAction() + reservationDto.getId(),
                ActionTypeEnum.CREATE_RESERVATION_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return reservationDto;
    }

    @Override
    public ReservationDto updateReservationById(Long reservationId, UpdateReservationDto updateReservationDto) throws NotFoundException {
        Optional<ReservationEntity> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation with id =" + reservationId + " not found");
        }
        ReservationEntity reservation = optionalReservation.get();
        ReservationDto reservationDto = ReservationMapperImpl.INSTANCE.reservationEntityToReservationDto(reservationRepository.save(ReservationMapperImpl.INSTANCE.updateReservationDtoToReservationEntity(updateReservationDto, reservation)));

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.UPDATE_RESERVATION.getAction() + reservationDto.getId(),
                ActionTypeEnum.UPDATE_RESERVATION_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return reservationDto;
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

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.DELETE_RESERVATION.getAction() + reservationId,
                ActionTypeEnum.DELETE_RESERVATION_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return "Reservation with id = " + reservationId + " was successfully deleted";
    }
}
