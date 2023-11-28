package com.innowise.carmicroservice.mapper;

import com.innowise.carmicroservice.dto.reservation.CreateReservationDto;
import com.innowise.carmicroservice.dto.reservation.ReservationDto;
import com.innowise.carmicroservice.dto.reservation.UpdateReservationDto;
import com.innowise.carmicroservice.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    ReservationEntity createReservationDtoToReservationEntity(CreateReservationDto createReservationDto);
    ReservationEntity updateReservationDtoToReservationEntity(UpdateReservationDto updateReservationDto, @MappingTarget ReservationEntity reservationEntity);
    ReservationDto reservationEntityToReservationDto(ReservationEntity reservationEntity);
    ReservationEntity reservationDtoToReservationEntity(ReservationDto reservationDto);
    List<ReservationDto> toReservationDtosList(List<ReservationEntity> reservationEntities);
    List<ReservationEntity> toReservationEntitiesList(List<ReservationDto> reservationDtos);
}
