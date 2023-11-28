package com.innowise.carmicroservice.mapper;

import com.innowise.carmicroservice.dto.price.CreatePriceDto;
import com.innowise.carmicroservice.dto.price.PriceDto;
import com.innowise.carmicroservice.dto.price.UpdatePriceDto;
import com.innowise.carmicroservice.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = PriceMapper.class)
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    PriceDto priceEntityToPriceDto(PriceEntity priceEntity);
    PriceEntity priceDtoToPriceEntity(PriceDto priceDto);
    PriceEntity createPriceDtoToPriceEntity(CreatePriceDto priceDto);
    PriceEntity updatePriceDtoToPriceEntity(UpdatePriceDto updatePriceDto, @MappingTarget PriceEntity priceEntity);
    List<PriceDto> toPriceDtosList(List<PriceEntity> priceEntities);
    List<PriceEntity> toPriceEntitiesList(List<PriceDto> priceDtos);
}
