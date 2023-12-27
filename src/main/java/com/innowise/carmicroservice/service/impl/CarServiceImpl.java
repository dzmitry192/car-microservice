package com.innowise.carmicroservice.service.impl;

import avro.ClientActionRequest;
import com.innowise.carmicroservice.dto.car.CarDto;
import com.innowise.carmicroservice.dto.car.CreateCarDto;
import com.innowise.carmicroservice.dto.car.UpdateCarDto;
import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.enums.ActionEnum;
import com.innowise.carmicroservice.enums.ActionTypeEnum;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.kafka.KafkaProducers;
import com.innowise.carmicroservice.mapper.CarMapperImpl;
import com.innowise.carmicroservice.repository.CarRepository;
import com.innowise.carmicroservice.security.service.CustomUserDetails;
import com.innowise.carmicroservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final KafkaProducers kafkaProducers;

    @Override
    public List<CarDto> getCars(Integer offset, Integer limit) {
        List<CarDto> carDtoList = carRepository.findAll(PageRequest.of(offset, limit)).stream().map(CarMapperImpl.INSTANCE::carEntityToCarDto).toList();
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_CARS.getAction(),
                ActionTypeEnum.GET_CARS_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return carDtoList;
    }

    @Override
    public CarDto getCarById(Long carId) throws NotFoundException {
        Optional<CarEntity> car = carRepository.findById(carId);
        if (car.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        CarDto carDto = CarMapperImpl.INSTANCE.carEntityToCarDto(car.get());
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_CAR.getAction() + carDto.getId(),
                ActionTypeEnum.GET_CARS_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return carDto;
    }

    @Override
    public CarDto createCar(CreateCarDto createCarDto) throws AlreadyExistsException {
        if (carRepository.existsByLicensePlate(createCarDto.getLicensePlate())) {
            throw new AlreadyExistsException("Car with license plate = " + createCarDto.getLicensePlate() + " is already exists");
        }
        CarDto carDto = CarMapperImpl.INSTANCE.carEntityToCarDto(carRepository.save(CarMapperImpl.INSTANCE.createCarDtoToCarEntity(createCarDto)));
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.CREATE_CAR.getAction() + carDto.getId(),
                ActionTypeEnum.CREATE_CAR_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return carDto;
    }

    @Override
    public CarDto updateCarById(Long carId, UpdateCarDto updateCarDto) throws NotFoundException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        CarEntity car = optionalCar.get();
        CarDto carDto = CarMapperImpl.INSTANCE.carEntityToCarDto(carRepository.save(CarMapperImpl.INSTANCE.updateCarDtoToCarEntity(updateCarDto, car)));
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.UPDATE_CAR.getAction() + carDto.getId(),
                ActionTypeEnum.UPDATE_CAR_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return carDto;
    }

    @Override
    public String deleteCarById(Long carId) throws NotFoundException, BadRequestException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if (optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        CarEntity car = optionalCar.get();
        if (car.isUsed()) {
            throw new BadRequestException("Cannot delete car with id = " + carId + " because it's already in use");
        }

        carRepository.delete(car);

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.DELETE_CAR.getAction() + car.getId(),
                ActionTypeEnum.DELETE_CAR_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return "Car with id = " + carId + " was successfully deleted";
    }
}
