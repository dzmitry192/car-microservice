package com.innowise.carmicroservice.mapper;

import com.innowise.carmicroservice.dto.car.CarDto;
import com.innowise.carmicroservice.dto.car.CreateCarDto;
import com.innowise.carmicroservice.dto.car.UpdateCarDto;
import com.innowise.carmicroservice.entity.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = CarMapper.class)
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "used", constant = "false")
    @Mapping(target = "rentId", ignore = true)
    CarEntity createCarDtoToCarEntity(CreateCarDto createCarDto);
    CarEntity updateCarDtoToCarEntity(UpdateCarDto updateCarDto, @MappingTarget CarEntity carEntity);
    CarDto carEntityToCarDto(CarEntity carEntity);
    CarEntity carDtoToCarEntity(CarDto carDto);
    List<CarDto> toCarDtosList(List<CarEntity> carEntities);
    List<CarEntity> toCarEntitiesList(List<CarDto> carDtos);
}
