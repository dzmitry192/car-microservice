package com.innowise.carmicroservice.mapper;

import com.innowise.carmicroservice.dto.rent.CreateRentDto;
import com.innowise.carmicroservice.dto.rent.RentDto;
import com.innowise.carmicroservice.dto.rent.UpdateRentDto;
import com.innowise.carmicroservice.entity.RentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = RentMapper.class)
public interface RentMapper {
    RentMapper INSTANCE = Mappers.getMapper(RentMapper.class);

    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "priceId", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    RentEntity createRentDtoToRentEntity(CreateRentDto createRentDto);
    RentEntity updateRentDtoToRentEntity(UpdateRentDto updateRentDto, @MappingTarget RentEntity rentEntity);
    RentDto rentEntityToRentDto(RentEntity rentEntity);
    RentEntity rentDtoToRentEntity(RentDto rentDto);
    List<RentDto> toRentDtosList(List<RentEntity> rentEntities);
    List<RentEntity> toRentEntitiesList(List<RentDto> rentDtos);
}
