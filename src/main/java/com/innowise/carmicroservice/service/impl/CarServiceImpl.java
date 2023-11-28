package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.dto.car.CarDto;
import com.innowise.carmicroservice.dto.car.CreateCarDto;
import com.innowise.carmicroservice.dto.car.UpdateCarDto;
import com.innowise.carmicroservice.entity.CarEntity;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.mapper.CarMapperImpl;
import com.innowise.carmicroservice.repository.CarRepository;
import com.innowise.carmicroservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public List<CarDto> getCars() {
        return carRepository.findAll().stream().map(CarMapperImpl.INSTANCE::carEntityToCarDto).toList();
    }

    @Override
    public CarDto getCarById(Long carId) throws NotFoundException {
        Optional<CarEntity> car = carRepository.findById(carId);
        if(car.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        return CarMapperImpl.INSTANCE.carEntityToCarDto(car.get());
    }

    @Override
    public CarDto createCar(CreateCarDto createCarDto) throws AlreadyExistsException {
        if(carRepository.existsByLicensePlate(createCarDto.getLicensePlate())) {
            throw new AlreadyExistsException("Car with license plate = " + createCarDto.getLicensePlate() + " is already exists");
        }
        return CarMapperImpl.INSTANCE.carEntityToCarDto(carRepository.save(CarMapperImpl.INSTANCE.createCarDtoToCarEntity(createCarDto)));
    }

    @Override
    public CarDto updateCarById(Long carId, UpdateCarDto updateCarDto) throws NotFoundException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if(optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        return CarMapperImpl.INSTANCE.carEntityToCarDto(CarMapperImpl.INSTANCE.updateCarDtoToCarEntity(updateCarDto, optionalCar.get()));
    }

    @Override
    public String deleteCarById(Long carId) throws NotFoundException, BadRequestException {
        Optional<CarEntity> optionalCar = carRepository.findById(carId);
        if(optionalCar.isEmpty()) {
            throw new NotFoundException("Car with id = " + carId + " not found");
        }
        if(optionalCar.get().isUsed()) {
            throw new BadRequestException("Cannot delete car with id = " + carId + " because it's already in use");
        }

        carRepository.delete(optionalCar.get());

        return "Car with id = " + carId + " was successfully deleted";
    }
}
