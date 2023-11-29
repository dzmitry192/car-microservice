package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.dto.car.CarDto;
import com.innowise.carmicroservice.dto.car.CreateCarDto;
import com.innowise.carmicroservice.dto.car.UpdateCarDto;
import com.innowise.carmicroservice.exception.AlreadyExistsException;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;

import java.util.List;

public interface CarService {
    List<CarDto> getCars(Integer offset, Integer limit);

    CarDto getCarById(Long carId) throws NotFoundException;

    CarDto createCar(CreateCarDto carEntity) throws AlreadyExistsException;

    CarDto updateCarById(Long carId, UpdateCarDto updateCarDto) throws NotFoundException;

    String deleteCarById(Long carId) throws NotFoundException, BadRequestException;
}
